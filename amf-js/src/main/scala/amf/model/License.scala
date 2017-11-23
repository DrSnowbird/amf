package amf.model

import amf.plugins.domain.webapi.models

import scala.scalajs.js.annotation.JSExportAll

/**
  * JS License model class.
  */
@JSExportAll
case class License private[model] (private val license: models.License) extends DomainElement {

  def this() = this(models.License())

  val url: String  = license.url
  val name: String = license.name

  override private[amf] def element: models.License = license

  /** Set url property of this [[License]]. */
  def withUrl(url: String): this.type = {
    license.withUrl(url)
    this
  }

  /** Set name property of this [[License]]. */
  def withName(name: String): this.type = {
    license.withName(name)
    this
  }
}
