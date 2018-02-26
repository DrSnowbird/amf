package amf.plugins.document.vocabularies.spec

import amf.core.annotations.Aliases
import amf.core.model.document.{BaseUnit, DeclaresModel, Document, Fragment}
import amf.core.parser.{ParsedReference, _}
import amf.plugins.document.vocabularies.model.domain.DomainEntity
import org.yaml.model.YMap

import scala.collection.mutable

object ReferenceDeclarations {
  def apply(references: mutable.Map[String, BaseUnit])(implicit ctx: DialectContext) =
    new ReferenceDeclarations(references)

  def apply()(implicit ctx: DialectContext): ReferenceDeclarations = apply(mutable.Map[String, BaseUnit]())
}

case class ReferenceDeclarations(references: mutable.Map[String, BaseUnit] = mutable.Map())(
    implicit ctx: DialectContext) {

  def +=(alias: String, unit: BaseUnit): Unit = {
    references += (alias -> unit)
    val library = ctx.declarations.getOrCreateLibrary(alias)
    // todo : ignore domain entities of vocabularies?
    unit match {
      case d: DeclaresModel =>
        d.declares
          .filter({
            case _: DomainEntity => false
            case _               => true
          })
          .foreach(library += _)
    }
  }

  def +=(url: String, fragment: Fragment): Unit = {
    references += (url       -> fragment)
    ctx.declarations += (url -> fragment)
  }

  def +=(url: String, fragment: Document): Unit = references += (url -> fragment)

  def solvedReferences(): Seq[BaseUnit] = references.values.toSet.toSeq
}

case class ReferencesParser(key: String, map: YMap, references: Seq[ParsedReference])(implicit ctx: DialectContext) {
  def parse(location: String): ReferenceDeclarations = {
    val result: ReferenceDeclarations = parseLibraries(location)

    references.foreach {
      case ParsedReference(f: Fragment, origin: Reference, _) => result += (origin.url, f)
      case ParsedReference(d: Document, origin: Reference, _) => result += (origin.url, d)
      case _                                                  =>
    }

    result
  }

  private def target(url: String): Option[BaseUnit] =
    references.find(r => r.origin.url.equals(url)).map(_.unit)

  private def parseLibraries(id: String): ReferenceDeclarations = {
    val result = ReferenceDeclarations()

    map.key(
      key,
      entry =>
        entry.value
          .as[YMap]
          .entries
          .foreach(e => {
            val alias: String = e.key
            val url: String   = e.value
            target(url).foreach {
              case module: DeclaresModel => result += (alias, collectAlias(module, alias -> url))
              case other =>
                ctx
                  .violation(id, s"Expected module but found: $other", e) // todo Uses should only reference modules...
            }
          })
    )

    result
  }

  private def collectAlias(module: BaseUnit, alias: (String, String)): BaseUnit = {
    module.annotations.find(classOf[Aliases]) match {
      case Some(aliases) =>
        module.annotations.reject(_.isInstanceOf[Aliases])
        module.add(aliases.copy(aliases = aliases.aliases + alias))
      case None => module.add(Aliases(Set(alias)))
    }
  }

}
