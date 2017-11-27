package amf.plugins.document.graph.parser

import amf.core.metamodel.Type.{Array, Bool, Iri, RegExp, SortedArray, Str}
import amf.core.metamodel.document.BaseUnitModel.Location
import amf.core.metamodel.document._
import amf.core.metamodel.domain.{DataNodeModel, DomainElementModel, LinkableElementModel, ShapeModel}
import amf.core.metamodel.{Field, ModelDefaultBuilder, Obj, Type}
import amf.core.model.document._
import amf.core.model.domain._
import amf.core.model.domain.extensions.{CustomDomainProperty, DomainExtension}
import amf.core.parser.{Annotations, _}
import amf.core.registries.AMFDomainRegistry
import amf.core.remote.Platform
import amf.core.unsafe.TrunkPlatform
import org.yaml.convert.YRead.SeqNodeYRead
import org.yaml.model._

import scala.collection.mutable

/**
  * AMF Graph parser
  */
class GraphParser(platform: Platform)(implicit val ctx: ParserContext) extends GraphParserHelpers {

  def parse(document: YDocument, location: String): BaseUnit = {
    val parser = Parser(Map())
    parser.parse(document, location)
  }

  case class Parser(var nodes: Map[String, AmfElement]) {
    private val unresolvedReferences = mutable.Map[String, DomainElement with Linkable]()

    val dynamicGraphParser = new DynamicGraphParser(nodes)

    def parse(document: YDocument, location: String): BaseUnit = {
      val maybeMaps        = document.node.toOption[Seq[YMap]]
      val maybeMap         = maybeMaps.flatMap(s => s.headOption)
      val maybeMaybeObject = maybeMap.flatMap(parse)

      maybeMaybeObject match {
        case Some(unit: BaseUnit) => unit.set(Location, location)
        case _ =>
          ctx.violation(location, s"Unable to parse $document", document)
          Document()
      }
    }

    private def retrieveType(id: String, map: YMap): Option[Obj] = {
      val stringTypes = ts(map, ctx, id)
      stringTypes.find(findType(_).isDefined) match {
        case Some(t) => findType(t)
        case None =>
          ctx.violation(id, s"Error parsing JSON-LD node, unknown @types $stringTypes", map)
          None // todo review with pedro Obj empty?
      }
    }

    private def parseList(id: String, listElement: Type, node: YMap): Seq[AmfElement] = {
      retrieveElements(id, node).flatMap({ (n) =>
        listElement match {
          case _: Obj => parse(n.as[YMap])
          case _      => value(listElement, n).toOption[YScalar].map(s => str(s))
        }
      })
    }

    private def retrieveElements(id: String, map: YMap): Seq[YNode] = {
      map.key("@list") match {
        case Some(entry) => entry.value.as[Seq[YNode]]
        case _ =>
          ctx.violation(id, s"No @list declaration on list node $map", map)
          Nil
      }
    }

    private def parse(map: YMap): Option[AmfObject] = { // todo fix uses
      retrieveId(map, ctx)
        .flatMap(value => retrieveType(value, map).map(value2 => (value, value2)))
        .map {
          case (id, model) =>
            val sources = retrieveSources(id, map)

            val instance = buildType(model)(annotations(nodes, sources, id))
            instance.withId(id)

            // workaround for lazy values in shape
            val modelFields = model match {
              case shapeModel: ShapeModel =>
                shapeModel.fields ++ Seq(
                  ShapeModel.CustomShapePropertyDefinitions,
                  ShapeModel.CustomShapeProperties
                )
              case _ => model.fields
            }

            modelFields.foreach(f => {
              val k = f.value.iri()
              map.key(k) match {
                case Some(entry) => traverse(instance, f, value(f.`type`, entry.value), sources, k)
                case _           =>
              }
            })

            // parsing custom extensions
            instance match {
              case l: DomainElement with Linkable => parseLinkableProperties(map, l)
              case elm: DomainElement             => parseCustomProperties(map, elm)
              case _                              => // ignore
            }

            nodes = nodes + (id -> instance)
            instance
        }
    }

    private def parseLinkableProperties(map: YMap, instance: DomainElement with Linkable): Unit = {
      map
        .key(LinkableElementModel.TargetId.value.iri())
        .flatMap(entry => {
          retrieveId(entry.value.as[Seq[YMap]].head, ctx)
        })
        .foreach(unresolvedReferences += _ -> instance)

      map
        .key(LinkableElementModel.Label.value.iri())
        .flatMap(entry => {
          entry.value
            .toOption[Seq[YNode]]
            .flatMap(nodes => nodes.head.toOption[YMap])
            .flatMap(map => map.key("@value"))
            .flatMap(_.value.toOption[YScalar].map(_.text))
        })
        .foreach(s => instance.withLinkLabel(s))

      val unresolvedOption = unresolvedReferences.get(instance.id)
      unresolvedOption.foreach(u => {
        u.withLinkTarget(instance)
        unresolvedReferences.remove(instance.id)
      }) // todo remove?
    }

    private def parseCustomProperties(map: YMap, instance: DomainElement) = {
      val customProperties: Seq[String] = map.key(DomainElementModel.CustomDomainProperties.value.iri()) match {
        case Some(entry) =>
          entry.value
            .toOption[Seq[YNode]]
            .map(nodes => {
              nodes.flatMap(n => n.toOption[YMap]).flatMap(_.key("@id")).flatMap(_.value.toOption[YScalar].map(_.text))
            })
            .getOrElse(Nil)
        case _ => Seq()
      }

      val domainExtensions: Seq[DomainExtension] = customProperties
        .flatMap { propertyUri =>
          map
            .key(propertyUri)
            .map(entry => {
              val parsedNode      = dynamicGraphParser.parseDynamicType(entry.value.as[YMap])
              val domainExtension = DomainExtension()
              val domainProperty  = CustomDomainProperty()
              domainProperty.id = propertyUri
              domainExtension.withDefinedBy(domainProperty)
              parsedNode.foreach { pn =>
                domainExtension.withId(pn.id)
                domainExtension.withExtension(pn)
              }
              domainExtension
            })
        }

      if (domainExtensions.nonEmpty) {
        instance.withCustomDomainProperties(domainExtensions)
      }
    }

    private def traverse(instance: AmfObject, f: Field, node: YNode, sources: SourceMap, key: String) = {
      f.`type` match {
        case DataNodeModel => // dynamic nodes parsed here
          dynamicGraphParser.parseDynamicType(node.as[YMap]) match {
            case Some(parsed) => instance.set(f, parsed, annotations(nodes, sources, key))
            case _            =>
          }
        case _: Obj =>
          parse(node.as[YMap]).foreach(n => instance.set(f, n, annotations(nodes, sources, key)))
          instance
        case Str | RegExp | Iri => instance.set(f, str(node.as[YScalar]), annotations(nodes, sources, key))
        case Bool               => instance.set(f, bool(node.as[YScalar]), annotations(nodes, sources, key))
        case Type.Int           => instance.set(f, int(node.as[YScalar]), annotations(nodes, sources, key))
        case l: SortedArray =>
          instance.setArray(f, parseList(instance.id, l.element, node.as[YMap]), annotations(nodes, sources, key))
        case a: Array =>
          val items = node.as[Seq[YNode]]
          val values: Seq[AmfElement] = a.element match {
            case _: Obj    => items.flatMap(n => parse(n.as[YMap]))
            case Str | Iri => items.map(n => str(value(a.element, n).as[YScalar]))
          }
          a.element match {
            case _: BaseUnitModel => instance.setArrayWithoutId(f, values, annotations(nodes, sources, key))
            case _                => instance.setArray(f, values, annotations(nodes, sources, key))
          }
      }
    }
  }

  private def str(node: YScalar) = AmfScalar(node.text)

  private def bool(node: YScalar) = AmfScalar(node.text.toBoolean)

  private def int(node: YScalar) = AmfScalar(node.text.toInt)

  private val types: Map[String, Obj] = Map.empty ++ AMFDomainRegistry.metadataRegistry

  private def findType(typeString: String): Option[Obj] = {
    types.get(typeString).orElse(AMFDomainRegistry.findType(typeString))
  }

  private def buildType(modelType: Obj): (Annotations) => AmfObject = {
    AMFDomainRegistry.metadataRegistry.get(modelType.`type`.head.iri()) match {
      case Some(modelType: ModelDefaultBuilder) =>
        (annotations: Annotations) =>
          val instance = modelType.modelInstance
          instance.annotations ++= annotations
          instance
      case _ => AMFDomainRegistry.buildType(modelType) match {
        case Some(builder) => builder
        case _             => throw new Exception(s"Cannot find builder for node type $modelType")
      }
    }
  }
}

object GraphParser {
  def apply: GraphParser                     = GraphParser(TrunkPlatform(""))
  def apply(platform: Platform): GraphParser = new GraphParser(platform)(ParserContext())
}