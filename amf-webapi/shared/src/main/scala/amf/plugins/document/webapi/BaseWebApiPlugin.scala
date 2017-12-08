package amf.plugins.document.webapi

import amf.core.model.document.BaseUnit
import amf.core.model.domain.{DataNode, Shape}
import amf.core.plugins.{AMFDocumentPlugin, AMFPlugin, AMFValidationPlugin}
import amf.core.remote.Platform
import amf.core.unsafe.PlatformSecrets
import amf.core.validation.core.ValidationProfile
import amf.core.validation.{AMFValidationReport, EffectiveValidations}
import amf.plugins.document.webapi.annotations.{DeclaredElement, ParsedJSONSchema}
import amf.plugins.document.webapi.metamodel.FragmentsTypesModels._
import amf.plugins.document.webapi.metamodel.{ExtensionModel, OverlayModel}
import amf.plugins.document.webapi.references.WebApiReferenceCollector
import amf.plugins.document.webapi.validation.{PayloadValidation, WebApiValidations}
import amf.plugins.domain.shapes.DataShapesDomainPlugin
import amf.plugins.domain.webapi.WebAPIDomainPlugin

import scala.concurrent.Future

trait BaseWebApiPlugin extends AMFDocumentPlugin with AMFValidationPlugin with WebApiValidations with PlatformSecrets {

  override def referenceCollector() = new WebApiReferenceCollector(ID)

  override def dependencies() = Seq(WebAPIDomainPlugin, DataShapesDomainPlugin)

  override def modelEntities = Seq(
    ExtensionModel,
    OverlayModel,
    DocumentationItemFragmentModel,
    DataTypeFragmentModel,
    NamedExampleFragmentModel,
    ResourceTypeFragmentModel,
    TraitFragmentModel,
    AnnotationTypeDeclarationFragmentModel,
    SecuritySchemeFragmentModel
  )

  override def serializableAnnotations() = Map(
    "parsed-json-schema" -> ParsedJSONSchema,
    "declared-element"   -> DeclaredElement
  )

  val validationProfile: String

  /**
    * Validation profiles supported by this plugin by default
    */
  override def domainValidationProfiles(platform: Platform): Map[String, () => ValidationProfile] =
    defaultValidationProfiles

  def validationRequest(baseUnit: BaseUnit,
                        profile: String,
                        validations: EffectiveValidations,
                        platform: Platform): Future[AMFValidationReport] = {
    validationRequestsForBaseUnit(baseUnit, profile, validations, validationProfile, platform)
  }

  override def init(): Future[AMFPlugin] = Future.successful(this)

  def validatePayload(shape: Shape, payload: DataNode): Future[AMFValidationReport] = new PayloadValidation(platform, shape).validate(payload)
}
