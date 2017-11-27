package amf.resolution

import amf.core.remote.{Amf, Oas, OasJsonHint, RamlYamlHint}
import amf.remote._

class ParameterResolutionTest extends ResolutionTest {

  override val basePath = "amf-tests/shared/src/test/resources/resolution/"

  test("resolution AMF") {
    cycle("parameters.raml", "parameters.raml.jsonld", RamlYamlHint, Amf)
  }

  test("resolution OpenAPI") {
    cycle("parameters.json", "parameters.json.jsonld", OasJsonHint, Oas)
  }
}