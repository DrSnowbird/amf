package amf.plugins.document.webapi.parser.spec.declaration

import amf.core.annotations.ExplicitField
import amf.core.metamodel.domain.ShapeModel
import amf.core.metamodel.domain.extensions.PropertyShapeModel
import amf.core.model.domain._
import amf.core.model.domain.extensions.PropertyShape
import amf.core.parser.{Annotations, ScalarNode, _}
import amf.core.utils.Strings
import amf.core.vocabulary.Namespace
import amf.plugins.document.webapi.annotations.{CollectionFormatFromItems, Inferred, JSONSchemaId}
import amf.plugins.document.webapi.contexts.{OasWebApiContext, WebApiContext}
import amf.plugins.document.webapi.parser.OasTypeDefMatcher.matchType
import amf.plugins.document.webapi.parser.spec._
import amf.plugins.document.webapi.parser.spec.OasDefinitions
import amf.plugins.document.webapi.parser.spec.common.AnnotationParser
import amf.plugins.document.webapi.parser.spec.domain.{ExampleOptions, NodeDataNodeParser, RamlExamplesParser}
import amf.plugins.document.webapi.parser.spec.oas.OasSpecParser
import amf.plugins.domain.shapes.annotations.NilUnion
import amf.plugins.domain.shapes.metamodel._
import amf.plugins.domain.shapes.models.TypeDef._
import amf.plugins.domain.shapes.models._
import amf.plugins.domain.shapes.parser.XsdTypeDefMapping
import amf.plugins.domain.webapi.annotations.TypePropertyLexicalInfo
import org.yaml.model._
import org.yaml.render.YamlRender

import scala.collection.mutable

class JSONSchemaVersion(val name: String)
class OASSchemaVersion(override val name: String, val position: String) extends JSONSchemaVersion(name) {
  if (position != "schema" && position != "parameter")
    throw new Exception(s"Invalid schema position '$position', only 'schema' and 'parameter' are valid")
}
class OAS20SchemaVersion(override val position: String) extends OASSchemaVersion("oas2.0", position)
object OAS20SchemaVersion { def apply(position: String) = new OAS20SchemaVersion(position) }
class OAS30SchemaVersion(override val position: String) extends OASSchemaVersion("oas3.0.0", position)
object OAS30SchemaVersion { def apply(position: String) = new OAS20SchemaVersion(position) }
object JSONSchemaVersion extends JSONSchemaVersion("json-schema")

/**
  * OpenAPI Type Parser.
  */
object OasTypeParser {

  def apply(entry: YMapEntry, adopt: Shape => Unit, version: JSONSchemaVersion)(
      implicit ctx: WebApiContext): OasTypeParser =
    new OasTypeParser(Left(entry), entry.key.as[String], entry.value.as[YMap], adopt, version)(toOas(ctx))

  def apply(entry: YMapEntry, adopt: Shape => Unit)(implicit ctx: WebApiContext): OasTypeParser =
    new OasTypeParser(Left(entry), entry.key.as[String], entry.value.as[YMap], adopt, OAS20SchemaVersion("schema"))(
      toOas(ctx))

  def apply(node: YNode, name: String, adopt: Shape => Unit, version: JSONSchemaVersion)(
      implicit ctx: WebApiContext): OasTypeParser =
    new OasTypeParser(Right(node), name, node.as[YMap], adopt, version)(toOas(ctx))

  def apply(node: YNode, name: String, adopt: Shape => Unit)(implicit ctx: WebApiContext): OasTypeParser =
    new OasTypeParser(Right(node), name, node.as[YMap], adopt, OAS20SchemaVersion("schema"))(toOas(ctx))
}

case class OasTypeParser(entryOrNode: Either[YMapEntry, YNode],
                         name: String,
                         map: YMap,
                         adopt: Shape => Unit,
                         version: JSONSchemaVersion)(implicit val ctx: OasWebApiContext)
    extends OasSpecParser {

  private val ast: YPart = entryOrNode match {
    case Left(l)  => l
    case Right(r) => r
  }

  def parse(): Option[AnyShape] = {

    if (detectDisjointUnion()) {
      Some(parseDisjointUnionType())
    } else {
      val parsedShape = detect(version) match {
        case UnionType                   => Some(parseUnionType())
        case LinkType                    => parseLinkType()
        case ObjectType                  => Some(parseObjectType())
        case ArrayType                   => Some(parseArrayType())
        case AnyType                     => Some(parseAnyType())
        case typeDef if typeDef.isScalar => Some(parseScalarType(typeDef))
        case _                           => None
      }
      parsedShape match {
        case Some(shape: AnyShape) =>
          if (isOas) // external schemas can have any top level key
            ctx.closedShape(shape.id, map, version.asInstanceOf[OASSchemaVersion].position)
          if (isOas3) Some(checkNilUnion(shape))
          else Some(shape)
        case None => None
      }
    }
  }

  protected def isOas: Boolean  = version.isInstanceOf[OASSchemaVersion]
  protected def isOas3: Boolean = version.isInstanceOf[OAS30SchemaVersion]

  def checkNilUnion(parsed: AnyShape): AnyShape = {
    map.key("nullable") match {
      case Some(nullableEntry) if nullableEntry.value.toOption[Boolean].getOrElse(false) =>
        val union = UnionShape().withName(name).withId(parsed.id + "/nilUnion")
        parsed.annotations += NilUnion(Range(nullableEntry.key.range).toString())
        union.withAnyOf(
          Seq(
            parsed,
            NilShape().withId(union.id + "_nil")
          )
        )
        union
      case _ =>
        parsed
    }
  }

  /**
    * JSON Schema allows to define multiple types for a shape.
    * In this case we can parse this as a union because properties
    * are going to be disjoint for each of them
    * @return
    */
  private def detectDisjointUnion(): Boolean = {
    map.key("type").isDefined && map.key("type").get.value.asOption[YSequence].isDefined
  }

  private def detect(version: JSONSchemaVersion): TypeDef = {
    val defaultType = version match {
      case oasSchema: OASSchemaVersion if oasSchema.position == "parameter" => UndefinedType
      case _                                                                => AnyType
    }

    detectDependency()
      .orElse(detectType())
      .orElse(detectObjectProperties())
      .orElse(detectUnion())
      .orElse(detectItemProperties())
      .orElse(detectNumberProperties())
      .orElse(detectStringProperties())
      .getOrElse(defaultType)
  }

  private def detectObjectProperties(): Option[TypeDef.ObjectType.type] =
    map
      .key("properties")
      .orElse(map.key("x-amf-merge"))
      .orElse(map.key("minProperties"))
      .orElse(map.key("maxProperties"))
      .orElse(map.key("dependencies"))
      .orElse(map.key("patternProperties"))
      .orElse(map.key("additionalProperties"))
      .orElse(map.key("required"))
      .map(_ => ObjectType)

  private def detectItemProperties(): Option[TypeDef.ArrayType.type] =
    map
      .key("items")
      .orElse(map.key("minItems"))
      .orElse(map.key("maxItems"))
      .orElse(map.key("uniqueItems"))
      .map(_ => ArrayType)

  private def detectNumberProperties(): Option[TypeDef.NumberType.type] =
    map
      .key("multipleOf")
      .orElse(map.key("minimum"))
      .orElse(map.key("maximum"))
      .orElse(map.key("exclusiveMinimum"))
      .orElse(map.key("exclusiveMaximum"))
      .map(_ => NumberType)

  private def detectStringProperties(): Option[TypeDef.StrType.type] =
    map
      .key("minLength")
      .orElse(map.key("maxLength"))
      .orElse(map.key("pattern"))
      .orElse(map.key("format"))
      .map(_ => StrType)

  private def detectDependency(): Option[TypeDef] = map.key("$ref").map(_ => LinkType)

  private def detectUnion(): Option[TypeDef.UnionType.type] = {
    map.key("x-amf-union").map(_ => UnionType)
  }

  private def detectType(): Option[TypeDef] = {
    map
      .key("type")
      .map(e => {
        val t = e.value.as[YScalar].text
        val f = map.key("format").flatMap(e => e.value.toOption[YScalar].map(_.text)).getOrElse("")
        matchType(t, f)
      })
  }

  private def parseDisjointUnionType(): UnionShape = {

    val detectedTypes = map.key("type").get.value.as[YSequence].nodes.map(_.as[String])
    val filtered      = YMap(map.entries.filter(_.key.as[String] != "type"))

    val union = UnionShapeParser(Right(filtered), name).parse()
    adopt(union)

    val exclusiveProps = YMap(filtered.entries.filter { entry =>
      val prop = entry.key.as[String]
      prop != "example" && prop != "examples".asOasExtension && prop != "title" &&
      prop != "description" && prop != "default" && prop != "enum" &&
      prop != "externalDocs" && prop != "xml" && prop != "facets".asOasExtension &&
      prop != "anyOf" && prop != "allOf" && prop != "oneOf" && prop != "not"
    })

    val parsedTypes = detectedTypes map {
      case "object" => Some(parseObjectType(name, exclusiveProps, (s) => s.withId(union.id + "/object")))
      case "array"  => Some(parseArrayType(name, exclusiveProps, (s) => s.withId(union.id + "/array")))
      case "number" =>
        Some(parseScalarType(TypeDef.NumberType, name, exclusiveProps, (s) => s.withId(union.id + "/number")))
      case "integer" =>
        Some(parseScalarType(TypeDef.IntType, name, exclusiveProps, (s) => s.withId(union.id + "/integer")))
      case "string" =>
        Some(parseScalarType(TypeDef.StrType, name, exclusiveProps, (s) => s.withId(union.id + "/string")))
      case "boolean" =>
        Some(parseScalarType(TypeDef.BoolType, name, exclusiveProps, (s) => s.withId(union.id + "/boolean")))
      case "any"             => Some(parseAnyType(name, exclusiveProps, (s) => s.withId(union.id + "/any")))
      case _                 => None
    } collect { case Some(t) => t }

    if (parsedTypes.nonEmpty) union.setArrayWithoutId(UnionShapeModel.AnyOf, parsedTypes)

    union
  }

  private def parseScalarType(typeDef: TypeDef,
                              name: String = name,
                              map: YMap = map,
                              adopt: (Shape) => Unit = adopt): AnyShape = {
    val parsed = typeDef match {
      case NilType =>
        val shape = NilShape(ast).withName(name)
        adopt(shape)
        shape
      case FileType =>
        val shape = FileShape(ast).withName(name)
        adopt(shape)
        FileShapeParser(typeDef, shape, map).parse()
      case _ =>
        val shape = ScalarShape(ast).withName(name)
        adopt(shape)
        ScalarShapeParser(typeDef, shape, map).parse()
    }
    parsed
  }

  private def parseAnyType(name: String = name, map: YMap = map, adopt: (Shape) => Unit = adopt): AnyShape = {
    val shape = AnyShape(ast).withName(name)
    adopt(shape)
    AnyTypeShapeParser(shape, map).parse()
  }

  private def parseArrayType(name: String = name, map: YMap = map, adopt: (Shape) => Unit = adopt): AnyShape = {
    DataArrangementParser(name, ast, map, (shape: Shape) => adopt(shape)).parse()
  }

  private def parseLinkType(): Option[AnyShape] = {
    map
      .key("$ref")
      .flatMap { e =>
        e.value.tagType match {
          case YType.Null => None // we dont have to register violation because web api reference handler already do it
          case _ =>
            val ref: String = e.value
            val text        = OasDefinitions.stripDefinitionsPrefix(ref)
            ctx.declarations.findType(text, SearchScope.All) match {
              case Some(s) =>
                val copied =
                  s.link(text, Annotations(ast)).asInstanceOf[AnyShape].withName(name).withSupportsRecursion(true)
                adopt(copied)
                Some(copied)
              case None
                  if !isOas => // Only enabled for JSON Schema, not OAS. In OAS local references can only point to the #/definitions (#/components in OAS 3) node
                // now we work with canonical JSON schema pointers, not local refs
                val fullRef = ctx.resolvedPath(ctx.rootContextDocument, ref)
                ctx.findJsonSchema(fullRef) match {
                  case Some(s) =>
                    val annots = Annotations(ast)
                    val copied = s.link(text, annots).asInstanceOf[AnyShape].withName(name).withSupportsRecursion(true)
                    adopt(copied)
                    Some(copied)
                  // Local reference
                  case None =>
                    // This is not the normal mechanism, just a way of having something to prevent the recursion
                    // Introduces the problem of an invalid link
                    val tmpShape =
                      UnresolvedShape(fullRef, map).withName(fullRef).withId(fullRef).withSupportsRecursion(true)
                    tmpShape.withContext(ctx)
                    adopt(tmpShape)
                    ctx.registerJsonSchema(fullRef, tmpShape)

                    if (ref.startsWith("#")) {
                      ctx.findLocalJSONPath(ref) match {
                        case Some((_, shapeNode)) =>
                          OasTypeParser(YMapEntry(name, shapeNode), adopt, version)
                            .parse()
                            .map { shape =>
                              ctx.futureDeclarations.resolveRef(fullRef, shape)
                              tmpShape.resolve(shape) // useless?
                              ctx.registerJsonSchema(fullRef, shape)
                              shape
                            } orElse {
                            Some(tmpShape)
                          }
                        case None =>
                          ctx.violation(tmpShape.id, s"Cannot find local JSON Schema reference $ref", e.value)
                          Some(tmpShape)
                      }
                    } else {
                      // remote reference
                      ctx.parseRemoteJSONPath(fullRef).map { shape =>
                        ctx.registerJsonSchema(fullRef, shape)
                        ctx.futureDeclarations.resolveRef(fullRef, shape)
                        shape
                      } match {
                        case None =>
                          // it might still be resolvable at the RAML (not JSON Schema) level
                          tmpShape.unresolved(text, map).withSupportsRecursion(true)
                          Some(tmpShape)
                        case someShape => someShape
                      }
                    }
                }
              case other =>
                // normal path for a variable in a regular OAS, not inlined JSON Schema
                val shape = UnresolvedShape(text, map).withName(text)
                shape.withContext(ctx)
                shape.unresolved(text, map)
                adopt(shape)
                Some(shape)
            }
        }
      }
  }

  private def parseObjectType(name: String = name, map: YMap = map, adopt: (Shape) => Unit = adopt): AnyShape = {
    if (map.key("schema".asOasExtension).isDefined) {
      val shape = SchemaShape(ast).withName(name)
      adopt(shape)
      SchemaShapeParser(shape, map).parse()
    } else {
      val shape = NodeShape(ast).withName(name)
      adopt(shape)
      NodeShapeParser(shape, map).parse()
    }
  }

  private def parseUnionType(): UnionShape = {
    UnionShapeParser(entryOrNode, name).parse()
  }

  trait CommonScalarParsingLogic {
    def parseScalar(map: YMap, shape: Shape): Unit = {
      map.key("pattern", ScalarShapeModel.Pattern in shape)
      map.key("minLength", ScalarShapeModel.MinLength in shape)
      map.key("maxLength", ScalarShapeModel.MaxLength in shape)

      map.key("minimum", entry => {
        val value = ScalarNode(entry.value)
        shape.set(ScalarShapeModel.Minimum, value.text(), Annotations(entry))
      })

      map.key("maximum", entry => {
        val value = ScalarNode(entry.value)
        shape.set(ScalarShapeModel.Maximum, value.text(), Annotations(entry))
      })

      map.key("exclusiveMinimum", ScalarShapeModel.ExclusiveMinimum in shape)
      map.key("exclusiveMaximum", ScalarShapeModel.ExclusiveMaximum in shape)
      map.key("format", ScalarShapeModel.Format in shape)
      map.key("multipleOf", ScalarShapeModel.MultipleOf in shape)

//      shape.set(ScalarShapeModel.Repeat, value = false)

    }
  }

  case class ScalarShapeParser(typeDef: TypeDef, shape: ScalarShape, map: YMap)
      extends AnyShapeParser()
      with CommonScalarParsingLogic {
    override def parse(): ScalarShape = {
      super.parse()
      map
        .key("type")
        .fold(shape
          .set(ScalarShapeModel.DataType, AmfScalar(XsdTypeDefMapping.xsd(typeDef)), Annotations() += Inferred()))(
          entry => shape.set(ScalarShapeModel.DataType, AmfScalar(XsdTypeDefMapping.xsd(typeDef)), Annotations(entry)))

      parseScalar(map, shape)

      shape
    }
  }

  case class UnionShapeParser(nodeOrEntry: Either[YMapEntry, YNode], name: String) extends AnyShapeParser() {

    val node = nodeOrEntry match {
      case Left(entry) => entry.value
      case Right(node) => node
    }
    override val map: YMap = node.as[YMap]

    override val shape: UnionShape = UnionShape(Annotations.valueNode(node)).withName(name)

    override def parse(): UnionShape = {
      super.parse()

      map.key(
        "x-amf-union", { entry =>
          entry.value.to[Seq[YNode]] match {
            case Right(seq) =>
              val unionNodes = seq.zipWithIndex
                .map {
                  case (unionNode, index) =>
                    val entry = YMapEntry(YNode(s"item$index"), unionNode)
                    OasTypeParser(entry, item => item.adopted(shape.id + "/items/" + index), version).parse()
                }
                .filter(_.isDefined)
                .map(_.get)
              shape.setArray(UnionShapeModel.AnyOf, unionNodes, Annotations(entry.value))
            case _ =>
              ctx.violation(shape.id, "Unions are built from multiple shape nodes", entry.value)

          }
        }
      )

      shape
    }
  }

  case class OrConstraintParser(map: YMap, shape: Shape) {

    def parse(): Unit = {
      map.key(
        "anyOf", { entry =>
          entry.value.to[Seq[YNode]] match {
            case Right(seq) =>
              val unionNodes = seq.zipWithIndex
                .map {
                  case (node, index) =>
                    val entry = YMapEntry(YNode(s"item$index"), node)
                    OasTypeParser(entry, item => item.adopted(shape.id + "/or/" + index), version).parse()
                }
                .filter(_.isDefined)
                .map(_.get)
              shape.setArray(ShapeModel.Or, unionNodes, Annotations(entry.value))
            case _ =>
              ctx.violation(shape.id, "Or constraints are built from multiple shape nodes", entry.value)

          }
        }
      )
    }
  }

  case class AndConstraintParser(map: YMap, shape: Shape) {

    def parse(): Unit = {
      adopt(shape)
      map.key(
        "allOf", { entry =>
          entry.value.to[Seq[YNode]] match {
            case Right(seq) =>
              val andNodes = seq.zipWithIndex
                .map {
                  case (node, index) =>
                    val entry = YMapEntry(YNode(s"item$index"), node)
                    OasTypeParser(entry, item => item.adopted(shape.id + "/and/" + index), version).parse()
                }
                .filter(_.isDefined)
                .map(_.get)
              shape.setArray(ShapeModel.And, andNodes, Annotations(entry.value))
            case _ =>
              ctx.violation(shape.id, "And constraints are built from multiple shape nodes", entry.value)

          }
        }
      )
    }
  }

  case class XoneConstraintParser(map: YMap, shape: Shape) {

    def parse(): Unit = {
      adopt(shape)
      map.key(
        "oneOf", { entry =>
          entry.value.to[Seq[YNode]] match {
            case Right(seq) =>
              val nodes = seq.zipWithIndex
                .map {
                  case (node, index) =>
                    val entry = YMapEntry(YNode(s"item$index"), node)
                    OasTypeParser(entry, item => item.adopted(shape.id + "/xone/" + index), version).parse()
                }
                .filter(_.isDefined)
                .map(_.get)
              shape.setArray(ShapeModel.Xone, nodes, Annotations(entry.value))
            case _ =>
              ctx.violation(shape.id, "Xone constraints are built from multiple shape nodes", entry.value)

          }
        }
      )
    }
  }

  case class NotConstraintParser(map: YMap, shape: Shape) {

    def parse(): Unit = {
      adopt(shape)
      map.key(
        "not", { entry =>
          OasTypeParser(entry, item => item.adopted(shape.id + "/not"), version).parse() match {
            case Some(negated) =>
              shape.set(ShapeModel.Not, negated)
            case _ => // ignore
          }
        }
      )
    }
  }

  case class DataArrangementParser(name: String, ast: YPart, map: YMap, adopt: Shape => Unit) {

    def lookAhead(): Option[Either[TupleShape, ArrayShape]] = {
      map.key("items") match {
        case Some(entry) =>
          entry.value.to[Seq[YNode]] match {
            // this is a sequence, we need to create a tuple
            case Right(_) => Some(Left(TupleShape(ast).withName(name)))
            // not an array regular array parsing
            case _ => Some(Right(ArrayShape(ast).withName(name)))

          }
        case None => None
      }
    }

    def parse(): AnyShape = {
      lookAhead() match {
        case None =>
          val array = ArrayShape(ast).withName(name)
          ArrayShapeParser(array, map, adopt).parse()
        case Some(Left(tuple))  => TupleShapeParser(tuple, map, adopt).parse()
        case Some(Right(array)) => ArrayShapeParser(array, map, adopt).parse()
      }
    }

  }

  case class TupleShapeParser(shape: TupleShape, map: YMap, adopt: Shape => Unit) extends AnyShapeParser() {

    override def parse(): AnyShape = {
      adopt(shape)

      super.parse()

      map.key("minItems", ArrayShapeModel.MinItems in shape)
      map.key("maxItems", ArrayShapeModel.MaxItems in shape)
      map.key("uniqueItems", ArrayShapeModel.UniqueItems in shape)
      map.key("additionalItems", TupleShapeModel.AdditionalItems in shape)
      map.key(
        "items",
        entry => {
          val items = entry.value
            .as[YSequence]
            .nodes
            .collect { case node if node.tagType == YType.Map => node }
            .zipWithIndex
            .map {
              case (elem, index) =>
                OasTypeParser(elem, s"member$index", item => item.adopted(shape.id + "/items/" + index), version)
                  .parse()
            }
          shape.withItems(items.filter(_.isDefined).map(_.get))
        }
      )

      shape
    }
  }

  case class ArrayShapeParser(shape: ArrayShape, map: YMap, adopt: Shape => Unit) extends AnyShapeParser() {
    override def parse(): AnyShape = {
      adopt(shape)

      super.parse()

      map.key("minItems", ArrayShapeModel.MinItems in shape)
      map.key("maxItems", ArrayShapeModel.MaxItems in shape)
      map.key("uniqueItems", ArrayShapeModel.UniqueItems in shape)
      map.key("collectionFormat", ArrayShapeModel.CollectionFormat in shape)

      map
        .key("items")
        .flatMap(_.value.toOption[YMap])
        .foreach(
          _.key("collectionFormat",
                (ArrayShapeModel.CollectionFormat in shape)
                  .withAnnotation(CollectionFormatFromItems())))

      val finalShape = for {
        entry <- map.key("items")
        item  <- OasTypeParser(entry, items => items.adopted(shape.id + "/items"), version).parse()
      } yield {
        item match {
          case array: ArrayShape   => shape.withItems(array).toMatrixShape
          case matrix: MatrixShape => shape.withItems(matrix).toMatrixShape
          case other: AnyShape     => shape.withItems(other)
        }
      }

      finalShape match {
        case Some(parsed: AnyShape) => parsed.withId(shape.id)
        case None                   => shape.withItems(AnyShape())
      }
    }
  }

  case class AnyTypeShapeParser(shape: AnyShape, map: YMap) extends AnyShapeParser {
    override val options: ExampleOptions = ExampleOptions(strictDefault = true, quiet = true)
  }

  abstract class AnyShapeParser() extends ShapeParser() {

    override val shape: AnyShape
    val options: ExampleOptions = ExampleOptions(strictDefault = true, quiet = false)
    override def parse(): AnyShape = {
      super.parse()
      parseExample()

      map.key("type", _ => shape.add(ExplicitField())) // todo lexical of type?? new annotation?

      shape
    }

    private def parseExample() = {
      val examples: Seq[Example] =
        RamlExamplesParser(map, "example", "examples".asOasExtension, None, shape.withExample, options).parse()
      if (examples.nonEmpty)
        shape.setArray(AnyShapeModel.Examples, examples)
    }
  }

  case class NodeShapeParser(shape: NodeShape, map: YMap) extends AnyShapeParser() {
    override def parse(): NodeShape = {

      super.parse()

      map.key("type", _ => shape.add(ExplicitField()))

      map.key("minProperties", NodeShapeModel.MinProperties in shape)
      map.key("maxProperties", NodeShapeModel.MaxProperties in shape)

      shape.set(NodeShapeModel.Closed, value = false)

      map.key("additionalProperties").foreach { entry =>
        entry.value.tagType match {
          case YType.Bool => (NodeShapeModel.Closed in shape).negated.explicit(entry)
          case YType.Map =>
            OasTypeParser(entry, s => s.adopted(shape.id)).parse().foreach { s =>
              shape.set(NodeShapeModel.AdditionalPropertiesSchema, s, Annotations(entry))
            }
          case _ => ctx.violation(shape.id, "Invalid part type for additional properties node", entry)
        }
      }

      map.key("discriminator", NodeShapeModel.Discriminator in shape)
      map.key("discriminatorValue".asOasExtension, NodeShapeModel.DiscriminatorValue in shape)

      val requiredFields = map
        .key("required")
        .map { field =>
          field.value.tagType match {
            case YType.Seq =>
              field.value.as[YSequence].nodes.foldLeft(Map[String, YNode]()) {
                case (acc, node) =>
                  acc.updated(node.as[String], node)
              }
            case _ => Map[String, YNode]()
          }
        }
        .getOrElse(Map[String, YNode]())

      map.key(
        "properties",
        entry => {
          entry.value.toOption[YMap] match {
            case Some(m) =>
              val properties: Seq[PropertyShape] =
                PropertiesParser(m, shape.withProperty, requiredFields).parse()
              shape.set(NodeShapeModel.Properties, AmfArray(properties, Annotations(entry.value)), Annotations(entry))
            case _ => // Empty properties node.
          }
        }
      )

      val properties = mutable.ListMap[String, PropertyShape]()
      shape.properties.foreach(p => properties += (p.name.value() -> p))

      map.key(
        "dependencies",
        entry => {
          val dependencies: Seq[PropertyDependencies] =
            ShapeDependenciesParser(entry.value.as[YMap], properties).parse()
          shape.set(NodeShapeModel.Dependencies, AmfArray(dependencies, Annotations(entry.value)), Annotations(entry))
        }
      )

      map.key(
        "x-amf-merge",
        entry => {
          val inherits = AllOfParser(entry.value.as[Seq[YNode]], s => s.adopted(shape.id)).parse()
          shape.set(NodeShapeModel.Inherits, AmfArray(inherits, Annotations(entry.value)), Annotations(entry))
        }
      )

      shape
    }
  }

  case class AllOfParser(array: Seq[YNode], adopt: Shape => Unit) {
    def parse(): Seq[Shape] =
      array
        .flatMap(n => {
          n.toOption[YMap]
            .flatMap(declarationsRef)
            .orElse(OasTypeParser(n, "", adopt, version).parse())
        })

    private def declarationsRef(entries: YMap): Option[Shape] = {
      entries
        .key("$ref")
        .flatMap { entry =>
          ctx.declarations.shapes.get(entry.value.as[String].stripPrefix("#/definitions/")) map { declaration =>
            declaration
              .link(entry.value.as[String], Annotations(entry.value))
              .asInstanceOf[AnyShape]
              .withName(declaration.name.option().getOrElse("schema"))
          }
        }
    }
  }

  case class PropertiesParser(map: YMap, producer: String => PropertyShape, requiredFields: Map[String, YNode]) {
    def parse(): Seq[PropertyShape] = {
      map.entries.map(entry => PropertyShapeParser(entry, producer, requiredFields).parse())
    }
  }

  case class PropertyShapeParser(entry: YMapEntry,
                                 producer: String => PropertyShape,
                                 requiredFields: Map[String, YNode]) {

    def parse(): PropertyShape = {

      val name                = entry.key.as[YScalar].text
      val required            = requiredFields.contains(name)
      val requiredAnnotations = requiredFields.get(name).map(node => Annotations(node)).getOrElse(Annotations())

      val property = producer(name)
        .add(Annotations(entry))
        .set(PropertyShapeModel.MinCount, AmfScalar(if (required) 1 else 0), requiredAnnotations += ExplicitField())

      property.set(PropertyShapeModel.Path, (Namespace.Data + entry.key.as[YScalar].text.urlComponentEncoded).iri())
      entry.value.toOption[YMap].foreach(_.key("readOnly", PropertyShapeModel.ReadOnly in property))

      if (version.isInstanceOf[OAS30SchemaVersion]) {
        entry.value.toOption[YMap].foreach(_.key("writeOnly", PropertyShapeModel.WriteOnly in property))
        entry.value.toOption[YMap].foreach(_.key("deprecated", PropertyShapeModel.Deprecated in property))
      }

      OasTypeParser(entry, shape => shape.adopted(property.id), version)
        .parse()
        .foreach(property.set(PropertyShapeModel.Range, _))

      property
    }
  }

  case class Property(var typeDef: TypeDef = UndefinedType) {
    def withTypeDef(value: TypeDef): Unit = typeDef = value
  }

  abstract class ShapeParser(implicit ctx: WebApiContext) {

    val shape: Shape
    val map: YMap

    def parse(): Shape = {

      map.key("title", ShapeModel.DisplayName in shape)
      map.key("description", ShapeModel.Description in shape)

      map.key(
        "default",
        node => {
          val str = YamlRender.render(node.value)
          shape.set(ShapeModel.DefaultValueString, AmfScalar(str), Annotations(node))
          NodeDataNodeParser(node.value, shape.id, quiet = false).parse().dataNode.foreach { dn =>
            shape.set(ShapeModel.Default, dn, Annotations(node))
          }

        }
      )

      map.key("enum", ShapeModel.Values in shape)
      map.key("externalDocs", AnyShapeModel.Documentation in shape using OasCreativeWorkParser.parse)
      map.key("xml", AnyShapeModel.XMLSerialization in shape using XMLSerializerParser.parse(shape.name.value()))

      map.key(
        "facets".asOasExtension,
        entry => {
          val properties: Seq[PropertyShape] =
            PropertiesParser(entry.value.as[YMap], shape.withCustomShapePropertyDefinition, Map()).parse()
        }
      )

      // Explicit annotation for the type property
      map.key("type", entry => shape.annotations += TypePropertyLexicalInfo(Range(entry.key.range)))

      // Logical constraints
      if (map.key("anyOf").isDefined) OrConstraintParser(map, shape).parse()
      if (map.key("allOf").isDefined) AndConstraintParser(map, shape).parse()
      if (map.key("oneOf").isDefined) XoneConstraintParser(map, shape).parse()
      if (map.key("not").isDefined) NotConstraintParser(map, shape).parse()

      // normal annotations
      AnnotationParser(shape, map).parse()

      map.key("id", node => shape.annotations += JSONSchemaId(node.value.as[YScalar].text))
      shape
    }
  }

  case class FileShapeParser(typeDef: TypeDef, shape: FileShape, map: YMap)
      extends AnyShapeParser()
      with CommonScalarParsingLogic {
    override def parse(): FileShape = {
      super.parse()

      parseScalar(map, shape)

      map.key("fileTypes".asOasExtension, FileShapeModel.FileTypes in shape)

      shape
    }
  }

  case class SchemaShapeParser(shape: SchemaShape, map: YMap) extends AnyShapeParser() with CommonScalarParsingLogic {
    super.parse()

    override def parse(): AnyShape = {
      map.key(
        "schema".asOasExtension, { entry =>
          entry.value.to[String] match {
            case Right(str) => shape.withRaw(str)
            case _ =>
              ctx.violation(shape.id, "Cannot parse non string schema shape", entry.value)
              shape.withRaw("")
          }
        }
      )

      map.key(
        "mediaType".asOasExtension, { entry =>
          entry.value.to[String] match {
            case Right(str) =>
              shape.withMediaType(str)
            case _ =>
              ctx.violation(shape.id, "Cannot parse non string schema shape", entry.value)
              shape.withMediaType("*/*")
          }
        }
      )

      shape
    }
  }

}
