package amf.plugins.domain.webapi.metamodel

import amf.core.metamodel.Field
import amf.core.metamodel.Type.{Iri, Str}
import amf.core.metamodel.domain.{DomainElementModel, ExternalModelVocabularies, ModelDoc, ModelVocabularies}
import amf.core.metamodel.domain.common.NameFieldSchema
import amf.plugins.domain.webapi.models.Organization
import amf.core.vocabulary.Namespace.Schema
import amf.core.vocabulary.ValueType

/**
  * Organization metamodel
  */
object OrganizationModel extends DomainElementModel with NameFieldSchema {

  val Url = Field(Iri,
                  Schema + "url",
                  ModelDoc(ExternalModelVocabularies.SchemaOrg, "url", "URL identifying the organization"))

  val Email = Field(Str,
                    Schema + "email",
                    ModelDoc(ExternalModelVocabularies.SchemaOrg, "email", "Contact email for the organization"))

  override val `type`: List[ValueType] = Schema + "Organization" :: DomainElementModel.`type`

  override def fields: List[Field] = List(Url, Name, Email) ++ DomainElementModel.fields

  override def modelInstance = Organization()

  override val doc: ModelDoc = ModelDoc(ModelVocabularies.Http, "Organization", "Organization providing an API")
}
