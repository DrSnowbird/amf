Model: file://amf-client/shared/src/test/resources/validations/08/pattern.raml
Profile: RAML08
Conforms? false
Number of results: 1

Level: Violation

- Source: http://a.ml/vocabularies/amf/parser#exampleError
  Message: {"keyword":"pattern","dataPath":"","schemaPath":"#/pattern","params":{"pattern":"^[^0-9]*$"},"message":"should match pattern \"^[^0-9]*$\""}
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/08/pattern.raml#/web-api/end-points/%2Fresources/get/request/parameter/param/scalar/param/example/default-example
  Property: 
  Position: Some(LexicalInformation([(11,21)-(11,24)]))
  Location: 
