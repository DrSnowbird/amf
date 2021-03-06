package amf.client.model.domain

import amf.client.model.StrField

/**
  * All DomainElements supporting name
  */
trait NamedDomainElement {

  /** Return [[DomainElement]] name. */
  def name: StrField

  /** Update [[DomainElement]] name. */
  def withName(name: String): this.type

}
