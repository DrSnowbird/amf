package amf.framework.parser

import amf.document.BaseUnit
import amf.framework.services.RuntimeCompiler
import amf.plugins.document.vocabularies.core.DialectRegistry
import amf.remote.{Cache, Context, Platform}
import amf.validation.Validation
import org.yaml.model.YAggregate

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Reference(url: String, kind: ReferenceKind, ast: YAggregate) {

  def isRemote: Boolean = !url.startsWith("#")

  def resolve(remote: Platform,
              base: Option[Context],
              mediaType: String,
              vendor: String,
              currentValidation: Validation,
              cache: Cache,
              dialects: DialectRegistry): Future[BaseUnit] = {
    RuntimeCompiler(url, remote, mediaType, vendor, currentValidation, base, kind, cache)
      .map(root => {
//        target = root
        root
      })
  }
}
