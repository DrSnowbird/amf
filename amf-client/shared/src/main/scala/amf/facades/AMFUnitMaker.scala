package amf.facades

import amf.Core
import amf.client.render.RenderOptions
import amf.core.AMFSerializer
import amf.core.model.document.BaseUnit
import amf.core.remote._
import amf.plugins.document.graph.AMFGraphPlugin
import amf.plugins.document.vocabularies.RAMLVocabulariesPlugin
import amf.plugins.document.webapi.{OAS20Plugin, PayloadPlugin, RAML08Plugin, RAML10Plugin, _}
import amf.plugins.domain.shapes.DataShapesDomainPlugin
import amf.plugins.domain.webapi.WebAPIDomainPlugin
import amf.plugins.syntax.SYamlSyntaxPlugin
import org.yaml.model.YDocument

/**
  * AMF Unit Maker
  */
// TODO: this is only here for compatibility with the test suite
class AMFUnitMaker {

  Core.init()
  amf.core.registries.AMFPluginsRegistry.registerSyntaxPlugin(SYamlSyntaxPlugin)
  amf.core.registries.AMFPluginsRegistry.registerDocumentPlugin(RAML10Plugin)
  amf.core.registries.AMFPluginsRegistry.registerDocumentPlugin(RAML08Plugin)
  amf.core.registries.AMFPluginsRegistry.registerDocumentPlugin(OAS20Plugin)
  amf.core.registries.AMFPluginsRegistry.registerDocumentPlugin(OAS30Plugin)
  amf.core.registries.AMFPluginsRegistry.registerDocumentPlugin(PayloadPlugin)
  amf.core.registries.AMFPluginsRegistry.registerDocumentPlugin(AMFGraphPlugin)
  amf.core.registries.AMFPluginsRegistry.registerDocumentPlugin(RAMLVocabulariesPlugin)
  amf.core.registries.AMFPluginsRegistry.registerDomainPlugin(WebAPIDomainPlugin)
  amf.core.registries.AMFPluginsRegistry.registerDomainPlugin(DataShapesDomainPlugin)
  amf.core.registries.AMFPluginsRegistry.registerDocumentPlugin(JsonSchemaPlugin)

  def make(unit: BaseUnit, vendor: Vendor, options: RenderOptions): YDocument = {
    val vendorString = vendor match {
      case Amf           => "AMF Graph"
      case Payload       => "AMF Payload"
      case Raml10 | Raml => "RAML 1.0"
      case Raml08        => "RAML 0.8"
      case Oas           => "OAS 2.0"
      case Extension     => "RAML Extension"
      case Unknown       => "Unknown Vendor"
    }

    val mediaType = vendor match {
      case Amf       => "application/ld+json"
      case Payload   => "application/amf+json"
      case r: Raml   => "application/yaml"
      case Oas       => "application/json"
      case Extension => "application/yaml"
      case Unknown   => "text/plain"
    }

    new AMFSerializer(unit, mediaType, vendorString, options).make()
  }
}

object AMFUnitMaker {
  def apply(unit: BaseUnit, vendor: Vendor, options: RenderOptions): YDocument =
    new AMFUnitMaker().make(unit, vendor, options)
}
