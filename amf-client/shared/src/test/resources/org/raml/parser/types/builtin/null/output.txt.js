Model: file://amf-client/shared/src/test/resources/org/raml/parser/types/builtin/null/input.raml
Profile: RAML 1.0
Conforms? false
Number of results: 2

Level: Violation

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: lastname should be string
middlename should be null

  Level: Violation
  Target: file://amf-client/shared/src/test/resources/org/raml/parser/types/builtin/null/input.raml#/declarations/types/User/example/wrong-type
  Property: file://amf-client/shared/src/test/resources/org/raml/parser/types/builtin/null/input.raml#/declarations/types/User/example/wrong-type
  Position: Some(LexicalInformation([(15,0)-(18,0)]))
  Location: file://amf-client/shared/src/test/resources/org/raml/parser/types/builtin/null/input.raml

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: should have required property 'middlename'
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/org/raml/parser/types/builtin/null/input.raml#/declarations/types/User/example/missing-field
  Property: 
  Position: Some(LexicalInformation([(19,0)-(21,0)]))
  Location: file://amf-client/shared/src/test/resources/org/raml/parser/types/builtin/null/input.raml
