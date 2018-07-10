// auto-generated class from ValidationsImporter.toScala
package amf.plugins.document.webapi.validation

// scalastyle:off line.contains.tab
object AMFRawValidations {
  val raw = List(
    "amf-parser:mandatory-api-version	Missing madatory Swagger / info / version	OpenAPI	Domain	schema-org:WebAPI	schema-org:version	PropertyShape	sh:path	sh:minCount	1	API Version is Mandatory	Version is mandatory in Info object	Violation",
    "amf-parser:raml-schemes	Protocols property must be http or https	RAML	Domain	schema-org:WebAPI	raml-http:scheme	PropertyShape	sh:path	sh:in	http,https,HTTP,HTTPS	Protocols must have a case insensitive value matching http or https	Swagger object 'schemes' property must have a case insensitive value matching http or https	Violation",
    "amf-parser:raml-schemes	Protocols property must be http or https	RAML08	Domain	schema-org:WebAPI	raml-http:scheme	PropertyShape	sh:path	sh:in	http,https,HTTP,HTTPS	Protocols must have a case insensitive value matching http or https	Swagger object 'schemes' property must have a case insensitive value matching http or https	Violation",
    "amf-parser:openapi-schemes	Protocols property must be http,https,ws,wss	OpenAPI	Domain	schema-org:WebAPI	raml-http:scheme	PropertyShape	sh:path	sh:in	http,https,ws,wss	Protocols must match a value http, https, ws or wss	Swagger object 'schemes' property must have a value matching http, https, ws or wss	Violation",
    "amf-parser:mandatory-external-doc-url	Swagger external-doc element without URL	OpenAPI	Domain	schema-org:CreativeWork	schema-org:url	PropertyShape	sh:path	sh:minCount	1	Documentation URL is mandatory in API external documentation	URL is mandatory in External Documentation object	Violation",
    "amf-parser:mandatory-license-name	Swagger License node without name	OpenAPI	Domain	raml-http:License	schema-org:name	PropertyShape	sh:path	sh:minCount	1	License name is mandatory if license information is included	Name is mandatory in License object	Violation",
    "amf-parser:strict-url-strings	URLs in values mapped to schema-org:url must be valid	AMF	Domain	raml-doc:DomainElement	schema-org:url	NodeShape	sh:targetObjectsOf	sh:nodeKind	sh:IRI	URLs must be valid	URLs must be valid	Violation",
    "amf-parser:empty-responses	No responses declared	OpenAPI	Domain	hydra:Operation	hydra:returns	PropertyShape	sh:path	sh:minCount	1	Responses array cannot be empty	Responses cannot be empty	Violation",
    "amf-parser:empty-enum	Enum in types cannot be empty	OpenAPI	Domain	raml-shapes:Shape	sh:in	PropertyShape	sh:path	sh:node	amf-parser:NonEmptyList	Property 'enum' must have at least one value	Property 'enum' for a Schema object must have at least one value	Violation",
    "amf-parser:raml-status-code	Status code must match a valid numeric status code	RAML	Domain	raml-http:Response	hydra:statusCode	PropertyShape	sh:path	sh:pattern	^([1-5]{1}[0-9]{2})$	Status code must be numeric and in the 1xx-5xx range	Status code must be numeric and in the 1xx-5xx range	Violation",
    "amf-parser:raml-status-code	Status code must match a valid numeric status code	RAML08	Domain	raml-http:Response	hydra:statusCode	PropertyShape	sh:path	sh:pattern	^([1-5]{1}[0-9]{2})$	Status code must be numeric and in the 1xx-5xx range	Status code must be numeric an in the 1xx-5xx range	Violation",
    "amf-parser:array-shape-items-mandatory	Declaration of the type of the items for an array is required	OpenAPI	Domain	raml-shapes:ArrayShape	raml-shapes:items	PropertyShape	sh:path	sh:minCount	1	items facet of RAML Array type is required	items property of Schame objects of type 'array' is required	Violation",
//    "amf-parser:array-shape-items-optional	Declaration of the type of the items for an array is optional	RAML	Domain	raml-shapes:ArrayShape	raml-shapes:items	PropertyShape	sh:path	sh:minCount	0	items facet of RAML Array type is required	items property of Schame objects of type 'array' is required	Violation",
//    "amf-parser:array-shape-items-optional	Declaration of the type of the items for an array is optional	RAML08	Domain	raml-shapes:ArrayShape	raml-shapes:items	PropertyShape	sh:path	sh:minCount	0	items facet of RAML Array type is required	items property of Schame objects of type 'array' is required	Violation",
    "amf-parser:min-max-inclusive\tMaximum must be greater than or equal to minimum\tRAML\tDomain\traml-shapes:ScalarShape\tsh:minInclusive\tPropertyShape\tsh:path\traml-shapes:minimumMaximumValidation\t0\tMaximum must be greater than or equal to minimum\tMaximum must be greater than or equal to minimum\tViolation",
    "amf-parser:min-max-inclusive\tMaximum must be greater than or equal to minimum\tRAML08\tDomain\traml-shapes:ScalarShape\tsh:minInclusive\tPropertyShape\tsh:path\traml-shapes:minimumMaximumValidation\t0\tMaximum must be greater than or equal to minimum\tMaximum must be greater than or equal to minimum\tViolation",
    "amf-parser:min-max-items\tMaxItems must be greater than or equal to minItems\tRAML\tDomain\traml-shapes:ArrayShape\tsh:minCount\tPropertyShape\tsh:path\traml-shapes:minMaxItemsValidation\t0\tMaxItems must be greater than or equal to minItems\tMaxItems must be greater than or equal to minItems\tViolation",
    "amf-parser:min-max-items\tMaxItems must be greater than or equal to minItems\tRAML08\tDomain\traml-shapes:ArrayShape\tsh:minCount\tPropertyShape\tsh:path\traml-shapes:minMaxItemsValidation\t0\tMaxItems must be greater than or equal to minItems\tMaxItems must be greater than or equal to minItems\tViolation",
    "amf-parser:min-max-length\tMaxLength must be greater than or equal to minLength\tRAML\tDomain\traml-shapes:ScalarShape\tsh:minLength\tPropertyShape\tsh:path\traml-shapes:minMaxLengthValidation\t0\tMaxLength must be greater than or equal to minLength\tMaxLength must be greater than or equal to minLength\tViolation",
    "amf-parser:min-max-length\tMaxLength must be greater than or equal to minLength\tRAML08\tDomain\traml-shapes:ScalarShape\tsh:minLength\tPropertyShape\tsh:path\traml-shapes:minMaxLengthValidation\t0\tMaxLength must be greater than or equal to minLength\tMaxLength must be greater than or equal to minLength\tViolation",
    "amf-parser:min-max-properties\tMaxProperties must be greater than or equal to minProperties\tRAML\tDomain\tsh:NodeShape\traml-shapes:minProperties\tPropertyShape\tsh:path\traml-shapes:minMaxPropertiesValidation\t0\tMaxProperties must be greater than or equal to minProperties\tMaxProperties must be greater than or equal to minProperties\tViolation",
    "amf-parser:min-max-properties\tMaxProperties must be greater than or equal to minProperties\tRAML08\tDomain\tsh:NodeShape\traml-shapes:minProperties\tPropertyShape\tsh:path\traml-shapes:minMaxPropertiesValidation\t0\tMaxProperties must be greater than or equal to minProperties\tMaxProperties must be greater than or equal to minProperties\tViolation",
    "		AMF	Domain	raml-doc:DomainElement	schema-org:name	PropertyShape	sh:path	sh:datatype	xsd:string	Title and names must be string	Names must be string	Violation",
    "		AMF	Domain	raml-doc:DomainElement	schema-org:name	PropertyShape	sh:path	sh:maxCount	1	Titles and names must be single values	Names and titles must be single values	Violation",
    "		AMF	Domain	raml-doc:DomainElement	schema-org:description	PropertyShape	sh:path	sh:datatype	xsd:string	Descriptions must be strings	Description must be strings	Violation",
    "		AMF	Domain	raml-doc:DomainElement	schema-org:description	PropertyShape	sh:path	sh:maxCount	1	Descriptions must be single values	Descriptions must be single values	Violation",
    "		AMF	Domain	schema-org:WebAPI	schema-org:name	PropertyShape	sh:path	sh:minCount	1	API name must be a single value	Info object 'title' must be a single value	Violation",
    "		RAML	Domain	schema-org:WebAPI	schema-org:name	PropertyShape	sh:path	sh:minLength	1	Info object 'title' must not be empty	API name must not be an empty string	Violation",
    "		AMF	Domain	schema-org:WebAPI	raml-http:scheme	PropertyShape	sh:path	sh:datatype	xsd:string	API BaseUri scheme information must be a string	Swagger object 'schemes' must be a string	Violation",
    "		AMF	Domain	schema-org:WebAPI	raml-http:scheme	PropertyShape	sh:path	sh:datatype	xsd:string	API BaseUri scheme information must be a string	Swagger object 'schemes' must be a string	Violation",
    "		AMF	Domain	schema-org:WebAPI	raml-http:accepts	PropertyShape	sh:path	sh:datatype	xsd:string	Default media types must contain strings	Field 'consumes' must contain strings	Violation",
    "		AMF	Domain	schema-org:WebAPI	raml-http:accepts	PropertyShape	sh:path	sh:pattern	^\\w+\\/[-+.\\w]+$	Default media types must be valid	Field 'produces' must be valid	Violation",
    "		AMF	Domain	schema-org:WebAPI	raml-http:mediaType	PropertyShape	sh:path	sh:datatype	xsd:string	Default media types must be string	Field 'produces' must contain strings	Violation",
    "		AMF	Domain	schema-org:WebAPI	schema-org:version	PropertyShape	sh:path	sh:datatype	xsd:string	API version must be a string	Info object 'version' must be string	Violation",
    "		AMF	Domain	schema-org:WebAPI	schema-org:version	PropertyShape	sh:path	sh:maxCount	1	API version must be a single value	Info object 'version' must be a single value	Violation",
    "		AMF	Domain	schema-org:WebAPI	schema-org:termsOfService	PropertyShape	sh:path	sh:datatype	xsd:string	API terms of service must be a string	Info object 'termsOfService' must be string	Violation",
    "		AMF	Domain	schema-org:WebAPI	schema-org:termsOfService	PropertyShape	sh:path	sh:maxCount	1	API terms of service must be a single value	Info object 'termsOfService' must a single value	Violation",
//    "		AMF	Domain	schema-org:WebAPI	schema-org:provider	PropertyShape	sh:path	sh:class	schema-org:Organization	API provider must hold Provider information	Info object 'contact' must be a Contact object	Violation",
    "		AMF	Domain	schema-org:WebAPI	schema-org:provider	PropertyShape	sh:path	sh:maxCount	1	API provider must be a single value	Info object 'contact' must be a single value	Violation",
//    "		AMF	Domain	schema-org:WebAPI	raml-http:endpoint	PropertyShape	sh:path	sh:class	raml-http:EndPoint	API paths must link Resources	Paths object must link PathItem objects	Violation",
//    "		AMF	Domain	schema-org:WebAPI	raml-http:parameter	PropertyShape	sh:path	sh:class	raml-http:Parameter	API parameters must be valid parameters	Swagger object 'parameters' must be valid Parameter objects	Violation",
    "		RAML	Domain	schema-org:CreativeWork	schema-org:title	PropertyShape	sh:path	sh:minCount	1	API documentation title is mandatory	Documentation object 'x-title' is mandatory	Violation",
    "		RAML	Domain	schema-org:CreativeWork	schema-org:description	PropertyShape	sh:path	sh:minCount	1	API documentation content is mandatory	Documentation object 'description' is mandatory	Violation",
    "		AMF	Domain	schema-org:Organization	schema-org:url	PropertyShape	sh:path	sh:maxCount	1	API provider URL must be a single value	Contact object 'url' must be a single value	Violation",
    "		AMF	Domain	schema-org:Organization	schema-org:email	PropertyShape	sh:path	sh:datatype	xsd:string	API provider email must be a string	Contact object 'email' must be a string	Violation",
    "		AMF	Domain	schema-org:Organization	schema-org:email	PropertyShape	sh:path	sh:maxCount	1	API provider must be a single value	Contact object 'email' must be a single value	Violation",
    "		AMF	Domain	raml-http:EndPoint	raml-http:path	PropertyShape	sh:path	sh:maxCount	1	Resource path must be unique	PathItem object must have a single path	Violation",
    "		AMF	Domain	raml-http:EndPoint	raml-http:path	PropertyShape	sh:path	sh:datatype	xsd:string	Resource path must be a string	PathItem object path must be a string	Violation",
    "		AMF	Domain	raml-http:EndPoint	raml-http:path	PropertyShape	sh:path	sh:pattern	^/	Resource path must start with a '/'	PathItem path must start with a '/'	Violation",
//    "		AMF	Domain	raml-http:Endpoint	hydra:supportedOperation	PropertyShape	sh:path	sh:class	hydra:Operation	Resources must hold Methods operation	PathItems must hold Operations information	Violation",
//    "		AMF	Domain	raml-http:Endpoint	raml-http:parameter	PropertyShape	sh:path	sh:class	raml-http:Parameter	Resources uriParameters must be valid parameters	PathItems 'parameters' property must be valid parameters	Violation",
    "		AMF	Domain	hydra:Operation	hydra:method	PropertyShape	sh:path	sh:in	get,put,post,delete,options,head,patch,connect,trace	Unknown method type	Unknown Operation method	Violation",
    "		AMF	Domain	hydra:Operation	hydra:method	PropertyShape	sh:path	sh:maxCount	1	Methods can only have a single type	Operations can only have a single type	Violation",
//    "		AMF	Domain	hydra:Operation	hydra:returns	PropertyShape	sh:path	sh:class	raml-http:Response	Methods must hold Responses information	Operations must hold Responses information	Violation",
    "		AMF	Domain	hydra:Operation	raml-http:guiSummary	PropertyShape	sh:path	sh:datatype	xsd:string	Methods' summary information must be a string	Methods' summary information must be a string	Violation",
    "		AMF	Domain	hydra:Operation	raml-http:guiSummary	PropertyShape	sh:path	sh:maxCount	1	Methods can only have a single value for the summary information	Methods can only have a single value for summary information	Violation",
    "		AMF	Domain	hydra:Operation	raml-doc:deprecated	PropertyShape	sh:path	sh:datatype	xsd:boolean	Methods' deprecated must be a boolean	Methods' deprecated must be a boolean	Violation",
    "		AMF	Domain	hydra:Operation	raml-doc:deprecated	PropertyShape	sh:path	sh:maxCount	1	Methods' deprecated must be a single value	Methods' deprecated must be a single value	Violation",
    "		AMF	Domain	hydra:Operation	raml-http:scheme	PropertyShape	sh:path	sh:datatype	xsd:string	Protocols must contain strings	Schemes must contain strings	Violation",
    "		AMF	Domain	hydra:Operation	raml-http:accepts	PropertyShape	sh:path	sh:datatype	xsd:string	Method default media types consumed must be strings	Operation object 'consumes' must be strings	Violation",
//    "		AMF	Domain	hydra:Operation	hydra:returns	PropertyShape	sh:path	sh:class	raml-http:Response	An operation must have valid response information	Operation object must return a valid Response object	Violation",
//    "		AMF	Domain	raml-http:Request	raml-http:header	PropertyShape	sh:path	sh:class	raml-http:Parameter	Header information must be a valid header	Header information must be a valid header	Violation",
//    "		AMF	Domain	raml-http:Request	raml-http:payload	PropertyShape	sh:path	sh:class	raml-http:Payload	Payload information for a Method request info must be valid	Payload information for an Operation object request info must be valid	Violation",
//    "		AMF	Domain	raml-http:Request	raml-http:parameter	PropertyShape	sh:path	sh:class	raml-http:Parameter	Parameter information for a Method must be valid	Parameter information for an Operation object must be valid	Violation",
    "		AMF	Domain	raml-http:Response	hydra:statusCode	PropertyShape	sh:path	sh:datatype	xsd:string	Status code for a Response must be a string	Status code for a Response object must be a string	Violation",
    "		AMF	Domain	raml-http:Response	hydra:statusCode	PropertyShape	sh:path	sh:pattern	^([0-9]{3})$|^(default)$	Status code for a Response must be valid (match '^([0-9]{3})$|^(default)$')	Status code for a Response must be valid (match '^([0-9]{3})$|^(default)$')	Violation",
//    "		AMF	Domain	raml-http:Response	raml-http:header	PropertyShape	sh:path	sh:class	raml-http:Parameter	Header information must be a valid header	Header information must be a valid header	Violation",
//    "		AMF	Domain	raml-http:Response	raml-http:payload	PropertyShape	sh:path	sh:class	raml-http:Payload	Payload information for a Response must be valid	Payload information for a response must be valid	Violation",
    "		AMF	Domain	raml-http:Parameter	schema-org:name	PropertyShape	sh:path	sh:minCount	1	Parameter information must have a name	Parameter object must have a name property	Violation",
    "		AMF	Domain	raml-http:Parameter	hydra:required	PropertyShape	sh:path	sh:datatype	xsd:boolean	Information about required parameters must be a boolean value	Required property of a Parameter object must be boolean	Violation",
//    "		AMF	Domain	raml-http:Parameter	hydra:required	PropertyShape	sh:path	sh:minCount	1	Required information about a parameter is mandatory	Required property of a Parameter object is mandatory	Violation",
    "		AMF	Domain	raml-http:Parameter	hydra:required	PropertyShape	sh:path	sh:maxCount	1	Only one value is allowed for required information of a parameter	Required property of a Parameter object must be a single value	Violation",
    "		AMF	Domain	raml-http:Parameter	raml-http:binding	PropertyShape	sh:path	sh:datatype	xsd:string	Information about the binding of the parameter is mandatory	in' property of a Parameter object must be a string	Violation",
    "		AMF	Domain	raml-http:Parameter	raml-http:binding	PropertyShape	sh:path	sh:minCount	1	Binding information for a parameter is mandatory	in' property of a Parameter object is mandatory	Violation",
    "		AMF	Domain	raml-http:Parameter	raml-http:binding	PropertyShape	sh:path	sh:maxCount	1	Only one binding is allowed per parameter	in' property of a Parameter object must be a single value	Violation",
    "		AMF	Domain	raml-http:Parameter	raml-http:binding	PropertyShape	sh:path	sh:in	query,path,header,uri	Binding information for a parameter with an invalid value	in' property of a parameter with an invalid value	Violation",
//    "		AMF	Domain	raml-http:Parameter	raml-http:schema	PropertyShape	sh:path	sh:class	sh:Shape	Expected RAML Type not found	Expected Schema object not found	Violation",
    "		AMF	Domain	raml-http:Parameter	raml-http:schema	PropertyShape	sh:path	sh:maxCount	1	Only one RAML type can be specified	Only one Schema object can be specified	Violation",
    "		AMF	Domain	raml-http:Parameter	raml-http:schema	PropertyShape	sh:path	sh:minCount	1	RAML Type information is mandatory for parameters	Schema/type information required for Parameter objects	Violation",
    "		AMF	Domain	raml-http:Payload	raml-http:mediaType	PropertyShape	sh:path	sh:datatype	xsd:string	Method default media types must be strings	Operation object 'produces' must be strings	Violation",
    "		AMF	Domain	raml-http:Payload	raml-http:mediaType	PropertyShape	sh:path	sh:maxCount	1	Only one media type per payload is allowed	Only one media type per payload is allowed	Violation",
    "		AMF	Domain	raml-http:Payload	raml-http:mediaType	PropertyShape	sh:path	sh:maxCount	1	Only one media type per payload is allowed	Only one media type per payload is allowed	Violation",
    "		AMF	Domain	raml-http:Payload	raml-http:mediaType	PropertyShape	sh:path	sh:pattern	^(([-\\w]+|[*]{1})\\/([-+.\\w]+|[*]{1}))$|^text\\/\\w+; charset=[-\\w]+$	Invalid media type for method	Swagger Operation object 'produces' must be a valid media type	Violation",
//    "		AMF	Domain	raml-http:Payload	raml-http:schema	PropertyShape	sh:path	sh:class	sh:Shape	Expected RAML Type not found	Expected Schema object not found	Violation",
    "		AMF	Domain	raml-http:Payload	raml-http:schema	PropertyShape	sh:path	sh:maxCount	1	Only one RAML type can be specified	Only one Schema object can be specified	Violation",
//    "		AMF	Domain	raml-shapes:Shape	raml-shapes:xmlSerialization	PropertyShape	sh:path	sh:class	raml-shapes:XMLSerializer	Property 'xml' of a RAML type must have as a value a valid XML Serialization	Property 'xml' of a Schema object must have as a value a valid XML object	Violation",
    "		AMF	Domain	raml-shapes:Shape	raml-shapes:xmlSerialization	PropertyShape	sh:path	sh:maxCount	1	XML serialisation object must be a single value	XML Object for the 'xml' property of a Schema object must be  single value	Violation",
    "		AMF	Domain	raml-shapes:Shape	sh:in	PropertyShape	sh:path	sh:maxCount	1	Property 'enum'  must have a single list of values	Property 'enum' for a Schema object must have a single list of values	Violation",
    "		AMF	Domain	raml-shapes:Shape	sh:in	PropertyShape	sh:path	sh:node	amf-parser:NonEmptyList	Only 1 array can be specified in RAML type enum	Ony 1 array can be specified in Schame object enum property	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlAtribute	PropertyShape	sh:path	sh:datatype	xsd:boolean	XML attribute serialisation info must be boolean	XML attribute serialisation info must be boolean	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlAtribute	PropertyShape	sh:path	sh:maxCount	1	property 'attribute' of the XML serialisation mut be a single value	property 'attribute' of the XML serialisation mut be a single value	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlWrapped	PropertyShape	sh:path	sh:datatype	xsd:boolean	XML wraping serialisation info must be boolean	XML wrapping serialisation info must be boolean	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlWrapped	PropertyShape	sh:path	sh:maxCount	1	property 'wrapped' of the XML serialisation mut be a single value	property 'wrapped' of the XML serialisation mut be a single value	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlName	PropertyShape	sh:path	sh:datatype	xsd:string	XML name serialisation info must be string	XML name serialisation info must be string	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlName	PropertyShape	sh:path	sh:maxCount	1	property 'name' of the XML serialisation mut be a single value	property 'name' of the XML serialisation mut be a single value	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlNamespace	PropertyShape	sh:path	sh:datatype	xsd:string	XML namespace serialisation info must be string	XML namespace serialisation info must be string	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlNamespace	PropertyShape	sh:path	sh:maxCount	1	property 'namespace' of the XML serialisation mut be a single value	property 'namespace' of the XML serialisation mut be a single value	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlPrefix	PropertyShape	sh:path	sh:datatype	xsd:string	XML prefix serialisation info must be string	XML prefix serialisation info must be string	Violation",
    "		AMF	Domain	raml-shapes:XMLSerializer	raml-shapes:xmlPrefix	PropertyShape	sh:path	sh:maxCount	1	Property 'prefix' of the XML serialisation mut be a single value	Property 'prefix' of the XML serialisation mut be a single value	Violation",
    "		AMF	Domain	raml-shapes:Shape	sh:defaultValue	PropertyShape	sh:path	sh:maxCount	1	Default value for a RAML type must be unique	Default property for an Schema object must be unique	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:minProperties	PropertyShape	sh:path	sh:minInclusive	0	minProperties for a RAML Object type cannot be negative	minProperties for a Schema object cannot be negative	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:minProperties	PropertyShape	sh:path	sh:datatype	xsd:integer	minProperties for a RAML Object type must be an integer	minProperties for a Schema object must be an integer	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:minProperties	PropertyShape	sh:path	sh:maxCount	1	minProperties for a RAML Object type must be unique	minProperties for a Schema object must be unique	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:maxProperties	PropertyShape	sh:path	sh:minInclusive	0	maxProperties for a RAML Object type cannot be negative	maxProperties for a Schema object cannot be negative	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:maxProperties	PropertyShape	sh:path	sh:datatype	xsd:integer	maxProperties for a RAML Object type must be an integer	maxProperties for a Schema object must be an integer	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:maxProperties	PropertyShape	sh:path	sh:maxCount	1	maxProperties for a RAML Object type must be unique	maxProperties for a Schema object must be unique	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	sh:closed	PropertyShape	sh:path	sh:datatype	xsd:boolean	additionalProperties for a RAML Object type must be a boolean	additionalProperties for a Schema object must be a boolean	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	sh:closed	PropertyShape	sh:path	sh:maxCount	1	additionalProperties for a RAML Object must be unique	additionalProperties for a Schema object must be unique	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:discriminator	PropertyShape	sh:path	sh:datatype	xsd:string	discriminator for RAML Object type must be a string value	discriminator for a Schema object must be a string value	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:discriminator	PropertyShape	sh:path	sh:maxCount	1	discriminator for RAML Object type must be unique	discriminator for a Schema object must be unique	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:discriminatorValue	PropertyShape	sh:path	sh:datatype	xsd:string	x-discriminatorValue for RAML Object type must be a string value	discriminatorValue for a Schema object must be a string value	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:discriminatorValue	PropertyShape	sh:path	sh:maxCount	1	x-discriminatorValue for RAML Object type must be unique	discriminatorValue for a Schema object must be unique	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:readOnly 	PropertyShape	sh:path	sh:datatype	xsd:boolean	(readOnly) for a RAML Object type must be a boolean	readOnly for a Schema object must be a boolean	Violation",
    "		AMF	Domain	raml-shapes:ObjectShape	raml-shapes:readOnly 	PropertyShape	sh:path	sh:maxCount	1	(readOnly) for a RAML Object must be unique	readOnly for a Schema object must be unique	Violation",
//    "		AMF	Domain	raml-shapes:ArrayShape	raml-shapes:item	PropertyShape	sh:path	sh:class	raml-shapes:Shape	value of the items facet must be a RAML Type 	value of the items property must be a Schema node	Violation",
    "		AMF	Domain	raml-shapes:ArrayShape	sh:minCount	PropertyShape	sh:path	sh:datatype	xsd:integer	minItems for a RAML Array type must be an integer	minItems of a Schema object of type 'array' must be an integer	Violation",
    "		AMF	Domain	raml-shapes:ArrayShape	sh:minCount	PropertyShape	sh:path	sh:maxCount	1	minItems for a RAML Array type must be unique	minItems of a Schema object of type 'array' must be unique	Violation",
    "		AMF	Domain	raml-shapes:ArrayShape	sh:minCount	PropertyShape	sh:path	sh:minInclusive 	0	maxItems for a RAML Array type must be greater than 0	maxItems of a Schema object of type 'array' must be greater than 0	Violation",
    "		AMF	Domain	raml-shapes:ArrayShape	sh:maxCount	PropertyShape	sh:path	sh:datatype	xsd:integer	maxItems for a RAML Array type must be an integer	maxItems of a Schema object of type 'array' must be an integer	Violation",
    "		AMF	Domain	raml-shapes:ArrayShape	sh:maxCount	PropertyShape	sh:path	sh:maxCount	1	maxItems for a RAML Array type must be unique	maxItems of a Schema object of type 'array' must be unique	Violation",
    "		AMF	Domain	raml-shapes:ArrayShape	sh:minCount	PropertyShape	sh:path	sh:minInclusive 	0	minItems for a RAML Array type must be greater than 0	minItems of a Schema object of type 'array' must be greater than 0	Violation",
    "		AMF	Domain	raml-shapes:ArrayShape	raml-shapes:uniqueItems	PropertyShape	sh:path	sh:datatype	xsd:boolean	uniqueItems for a RAML Array type must be a boolean	uniqueItems of a Schema object of type 'array' must be a boolean	Violation",
    "		AMF	Domain	raml-shapes:ArrayShape	raml-shapes:uniqueItems	PropertyShape	sh:path	sh:maxCount	1	uniqueItems for a RAML Array type must be unique	uniqueItems of a Schema object of type 'array' must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:pattern	PropertyShape	sh:path	sh:datatype	xsd:string	pattern facet for a RAML scalar type must be a string	pattern for scalar Schema object of scalar type must be a string	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:pattern	PropertyShape	sh:path	sh:maxCount	1	pattern facet for a RAML scalar type must be unique	pattern for scalar Schema object of scalar type must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:minLength	PropertyShape	sh:path	sh:datatype	xsd:integer	minLength facet for a RAML scalar type must be a integer	minLength for scalar Schema object of scalar type must be a integer	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:minLength	PropertyShape	sh:path	sh:maxCount	1	minLength facet for a RAML scalar type must be unique	minLength for scalar Schema object of scalar type must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:maxLength	PropertyShape	sh:path	sh:datatype	xsd:integer	maxLength facet for a RAML scalar type must be a integer	maxLength for scalar Schema object of scalar type must be a integer	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:maxLength	PropertyShape	sh:path	sh:maxCount	1	maxLength facet for a RAML scalar type must be unique	maxLength for scalar Schema object of scalar type must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:minInclusive	PropertyShape	sh:path	sh:datatype	xsd:double	minimum facet for a RAML scalar type must be a number	minimum for scalar Schema object of scalar type must be a integer	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:minInclusive	PropertyShape	sh:path	sh:maxCount	1	minimum facet for a RAML scalar type must be unique	minimum for scalar Schema object of scalar type must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:maxInclusive	PropertyShape	sh:path	sh:datatype	xsd:double	maximum facet for a RAML scalar type must be a number	maximum for scalar Schema object of scalar type must be a integer	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:maxInclusive	PropertyShape	sh:path	sh:maxCount	1	maximum facet for a RAML scalar type must be unique	maximum for scalar Schema object of scalar type must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:minExclusive	PropertyShape	sh:path	sh:datatype	xsd:boolean	x-exclusiveMinimum facet for a RAML scalar type must be a boolean	exclusiveMinimum for scalar Schema object of scalar type must be a boolean	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:minExclusive	PropertyShape	sh:path	sh:maxCount	1	x-exclusiveMinimum facet for a RAML scalar type must be unique	exclusiveMinimum for scalar Schema object of scalar type must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:maxExclusive	PropertyShape	sh:path	sh:datatype	xsd:boolean	x-exclusiveMaximum facet for a RAML scalar type must be a boolean	exclusiveMaximum for scalar Schema object of scalar type must be a boolean	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:maxExclusive	PropertyShape	sh:path	sh:maxCount	1	x-exclusiveMaximum facet for a RAML scalar type must be unique	exclusiveMaximum for scalar Schema object of scalar type must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:minLength	PropertyShape	sh:path	sh:minInclusive	0	Min length facet should be greater or equal than 0	Min length facet should be greater or equal than 0	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:maxLength	PropertyShape	sh:path	sh:minInclusive	0	Max length facet should be greater or equal than 0	Max length facet should be greater or equal than 0	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	raml-shapes:format	PropertyShape	sh:path	sh:datatype	xsd:string	format facet for a RAML scalar type must be a string	format for scalar Schema object of scalar type must be a string	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	raml-shapes:format	PropertyShape	sh:path	sh:maxCount	1	format facet for a RAML scalar type must be unique	format for scalar Schema object of scalar type must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	raml-shapes:multipleOf	PropertyShape	sh:path	sh:datatype	xsd:double	multipleOf facet for a RAML scalar type must be a number	multipleOf for scalar Schema object of scalar type must be a number	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	raml-shapes:multipleOf	PropertyShape	sh:path	sh:maxCount	1	multipleOf facet for a RAML scalar type must be unique	multipleOf for scalar Schema object of scalar type must be unique	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	raml-shapes:multipleOf	PropertyShape	sh:path	sh:minExclusive	0	multipleOf facet for a RAML scalar type must be greater than 0	multipleOf for scalar Schema object of scalar type must be greater than 0	Violation",
    "		AMF	Domain	raml-shapes:ScalarShape	sh:datatype	PropertyShape	sh:path	sh:minCount	1	type information for a RAML scalar is required	type information fo a Schema object of scalar type is required	Violation",
    "		AMF	Domain	raml-doc:DomainProperty	raml-shapes:schema	PropertyShape	sh:path	sh:minCount	1	type is mandatory for a RAML annotationType	schema is mandatory for an extension type	Violation",
    "		AMF	Domain	raml-doc:DomainProperty	raml-shapes:schema	PropertyShape	sh:path	sh:maxCount	1	type must be a single value for a RAML annotationType	schema must be a single value for an extension type	Violation",
//    "		AMF	Domain	raml-doc:DomainProperty	raml-shapes:schema	PropertyShape	sh:path	sh:class	raml-shapes:Shape	type value must be a RAML Type	schema value must be a Schema object	Violation",
    "		AMF	Domain	raml-http:Tag	schema-org:name	PropertyShape	sh:path	sh:minCount	1	Tag must have a name	Tag object must have a name property	Violation",
    "		AMF	Domain	raml-http:Server	raml-http:url	PropertyShape	sh:path	sh:datatype	xsd:string	API baseUri host information must be a string	Swagger object 'host' and 'basePath' must be a string	Violation",
    "		AMF	Domain	raml-http:Server	schema-org:description	PropertyShape	sh:path	sh:datatype	xsd:string	Server 'description' property must be a string	Server 'description' property must be a string	Violation",
    "		AMF	Domain	raml-http:Server	raml-http:url	PropertyShape	sh:path	sh:minCount	1	Server must have an 'url' property	Server must have an 'url' propert	Violation",
    "		AMF	Domain	security:SecurityScheme	security:type	PropertyShape	sh:path	sh:pattern	^OAuth\\s1.0|OAuth\\s2.0|Basic\\sAuthentication|Digest\\sAuthentication|Pass\\sThrough|\\(apiKey\\)|x-.+$	Security scheme type should be one of the supported ones	Security scheme type should be one of the supported ones	Violation",
    "		AMF	Domain\tsecurity:SecurityScheme\tsecurity:type\tPropertyShape\tsh:path\tsh:minCount\t1\tType is mandatory in a Security Scheme Object\tType is mandatory in a Security Scheme Object\tViolation",
    "		RAML\tDomain\tsecurity:Settings\tsecurity:authorizationGrant\tPropertyShape\tsh:path\tsh:pattern\t^authorization_code|password|client_credentials|implicit|(\\w+:(\\/?\\/?)[^\\s]+)$\tInvalid authorization grant. The options are: authorization_code, password, client_credentials, implicit or any valid absolut URI\tInvalid authorization grant. The options are: authorization_code, password, client_credentials, implicit or any valid absolut URI\tViolation",
    "		RAML08\tDomain\tsecurity:Settings\tsecurity:authorizationGrant\tPropertyShape\tsh:path\tsh:pattern\t^code|token|owner|credentials$\tInvalid authorization grant. The options are: code, token, owner or credentials\tInvalid authorization grant. The options are: code, token, owner or credentials\tViolation"
  )
}
