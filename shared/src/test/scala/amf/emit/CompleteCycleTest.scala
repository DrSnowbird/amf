package amf.emit

import amf.common.Tests.checkDiff
import amf.compiler.AMFCompiler
import amf.dumper.AMFDumper
import amf.io.TmpTests
import amf.remote._
import org.scalatest.{Assertion, AsyncFunSuite}

import scala.concurrent.{ExecutionContext, Future}

class CompleteCycleTest extends AsyncFunSuite with TmpTests {

  override implicit val executionContext: ExecutionContext = ExecutionContext.Implicits.global

  val basePath = "file://shared/src/test/resources/upanddown/"

  test("Full raml to raml test") {
    assertCycle("full-example.raml", "full-example.raml", RamlYamlHint, Raml)
  }

  test("Full oas to oas test") {
    assertCycle("full-example.json", "full-example.json", OasJsonHint, Oas)
  }

  test("Full raml to oas test") {
    assertCycle("full-example.raml", "full-example.json", RamlYamlHint, Oas)
  }

  test("Full oas to raml test") {
    assertCycle("full-example.json", "full-example.json.raml", OasJsonHint, Raml)
  }

  test("Full raml to amf test") {
    assertCycle("full-example.raml", "full-example.raml.jsonld", RamlYamlHint, Amf)
  }

  test("Full oas to amf test") {
    assertCycle("full-example.json", "full-example.json.jsonld", OasJsonHint, Amf)
  }

//  test("Full cycle raml to amf test") {
//    cycle("full-example.raml", RamlYamlHint, Amf)
//  }

  test("Basic cycle for amf") {
    cycle("basic.jsonld", AmfJsonHint, Amf)
  }

  test("Basic cycle for raml") {
    cycle("basic.raml", RamlYamlHint, Raml)
  }

  test("Basic cycle for oas") {
    cycle("basic.json", OasJsonHint, Oas)
  }

  test("Basic raml to amf test") {
    assertCycle("basic.raml", "basic.raml.jsonld", RamlYamlHint, Amf)
  }

  test("Basic oas to amf test") {
    assertCycle("basic.json", "basic.json.jsonld", OasJsonHint, Amf)
  }

  test("Basic amf(raml) to raml test") {
    assertCycle("basic.raml.jsonld", "basic.raml", AmfJsonHint, Raml)
  }

  test("Basic amf(oas) to oas test") {
    assertCycle("basic.json.jsonld", "basic.json", AmfJsonHint, Oas)
  }

  test("Basic raml to oas test") {
    assertCycle("basic.raml", "basic.raml.json", RamlYamlHint, Oas)
  }

  test("Basic oas to raml test") {
    assertCycle("basic.json", "basic.json.raml", OasJsonHint, Raml)
  }

  test("Complete amf to amf test") {
    assertCycle("complete.jsonld", "complete.jsonld", AmfJsonHint, Amf)
  }

  test("Complete raml to amf test") {
    assertCycle("complete.raml", "complete.raml.jsonld", RamlYamlHint, Amf)
  }

  test("Complete raml to oas test") {
    assertCycle("complete.raml", "complete.json", RamlYamlHint, Oas)
  }

  test("Complete oas to amf test") {
    assertCycle("complete.json", "complete.json.jsonld", OasJsonHint, Amf)
  }

  test("Complete oas to raml test") {
    assertCycle("complete.json", "complete.raml", OasJsonHint, Raml)
  }

  test("Complete raml to raml test") {
    assertCycle("complete.raml", "complete.raml", RamlYamlHint, Raml)
  }

  test("Complete oas to oas test") {
    assertCycle("complete.json", "complete.json", OasJsonHint, Oas)
  }

  test("Complete amf(raml) to raml test") {
    assertCycle("complete.raml.jsonld", "complete.raml", AmfJsonHint, Raml)
  }

  test("Complete amf(oas) to oas test") {
    assertCycle("complete.json.jsonld", "complete.json", AmfJsonHint, Oas)
  }

  test("Endpoints amf to amf test") {
    assertCycle("endpoints.jsonld", "endpoints.jsonld", AmfJsonHint, Amf)
  }

  test("Endpoints raml to amf test") {
    assertCycle("endpoints.raml", "endpoints.raml.jsonld", RamlYamlHint, Amf)
  }

  test("Endpoints raml to oas test") {
    assertCycle("endpoints.raml", "endpoints.json", RamlYamlHint, Oas)
  }

  test("Endpoints raml to raml test") {
    assertCycle("endpoints.raml", "endpoints.raml", RamlYamlHint, Raml)
  }

  test("Endpoints oas to raml test") {
    assertCycle("endpoints.json", "endpoints.json.raml", OasJsonHint, Raml)
  }

  test("Endpoints oas to amf test") {
    assertCycle("endpoints.json", "endpoints.json.jsonld", OasJsonHint, Amf)
  }

  test("Endpoints oas to oas test") {
    assertCycle("endpoints.json", "endpoints.json", OasJsonHint, Oas)
  }

  test("Endpoints amf(raml) to raml test") {
    assertCycle("endpoints.raml.jsonld", "endpoints.raml", AmfJsonHint, Raml)
  }

  test("Endpoints amf(oas) to oas test") {
    assertCycle("endpoints.json.jsonld", "endpoints.json", AmfJsonHint, Oas)
  }

  test("Complete with operations raml to oas test") {
    assertCycle("complete-with-operations.raml", "complete-with-operations.json", RamlYamlHint, Oas)
  }

  test("Complete with operations raml to raml test") {
    assertCycle("complete-with-operations.raml", "complete-with-operations.raml", RamlYamlHint, Raml)
  }

  test("Complete with operations oas to raml test") {
    assertCycle("complete-with-operations.json", "complete-with-operations.json.raml", OasJsonHint, Raml)
  }

  test("Complete with operations oas to oas test") {
    assertCycle("complete-with-operations.json", "complete-with-operations.json", OasJsonHint, Oas)
  }

  test("Complete with request raml to raml test") {
    assertCycle("operation-request.raml", "operation-request.raml", RamlYamlHint, Raml)
  }

  test("Complete with request oas to raml test") {
    assertCycle("operation-request.json", "operation-request.json.raml", OasJsonHint, Raml)
  }

  test("Complete with request raml to oas test") {
    assertCycle("operation-request.raml", "operation-request.raml.json", RamlYamlHint, Oas)
  }

  test("Complete with response oas to raml test") {
    assertCycle("operation-response.json", "operation-response.raml", OasJsonHint, Raml)
  }

  test("Complete with response oas to oas test") {
    assertCycle("operation-response.json", "operation-response.json", OasJsonHint, Oas)
  }

  test("Complete with response raml to raml test") {
    assertCycle("operation-response.raml", "operation-response.raml", RamlYamlHint, Raml)
  }

  test("Complete with response raml to oas test") {
    assertCycle("operation-response.raml", "operation-response.raml.json", RamlYamlHint, Oas)
  }

  test("Complete with payloads raml to raml test") {
    assertCycle("payloads.raml", "payloads.raml", RamlYamlHint, Raml)
  }

  test("Complete with payloads raml to oas test") {
    assertCycle("payloads.raml", "payloads.raml.json", RamlYamlHint, Oas)
  }

  test("Complete with payloads oas to oas test") {
    assertCycle("payloads.json", "payloads.json", OasJsonHint, Oas)
  }

  test("Complete with payloads oas to raml test") {
    assertCycle("payloads.json", "payloads.json.raml", OasJsonHint, Raml)
  }

  def assertCycle(source: String, golden: String, hint: Hint, target: Vendor): Future[Assertion] = {
    val expected = platform
      .resolve(basePath + golden, None)
      .map(_.stream.toString)

    val actual = AMFCompiler(basePath + source, platform, hint)
      .build()
      .flatMap(unit => new AMFDumper(unit, target).dumpToString)

    actual
      .zip(expected)
      .map(checkDiff)
  }

  def cycle(source: String, hint: Hint, target: Vendor): Future[Assertion] = {
    AMFCompiler(basePath + source, platform, hint)
      .build()
      .flatMap(new AMFDumper(_, target).dumpToString)
      .flatMap(content => {
        val file = tmp(source + ".tmp")
        platform.write("file://" + file, content).map((_, content))
      })
      .flatMap({
        case (path, actual) =>
          platform
            .resolve(basePath + source, None)
            .map(expected => checkDiff(actual, path, expected.stream.toString, expected.url))
      })
  }
}
