Model: file://amf-client/shared/src/test/resources/validations/jsonschema/ref/api2.raml
Profile: RAML
Conforms? false
Number of results: 1

Level: Violation

- Source: http://a.ml/vocabularies/amf/parser#exampleError
  Message: {"keyword":"type","dataPath":".bar","schemaPath":"#/properties/bar/type","params":{"type":"integer"},"message":"should be integer"}
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/jsonschema/ref/api2.raml#/web-api/end-points/%2Fep2/get/200/application%2Fjson/schema/example/default-example
  Property: 
  Position: Some(LexicalInformation([(31,0)-(31,23)]))
  Location: file://amf-client/shared/src/test/resources/validations/jsonschema/ref/api2.raml
