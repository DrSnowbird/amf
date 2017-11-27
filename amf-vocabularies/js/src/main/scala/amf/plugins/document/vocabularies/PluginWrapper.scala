package amf.plugins.document.vocabularies

import amf.core.metamodel.Obj
import amf.core.unsafe.PlatformSecrets
import amf.model.{DialectFragment, DomainEntity}
import amf.plugins.document.vocabularies.metamodel.document.DialectNodeFragmentModel
import amf.plugins.document.vocabularies.metamodel.domain.DialectEntityModel
import amf.plugins.document.vocabularies.model.domain
import amf.plugins.document.vocabularies.model.document

object PluginWrapper extends PlatformSecrets{

  def init() = {
    val p: (Obj) => Boolean = (x: Obj) => x.isInstanceOf[DialectEntityModel]
    platform.registerWrapperPredicate(p) {
      case m: domain.DomainEntity => DomainEntity(m)
    }
    platform.registerWrapper(DialectNodeFragmentModel) {
      case d: document.DialectFragment => new DialectFragment(d)
    }
  }

}