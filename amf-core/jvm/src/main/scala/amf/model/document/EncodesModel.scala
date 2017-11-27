package amf.model.document

import amf.core.model.document
import amf.core.unsafe.PlatformSecrets
import amf.model.domain.DomainElement

trait EncodesModel extends PlatformSecrets {

  /** Encoded [[DomainElement]] described in the document element. */
  private[amf] val element: document.EncodesModel

  /** Encoded [[DomainElement]] described in the document element. */
  lazy val encodes: DomainElement = platform.wrap(element.encodes)
}