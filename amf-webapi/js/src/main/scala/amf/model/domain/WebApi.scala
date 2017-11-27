package amf.model.domain

import amf.plugins.domain.webapi.models

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

/**
  * JS WebApi model class.
  */
@JSExportAll
case class WebApi(private val webApi: models.WebApi) extends DomainElement {

  @JSExportTopLevel("WebApi")
  def this() = this(models.WebApi())

  val name: String                                      = webApi.name
  val description: String                               = webApi.description
  val host: String                                      = webApi.host
  val schemes: js.Iterable[String]                      = webApi.schemes.toJSArray
  val endPoints: js.Iterable[EndPoint]                  = webApi.endPoints.map(amf.model.domain.EndPoint).toJSArray
  val basePath: String                                  = webApi.basePath
  val accepts: js.Iterable[String]                      = webApi.accepts.toJSArray
  val contentType: js.Iterable[String]                  = webApi.contentType.toJSArray
  val version: String                                   = webApi.version
  val termsOfService: String                            = webApi.termsOfService
  val provider: Organization                            = Option(webApi.provider).map(amf.model.domain.Organization).orNull
  val license: License                                  = Option(webApi.license).map(amf.model.domain.License).orNull
  val documentations: js.Iterable[CreativeWork]         = webApi.documentations.map(CreativeWork).toJSArray
  val baseUriParameters: js.Iterable[Parameter]         = webApi.baseUriParameters.map(amf.model.domain.Parameter).toJSArray
  def security: js.Iterable[ParametrizedSecurityScheme] = webApi.security.map(ParametrizedSecurityScheme).toJSArray

  override private[amf] def element: models.WebApi = webApi

  /** Set name property of this [[WebApi]]. */
  def withName(name: String): this.type = {
    webApi.withName(name)
    this
  }

  /** Set description property of this [[WebApi]]. */
  def withDescription(description: String): this.type = {
    webApi.withDescription(description)
    this
  }

  /** Set host property of this [[WebApi]]. */
  def withHost(host: String): this.type = {
    webApi.withHost(host)
    this
  }

  /** Set schemes property of this [[WebApi]]. */
  def withSchemes(schemes: js.Iterable[String]): this.type = {
    webApi.withSchemes(schemes.toSeq)
    this
  }

  /** Set endPoints property of this [[WebApi]]. */
  def withEndPoints(endPoints: js.Iterable[EndPoint]): this.type = {
    webApi.withEndPoints(endPoints.toSeq.map(_.element))
    this
  }

  /** Set basePath property of this [[WebApi]]. */
  def withBasePath(path: String): this.type = {
    webApi.withBasePath(path)
    this
  }

  /** Set accepts property of this [[WebApi]]. */
  def withAccepts(accepts: js.Iterable[String]): this.type = {
    webApi.withAccepts(accepts.toSeq)
    this
  }

  /** Set contentType property of this [[WebApi]]. */
  def withContentType(contentType: js.Iterable[String]): this.type = {
    webApi.withContentType(contentType.toSeq)
    this
  }

  /** Set version property of this [[WebApi]]. */
  def withVersion(version: String): this.type = {
    webApi.withVersion(version)
    this
  }

  /** Set termsOfService property of this [[WebApi]]. */
  def withTermsOfService(terms: String): this.type = {
    webApi.withTermsOfService(terms)
    this
  }

  /** Set provider property of this [[WebApi]] using a [[Organization]]. */
  def withProvider(provider: Organization): this.type = {
    webApi.withProvider(provider.element)
    this
  }

  /** Set license property of this [[WebApi]] using a [[License]]. */
  def withLicense(license: License): this.type = {
    webApi.withLicense(license.element)
    this
  }

  /** Set documentation property of this [[WebApi]] using a [[CreativeWork]]. */
  def withDocumentation(documentations: js.Iterable[CreativeWork]): this.type = {
    webApi.withDocumentations(documentations.toSeq.map(_.element))
    this
  }

  /** Set security property of this [[WebApi]] using a list of [[ParametrizedSecurityScheme]]. */
  def withSecurity(security: js.Iterable[ParametrizedSecurityScheme]): this.type = {
    webApi.withSecurity(security.toSeq.map(_.element))
    this
  }

  /**
    * Adds one [[CreativeWork]] to the documentations property of this [[WebApi]] and returns it for population.
    * Path property of the CreativeWork is required.
    */
  def withDocumentationTitle(title: String): CreativeWork = CreativeWork(webApi.withDocumentationTitle(title))

  /**
    * Adds one [[CreativeWork]] to the documentations property of this [[WebApi]] and returns it for population.
    * Path property of the CreativeWork is required.
    */
  def withDocumentationUrl(url: String): CreativeWork = CreativeWork(webApi.withDocumentationUrl(url))

  /** Set baseUriParameters property of this [[WebApi]]. */
  def withBaseUriParameters(parameters: js.Iterable[Parameter]): this.type = {
    webApi.withBaseUriParameters(parameters.toSeq.map(_.element))
    this
  }

  /**
    * Adds one [[EndPoint]] to the endPoints property of this [[WebApi]] and returns it for population.
    * Path property of the endPoint is required.
    */
  def withEndPoint(path: String): EndPoint = EndPoint(webApi.withEndPoint(path))

  /**
    * Adds one [[Parameter]] to the baseUriParameters property of this [[WebApi]] and returns it for population.
    * Name property of the parameter is required.
    */
  def withBaseUriParameter(name: String): Parameter = Parameter(webApi.withBaseUriParameter(name))
}