package amf.model.domain

import amf.plugins.domain.webapi.models

import scala.collection.JavaConverters._

/**
  * JS WebApi model class.
  */
case class WebApi(private val webApi: models.WebApi) extends DomainElement {

  def this() = this(models.WebApi())

  val name: String                                      = webApi.name
  val description: String                               = webApi.description
  val host: String                                      = webApi.host
  val schemes: java.util.List[String]                      = webApi.schemes.asJava
  val endPoints: java.util.List[EndPoint]                  = webApi.endPoints.map(amf.model.domain.EndPoint).asJava
  val basePath: String                                  = webApi.basePath
  val accepts: java.util.List[String]                      = webApi.accepts.asJava
  val contentType: java.util.List[String]                  = webApi.contentType.asJava
  val version: String                                   = webApi.version
  val termsOfService: String                            = webApi.termsOfService
  val provider: Organization                            = Option(webApi.provider).map(amf.model.domain.Organization).orNull
  val license: License                                  = Option(webApi.license).map(amf.model.domain.License).orNull
  val documentations: java.util.List[CreativeWork]         = webApi.documentations.map(CreativeWork).asJava
  val baseUriParameters: java.util.List[Parameter]         = webApi.baseUriParameters.map(amf.model.domain.Parameter).asJava
  def security: java.util.List[ParametrizedSecurityScheme] = webApi.security.map(ParametrizedSecurityScheme).asJava

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
  def withSchemes(schemes: java.util.List[String]): this.type = {
    webApi.withSchemes(schemes.asScala)
    this
  }

  /** Set endPoints property of this [[WebApi]]. */
  def withEndPoints(endPoints: java.util.List[EndPoint]): this.type = {
    webApi.withEndPoints(endPoints.asScala.map(_.element))
    this
  }

  /** Set basePath property of this [[WebApi]]. */
  def withBasePath(path: String): this.type = {
    webApi.withBasePath(path)
    this
  }

  /** Set accepts property of this [[WebApi]]. */
  def withAccepts(accepts: java.util.List[String]): this.type = {
    webApi.withAccepts(accepts.asScala)
    this
  }

  /** Set contentType property of this [[WebApi]]. */
  def withContentType(contentType: java.util.List[String]): this.type = {
    webApi.withContentType(contentType.asScala)
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
  def withDocumentation(documentations: java.util.List[CreativeWork]): this.type = {
    webApi.withDocumentations(documentations.asScala.map(_.element))
    this
  }

  /** Set security property of this [[WebApi]] using a list of [[ParametrizedSecurityScheme]]. */
  def withSecurity(security: java.util.List[ParametrizedSecurityScheme]): this.type = {
    webApi.withSecurity(security.asScala.map(_.element))
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
  def withBaseUriParameters(parameters: java.util.List[Parameter]): this.type = {
    webApi.withBaseUriParameters(parameters.asScala.map(_.element))
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