package amf.plugins.domain.webapi.models

import amf.client.model.StrField
import amf.core.model.domain.DomainElement
import amf.core.parser.{Annotations, Fields}
import amf.plugins.domain.webapi.metamodel.CallbackModel
import amf.plugins.domain.webapi.metamodel.CallbackModel._
import org.yaml.model.YMap

/**
  * Callback internal model
  */
case class Callback(fields: Fields, annotations: Annotations) extends DomainElement {

  def name: StrField       = fields.field(Name)
  def expression: StrField = fields.field(Expression)
  def endpoint: EndPoint   = fields.field(Endpoint)

  def withName(name: String): this.type             = set(Name, name)
  def withExpression(expression: String): this.type = set(Expression, expression)
  def withEndpoint(endpoint: EndPoint): this.type   = set(Endpoint, endpoint)

  def withEndpoint(): EndPoint = {
    val result = EndPoint()
    set(Endpoint, result)
    result
  }

  override def adopted(parent: String): this.type = withId(parent + "/" + name)

  override def meta = CallbackModel
}

object Callback {

  def apply(): Callback = apply(Annotations())

  def apply(ast: YMap): Callback = apply(Annotations(ast))

  def apply(annotations: Annotations): Callback = new Callback(Fields(), annotations)
}