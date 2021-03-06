package amf.plugins.document.webapi.validation.remote
import amf.ProfileName
import amf.client.plugins.ValidationMode
import amf.core.model.document.PayloadFragment
import amf.core.model.domain.Shape
import amf.core.validation.{AMFValidationResult, SeverityLevels}
import amf.plugins.features.validation.ParserSideValidations.ExampleValidationErrorSpecification

import scala.scalajs.js
import scala.scalajs.js.{Dictionary, JavaScriptException, SyntaxError}

class JsPayloadValidator(val shape: Shape, val validationMode: ValidationMode)
    extends PlatformPayloadValidator(shape) {

  override type LoadedObj    = js.Dynamic
  override type LoadedSchema = Dictionary[js.Dynamic]

  override protected def getReportProcessor(profileName: ProfileName): ValidationProcessor =
    JsReportValidationProcessor(profileName)

  override protected def loadDataNodeString(payload: PayloadFragment): Option[js.Dynamic] = {
    try {
      literalRepresentation(payload) map { payloadText =>
        loadJson(payloadText)
      }
    } catch {
      case _: ExampleUnknownException                                      => None
      case e: JavaScriptException if e.exception.isInstanceOf[SyntaxError] => throw new InvalidJsonObject(e)
    }
  }

  override protected def loadJson(str: String): js.Dynamic = {
    js.Dynamic.global.JSON.parse(str)
  }

  override protected def loadSchema(jsonSchema: CharSequence): Option[Dictionary[js.Dynamic]] = {
    var schemaNode = loadJson(jsonSchema.toString).asInstanceOf[Dictionary[js.Dynamic]]
    schemaNode -= "x-amf-fragmentType"
    schemaNode -= "example"
    schemaNode -= "examples"
    schemaNode -= "x-amf-examples"
    Some(schemaNode)
  }

  override protected def callValidator(schema: Dictionary[js.Dynamic],
                                       obj: js.Dynamic,
                                       fragment: Option[PayloadFragment],
                                       validationProcessor: ValidationProcessor): validationProcessor.Return = {

    val fast      = validationProcessor.isInstanceOf[BooleanValidationProcessor.type]
    val validator = if (fast) LazyAjv.fast else LazyAjv.default

    try {
      val correct = validator.validate(schema.asInstanceOf[js.Object], obj)

      if (fast) correct.asInstanceOf[validationProcessor.Return]
      else {
        val results: Seq[AMFValidationResult] = if (!correct) {
          validator.errors.getOrElse(js.Array[ValidationResult]()).map { result =>
            AMFValidationResult(
              message = makeValidationMessage(result),
              level = SeverityLevels.VIOLATION,
              targetNode = fragment.map(_.encodes.id).getOrElse(""),
              targetProperty = None,
              validationId = ExampleValidationErrorSpecification.id,
              position = fragment.flatMap(_.encodes.position()),
              location = fragment.flatMap(_.encodes.location()),
              source = result
            )
          }
        } else Nil

        validationProcessor.processResults(results)
      }
    } catch {
      case e: JavaScriptException =>
        validationProcessor.processException(e, fragment)
    }
  }

  private def makeValidationMessage(validationResult: ValidationResult): String = {
    var pointer = validationResult.dataPath
    if (pointer.startsWith(".")) pointer = pointer.replaceFirst("\\.", "")
    (pointer + " " + validationResult.message).trim
  }
}

case class JsReportValidationProcessor(override val profileName: ProfileName) extends ReportValidationProcessor {

  override def processException(r: Throwable, fragment: Option[PayloadFragment]): Return = {
    val results = r match {
      case e: scala.scalajs.js.JavaScriptException =>
        Seq(
          AMFValidationResult(
            message = s"Internal error during validation ${e.getMessage}",
            level = SeverityLevels.VIOLATION,
            targetNode = fragment.map(_.encodes.id).getOrElse(""),
            targetProperty = None,
            validationId = ExampleValidationErrorSpecification.id,
            position = fragment.flatMap(_.encodes.position()),
            location = fragment.flatMap(_.encodes.location()),
            source = e
          ))
      case other => processCommonException(other, fragment)
    }
    processResults(results)
  }
}
