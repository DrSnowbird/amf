package amf.client.commands

import amf.core.client.{GenerationOptions, ParserConfig}
import amf.core.model.document.BaseUnit
import amf.core.registries.AMFPluginsRegistry
import amf.core.remote._
import amf.core.services.{RuntimeCompiler, RuntimeSerializer}
import amf.plugins.document.graph.AMFGraphPlugin
import amf.plugins.document.vocabularies.RAMLVocabulariesPlugin
import amf.plugins.document.vocabularies.registries.PlatformDialectRegistry
import amf.plugins.document.webapi.{OAS20Plugin, RAML10Plugin}
import amf.plugins.domain.shapes.DataShapesDomainPlugin
import amf.plugins.domain.webapi.WebAPIDomainPlugin
import amf.plugins.syntax.SYamlSyntaxPlugin

import scala.concurrent.{ExecutionContext, Future}

trait CommandHelper {
  implicit val executionContext: ExecutionContext = ExecutionContext.Implicits.global
  val platform: Platform

  def AMFInit(): Future[Any] = {
    amf.core.AMF.init()
    AMFPluginsRegistry.registerSyntaxPlugin(SYamlSyntaxPlugin)
    AMFPluginsRegistry.registerDocumentPlugin(RAMLVocabulariesPlugin)
    AMFPluginsRegistry.registerDocumentPlugin(RAML10Plugin)
    AMFPluginsRegistry.registerDocumentPlugin(OAS20Plugin)
    AMFPluginsRegistry.registerDocumentPlugin(AMFGraphPlugin)
    AMFPluginsRegistry.registerDomainPlugin(WebAPIDomainPlugin)
    AMFPluginsRegistry.registerDomainPlugin(DataShapesDomainPlugin)
    amf.plugins.features.validation.AMFValidatorPlugin.init()
  }

  def ensureUrl(inputFile: String): String =
    if (!inputFile.startsWith("file:") && !inputFile.startsWith("http:") && !inputFile.startsWith("https:")) {
      if (inputFile.startsWith("/")) {
        s"file:/$inputFile"
      } else {
        s"file://$inputFile"
      }
    } else {
      inputFile
    }

  protected def processDialects(config: ParserConfig): Future[Unit] = {
    val dialectFutures = config.dialects.map { dialect =>
      PlatformDialectRegistry.registerDialect(dialect)
    }
    Future.sequence(dialectFutures).map[Unit] { _ => }
  }

  protected def parseInput(config: ParserConfig): Future[BaseUnit] = {
    var inputFile   = ensureUrl(config.input.get)
    val inputFormat = config.inputFormat.get
    RuntimeCompiler(
      inputFile,
      platform,
      effectiveMediaType(config.inputMediaType, config.inputFormat),
      effectiveVendor(config.inputMediaType, config.inputFormat)
    )
  }

  protected def generateOutput(config: ParserConfig, unit: BaseUnit): Future[Unit] = {
    val generateOptions = GenerationOptions()
    if (config.withSourceMaps) {
      generateOptions.withSourceMaps
    }
    config.output match {
      case Some(f) =>
        RuntimeSerializer.dumpToFile(
          platform,
          f,
          unit,
          effectiveMediaType(config.inputMediaType, config.inputFormat),
          effectiveVendor(config.inputMediaType, config.inputFormat),
          generateOptions
        )
      case None    => Future {
        config.stdout.print(
          RuntimeSerializer(
            unit,
            effectiveMediaType(config.outputMediaType, config.inputFormat),
            effectiveVendor(config.outputMediaType, config.inputFormat),
            generateOptions
          )
        )
      }
    }
  }

  def effectiveMediaType(mediaType: Option[String], vendor: Option[String]) = {
    mediaType match {
      case Some(effectiveMediaType) => effectiveMediaType
      case None => vendor match {
        case Some(effectiveVendor) if AMFPluginsRegistry.documentPluginForID(effectiveVendor).isDefined =>
          AMFPluginsRegistry.documentPluginForID(effectiveVendor).get.documentSyntaxes.head
        case _ => "*/*"
      }
    }
  }


  def effectiveVendor(mediaType: Option[String], vendor: Option[String]): String = {
    vendor match {
      case Some(effectiveVendor) => effectiveVendor
      case None => "Unknown"
    }
  }

}