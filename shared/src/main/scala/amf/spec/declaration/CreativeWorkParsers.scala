package amf.spec.declaration

import amf.domain.CreativeWork
import amf.framework.parser.Annotations
import amf.metadata.domain.CreativeWorkModel
import amf.parser.YMapOps
import amf.spec.ParserContext
import amf.spec.common.{AnnotationParser, ValueNode}
import org.yaml.model.YMap

/**
  *
  */
case class OasCreativeWorkParser(map: YMap)(
  implicit val ctx: ParserContext) {
  def parse(): CreativeWork = {

    val creativeWork = CreativeWork(map)

    map.key("url", entry => {
      val value = ValueNode(entry.value)
      creativeWork.set(CreativeWorkModel.Url, value.string(), Annotations(entry))
    })

    map.key("description", entry => {
      val value = ValueNode(entry.value)
      creativeWork.set(CreativeWorkModel.Description, value.string(), Annotations(entry))
    })

    map.key("x-title", entry => {
      val value = ValueNode(entry.value)
      creativeWork.set(CreativeWorkModel.Title, value.string(), Annotations(entry))
    })

    AnnotationParser(() => creativeWork, map).parse()

    creativeWork
  }
}

case class RamlCreativeWorkParser(map: YMap, withExtention: Boolean)(
  implicit val ctx: ParserContext) {
  def parse(): CreativeWork = {

    val documentation = CreativeWork(Annotations(map))

    map.key("title", entry => {
      val value = ValueNode(entry.value)
      documentation.set(CreativeWorkModel.Title, value.string(), Annotations(entry))
    })

    map.key("content", entry => {
      val value = ValueNode(entry.value)
      documentation.set(CreativeWorkModel.Description, value.string(), Annotations(entry))
    })

    if (withExtention)
      map.key("(url)", entry => {
        val value = ValueNode(entry.value)
        documentation.set(CreativeWorkModel.Url, value.string(), Annotations(entry))
      })
    else
      map.key("url", entry => {
        val value = ValueNode(entry.value)
        documentation.set(CreativeWorkModel.Url, value.string(), Annotations(entry))
      })
    documentation
  }
}
