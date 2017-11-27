package amf.model.domain

import amf.plugins.domain.webapi.models

/**
  * JS Organization model class.
  */
case class Organization private[model] (private val organization: models.Organization) extends DomainElement {

  def this() = this(models.Organization())

  val url: String   = organization.url
  val name: String  = organization.name
  val email: String = organization.email

  override private[amf] def element: models.Organization = organization

  /** Set url property of this [[Organization]]. */
  def withUrl(url: String): this.type = {
    organization.withUrl(url)
    this
  }

  /** Set name property of this [[Organization]]. */
  def withName(name: String): this.type = {
    organization.withName(name)
    this
  }

  /** Set email property of this [[Organization]]. */
  def withEmail(email: String): this.type = {
    organization.withEmail(email)
    this
  }
}