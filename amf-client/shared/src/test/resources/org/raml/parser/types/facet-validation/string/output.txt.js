Model: file://amf-client/shared/src/test/resources/org/raml/parser/types/facet-validation/string/input.raml
Profile: RAML 1.0
Conforms? false
Number of results: 1

Level: Violation

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: should NOT be shorter than 10 characters
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/org/raml/parser/types/facet-validation/string/input.raml#/declarations/types/scalar/DefaultMaxLength/example/two
  Property: 
  Position: Some(LexicalInformation([(14,13)-(14,18)]))
  Location: file://amf-client/shared/src/test/resources/org/raml/parser/types/facet-validation/string/input.raml
