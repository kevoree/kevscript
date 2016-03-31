grammar KevScript;

script
    : statement*
    ;
statement
    : instance
    | add
    | remove
    | attach
    | detach
    | move
    | start
    | stop
    | set
    | bind
    | unbind
    | netinit
    | netmerge
    | netremove
    | metainit
    | metamerge
    | metaremove
    | letDecl
    | funcDecl
    | forDecl
    | funcCall
    | importDecl
    | attachModelConnector
    | detachModelConnector
    | replaceModelConnector
    | timeDecl
    ;
instance
    : INSTANCE varName=basic_identifier ASSIGN (instanceName=expression?) type
    | INSTANCE varIdentifierList ASSIGN type
    ;
add
    : ADD identifier (LS_BRACKET identifierList RS_BRACKET | identifier)? // attach a list of components to a node. if the list is empty the node is added to the model
    | ADD LS_BRACKET identifierList RS_BRACKET                            // add a list of instances at the root of the model
    | ADD instanceList                                                    // add a list of instances path (add a:b = add component b to node a)
    ;
remove
    : REMOVE instanceList
    ;
start
    : START instanceList
    ;
stop
    : STOP instanceList
    ;
set
    : SET dictionaryPath ASSIGN val=expression
    ;
attach
    : ATTACH groupId=identifier nodesId=identifierList
    ;
detach
    : DETACH groupId=identifier nodesId=identifierList
    ;

attachModelConnector : ATTACH_MODEL_CONNECTOR nodeId=identifier modelConnectorId=identifier;
detachModelConnector : DETACH_MODEL_CONNECTOR nodeId=identifier ;
replaceModelConnector : REPLACE_MODEL_CONNECTOR nodeId=identifier modelConnectorId=identifier;


move
    : MOVE identifier (LS_BRACKET identifierList RS_BRACKET | identifier | instanceList)            // move a list of instances to a targeted node ; if instanceList is a single element, this element is renamed)
    | MOVE instancePath instancePath                                                                // move an instance path to another instance path
    ;
bind
    : BIND chan=identifier nodes=portList
    ;
unbind
    : UNBIND chan=identifier nodes=portList
    ;
letDecl
    : EXPORT? LET basic_identifier ASSIGN val=expression
    ;
netinit
    : NETINIT identifier (objectDecl|identifier)
    ;
netmerge
    : NETMERGE identifier (objectDecl|identifier)
    ;
netremove
    : NETREMOVE identifier identifier
    | NETREMOVE identifier LS_BRACKET identifierList RS_BRACKET
    ;
metainit
    : METAINIT identifier (objectDecl|identifier)
    ;
metamerge
    : METAMERGE identifier (objectDecl|identifier)
    ;
metaremove
    : METAREMOVE identifier identifier
    | METAREMOVE identifier LS_BRACKET identifierList RS_BRACKET
    ;
varIdentifierList
    : basic_identifier (COMMA basic_identifier)*
    ;
forDecl
    : FOR L_BRACKET (index=basic_identifier COMMA)? val=basic_identifier IN iterable R_BRACKET LC_BRACKET forBody RC_BRACKET
    ;
iterable
    : arrayDecl
    | identifier
    | contextIdentifier
    ;
forBody
    : statement*
    ;
objectDecl
    : LC_BRACKET (values+=keyAndValue (COMMA values+=keyAndValue)*)? RC_BRACKET
    ;
keyAndValue
    : key=basic_identifier COLON value=expression
    ;
arrayDecl
    : LS_BRACKET expressionList? RS_BRACKET
    ;
funcCall
    : basic_identifier (DOT basic_identifier)? L_BRACKET parameters=expressionList? R_BRACKET // replace ID by a namespace+fonction reference.
    ;
funcDecl
    : EXPORT? FUNCTION functionName=basic_identifier L_BRACKET parameters=varIdentifierList? R_BRACKET LC_BRACKET funcBody RC_BRACKET
    | EXPORT? FUNCTION NATIVE functionName=basic_identifier L_BRACKET parameters=varIdentifierList? R_BRACKET SOURCE_CODE
    ;

timeDecl
    : TIME (NUMERIC_VALUE|identifier) LC_BRACKET statement* RC_BRACKET
    ;

importDecl
    : IMPORT (qualifiers=varIdentifierList FROM)? resource=string (AS basic_identifier)?
    ;

funcBody
    : (statement*) returnStatement?
    ;
returnStatement
    : RETURN expression
    ;
expression
    : string                        // a raw string
    | objectDecl                    // a object declaration
    | contextIdentifier             // a context reference
    | expression CONCAT expression  // a concatenation of expressions
    | arrayDecl                     // a list of values declaration
    | arrayAccess
    | identifier
    | funcCall
    | instancePath
    | portPath
    ;
expressionList
    : expression (COMMA expression)*
    ;
arrayAccess
    : basic_identifier LS_BRACKET NUMERIC_VALUE RS_BRACKET // IMPLEM : can numeric value be replaced by a variable ?
    ;
contextIdentifier
    : basic_identifier
    | contextRef
    | arrayAccess
    | contextIdentifier DOT contextIdentifier
    ;
contextRef
    : AMPERSAND contextIdentifier
    ;
identifier
    : basic_identifier (DOT identifier) ?
    | contextRef
    | funcCall (DOT identifier) ?
    | arrayAccess (DOT identifier) ?
    ;
identifierList
    : identifiers+=identifier (COMMA identifiers+=identifier)*
    ;
instancePath
    : identifier (COLON identifier)?
    ;
portPath
    : (instancePath (LEFT_LIGHT_ARROW|RIGHT_LIGHT_ARROW))? identifier
    ;
dictionaryPath
    : key=instancePath SHARP identifier (SLASH frag=identifier)?
    ;

instanceList
    : instances+=instancePath
    | LS_BRACKET instances+=instancePath (COMMA instances+=instancePath)* RS_BRACKET
    ;
portList
    : instances+=portPath
    | LS_BRACKET instances+=portPath (COMMA instances+=portPath)* RS_BRACKET
    ;
type
    : typeName (SLASH version duVersions?)?
    ;
typeName
    : (basic_identifier DOT)? basic_identifier
    ;
version
    : NUMERIC_VALUE
    | identifier
    ;
duVersions
    : objectDecl
    | identifier
    ;
string
    : value=SQ_STR
    | value=DQ_STR
    ;

basic_identifier : ID ;

FROM : 'from' ;
IMPORT : 'import' ;
EXPORT : 'export' ;
SHARP : '#' ;
RETURN : 'return' ;
ASSIGN : '=' ;
COLON : ':' ;
COMMA : ',' ;
DOT : '.' ;
CONCAT : '+' ;
AMPERSAND: '&';
SLASH : '/' ;
LS_BRACKET : '[' ;
RS_BRACKET : ']' ;
LC_BRACKET : '{' ;
RC_BRACKET : '}' ;
R_BRACKET : ')' ;
L_BRACKET : '(' ;
RIGHT_LIGHT_ARROW : '<-' ;
LEFT_LIGHT_ARROW : '->' ;
FOR : 'for' ;
IN : 'in' ;
INSTANCE: 'instance';
ADD : 'add' ;
AS : 'as' ;
TIME : 'time' ;
REMOVE : 'remove' ;
START : 'start' ;
STOP : 'stop' ;
SET : 'set' ;
DETACH : 'detach' ;
MOVE : 'move' ;
ATTACH : 'attach' ;
BIND : 'bind' ;
UNBIND : 'unbind' ;
LET : 'let' ;
FUNCTION : 'function' ;
NATIVE : 'native' ;
NETINIT : 'net-init' ;
NETMERGE : 'net-merge' ;
NETREMOVE : 'net-remove' ;
METAINIT : 'meta-init' ;
METAMERGE : 'meta-merge' ;
METAREMOVE : 'meta-remove' ;
ATTACH_MODEL_CONNECTOR : 'attach-model-connector' ;
DETACH_MODEL_CONNECTOR : 'detach-model-connector' ;
REPLACE_MODEL_CONNECTOR : 'replace-model-connector' ;

SOURCE_CODE : '{{{' .*? '}}}';
COMMENT
    : '/*' .*? '*/' -> skip
    ;
SINGLELINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;
NUMERIC_VALUE
    : [0-9]+
    ;
ID
    : [a-zA-Z_]([a-zA-Z0-9_-]*[a-zA-Z0-9_])?
    ;
SQ_STR
    : '\'' (~('\'' | '\\' | '\r' | '\n') | '\\' ('\'' | '\\'))* '\''
    ;
DQ_STR
    : '"' (~('"' | '\\' | '\r' | '\n') | '\\' ('"' | '\\'))* '"'
    ;
WS
    : [ \t\r\n]+ -> skip
    ;
