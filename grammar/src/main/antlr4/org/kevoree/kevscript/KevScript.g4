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
    | reattach
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
    | timeDecl
    | worldDecl
    ;
instance
    : INSTANCE varName=basicIdentifier ASSIGN (instanceName=expression)? type   #InstanceSingleIdentifier
    | INSTANCE varIdentifierList ASSIGN type                                    #InstanceManyIdentifiers
    ;
add
    : ADD (target=instancePath)? sources=instanceList
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
    : ATTACH groupId=identifier nodeId=identifier
    ;
detach
    : DETACH instanceList
    ;
reattach
    : REATTACH groupId=identifier nodeId=identifier
    ;
move
    : MOVE instancePath instanceList
    ;
bind
    : BIND chan=identifier nodes=portList
    ;
unbind
    : UNBIND chan=identifier nodes=portList
    ;
letDecl
    : EXPORT? LET basicIdentifier ASSIGN val=expression
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
    : basicIdentifier (COMMA basicIdentifier)*
    ;
forDecl
    : FOR L_BRACKET (index=basicIdentifier COMMA)? val=basicIdentifier IN iterable R_BRACKET LC_BRACKET forBody RC_BRACKET
    ;
iterable
    : arrayDecl             #IterableArrayDecl
    | identifier            #IterableIdentifier
    | contextIdentifier     #IterableContextIdentifier
    ;
forBody
    : statement*
    ;
objectDecl
    : LC_BRACKET (values+=keyAndValue (COMMA values+=keyAndValue)*)? RC_BRACKET
    ;
keyAndValue
    : key=basicIdentifier COLON value=expression
    ;
arrayDecl
    : LS_BRACKET expressionList? RS_BRACKET
    ;
funcCall
    : (basicIdentifier DOT)? basicIdentifier L_BRACKET parameters=expressionList? R_BRACKET
    ;
funcDecl
    : EXPORT? FUNCTION functionName=basicIdentifier L_BRACKET parameters=varIdentifierList? R_BRACKET LC_BRACKET funcBody RC_BRACKET
    | EXPORT? FUNCTION NATIVE functionName=basicIdentifier L_BRACKET parameters=varIdentifierList? R_BRACKET SOURCE_CODE
    ;
timeDecl
    : TIME (NUMERIC_VALUE|identifier) LC_BRACKET statement* RC_BRACKET
    ;
worldDecl
    : WORLD (NUMERIC_VALUE|identifier) LC_BRACKET statement* RC_BRACKET
    ;
importDecl
    : IMPORT (qualifiers=varIdentifierList FROM)? resource=string (AS basicIdentifier)?
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
    | contextRef                    // a context reference
    | expression CONCAT expression  // a concatenation of expressions
    | arrayDecl                     // a list of values declaration
    | identifier
    | instancePath
    | portPath
    ;
expressionList
    : expression (COMMA expression)*
    ;
arrayAccess
    : LS_BRACKET NUMERIC_VALUE RS_BRACKET // TODO handle identifier instead of only NUMERIC_VALUE
    ;
contextIdentifier
    : basicIdentifier
    | contextRef
    | basicIdentifier arrayAccess (DOT contextIdentifier)?
    | contextIdentifier DOT contextIdentifier
    ;
contextRef
    : AMPERSAND contextIdentifier
    ;
identifier
    : basicIdentifier (DOT identifier)?
    | contextRef
    | funcCall (DOT identifier)?
    | basicIdentifier arrayAccess (DOT identifier)?
    | funcCall arrayAccess (DOT identifier)?
    ;
identifierList
    : identifiers+=identifier (COMMA identifiers+=identifier)*
    ;
instancePath
    : identifier (COLON identifier)?
    ;
portPath
    : (instancePath (LEFT_ARROW|RIGHT_ARROW))? identifier
    ;
dictionaryPath
    : instancePath SHARP name=identifier (SLASH fragmentName=identifier)?
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
    : (basicIdentifier DOT)? basicIdentifier
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

basicIdentifier : ID ;

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
LEFT_ARROW : '<-' ;
RIGHT_ARROW : '->' ;
FOR : 'for' ;
IN : 'in' ;
INSTANCE: 'instance';
ADD : 'add' ;
AS : 'as' ;
TIME : 'time' ;
WORLD : 'world' ;
REMOVE : 'remove' ;
START : 'start' ;
STOP : 'stop' ;
SET : 'set' ;
DETACH : 'detach' ;
REATTACH : 'reattach' ;
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
