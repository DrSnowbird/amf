package amf.model.domain

import amf.plugins.domain.webapi.models

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

/**
  * JS Response model class.
  */
@JSExportAll
case class Response private[model] (private val response: models.Response) extends DomainElement {

  @JSExportTopLevel("model.domain.Response")
  def this() = this(models.Response())

  def name: String                    = response.name
  def description: String             = response.description
  def statusCode: String              = response.statusCode
  def headers: js.Iterable[Parameter] = Option(response.headers).getOrElse(Nil).map(Parameter).toJSArray
  def payloads: js.Iterable[Payload]  = Option(response.payloads).getOrElse(Nil).map(Payload).toJSArray
  def examples: js.Iterable[Example]  = Option(response.examples).getOrElse(Nil).map(Example).toJSArray

  override private[amf] def element: models.Response = response

  /** Set name property of this [[Response]]. */
  def withName(name: String): this.type = {
    response.withName(name)
    this
  }

  /** Set description property of this [[Response]]. */
  def withDescription(description: String): this.type = {
    response.withDescription(description)
    this
  }

  /** Set statusCode property of this [[Response]]. */
  def withStatusCode(statusCode: String): this.type = {
    response.withStatusCode(statusCode)
    this
  }

  /** Set headers property of this [[Response]]. */
  def withHeaders(headers: js.Iterable[Parameter]): this.type = {
    response.withHeaders(headers.toSeq.map(_.element))
    this
  }

  /** Set payloads property of this [[Response]]. */
  def withPayloads(payloads: js.Iterable[Payload]): this.type = {
    response.withPayloads(payloads.toSeq.map(_.element))
    this
  }

  /** Set examples property of this [[Response]]. */
  def withExamples(examples: js.Iterable[Example]): this.type = {
    response.withExamples(examples.toSeq.map(_.element))
    this
  }

  /**
    * Adds one [[Parameter]] to the headers property of this [[Response]] and returns it for population.
    * Name property of the parameter is required.
    */
  def withHeader(name: String): Parameter = Parameter(response.withHeader(name))

  /** Adds one [[Payload]] to the payloads property of this [[Response]] and returns it for population. */
  def withPayload(): Payload = Payload(response.withPayload())

  /** Adds one [[Payload]] to the payloads property of this [[Response]] with the given media type and returns it for population. */
  def withPayload(mediaType: String): Payload = Payload(response.withPayload(Some(mediaType)))
}
