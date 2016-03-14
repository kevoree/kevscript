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
    | varDecl
    | funcDecl
    | forDecl
    | funcCall
    ;
instance
    : INSTANCE varName=ID ASSIGN (instanceName=expression?) type
    | INSTANCE varNames=varIdentifierList ASSIGN type
    ;
add
    : ADD identifier instanceList
    | ADD LS_BRACKET identifierList RS_BRACKET
    | ADD instancePath
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
    : ATTACH groupId=identifier nodes=instanceList
    ;
detach
    : DETACH groupId=identifier nodes=instanceList
    ;
bind
    : BIND chan=identifier nodes=portList
    ;
unbind
    : UNBIND chan=identifier nodes=portList
    ;
varDecl
    : LET varIdentifierList ASSIGN val=expression
    ;
netinit
    : NETINIT identifier (objectDecl|identifier)
    ;
netmerge
    : NETMERGE identifier (objectDecl|identifier)
    ;
netremove
    : NETREMOVE identifier identifierList
    ;
metainit
    : METAINIT identifier (objectDecl|identifier)
    ;
metamerge
    : METAMERGE identifier (objectDecl|identifier)
    ;
metaremove
    : METAREMOVE identifier identifierList
    ;
varIdentifierList
    : ID (COMMA ID)*
    ;
forDecl
    : FOR L_BRACKET (index=ID COMMA)? val=ID IN iterable R_BRACKET LC_BRACKET forBody RC_BRACKET
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
    : key=ID COLON value=expression
    ;
arrayDecl
    : LS_BRACKET expressionList? RS_BRACKET
    ;
funcCall
    : ID L_BRACKET parameters=expressionList? R_BRACKET
    ;
funcDecl
    : FUNCTION functionName=ID L_BRACKET parameters=varIdentifierList? R_BRACKET LC_BRACKET funcBody RC_BRACKET
    ;
funcBody
    : (statement*) returnStatement?
    ;
returnStatement
    : RETURN expression
    ;
expression
    : string                            // a raw string
    | objectDecl                       // a object declaration
    | contextIdentifier                // a context reference
    | concat = string (CONCAT string)+  // a concatenation of string values
    | arrayDecl                        // a list of values declaration
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
    : ID LS_BRACKET NUMERIC_VALUE RS_BRACKET
    ;
contextIdentifier
    : ID
    | ID contextIdentifier
    | ID DOT contextRef
    | ID DOT contextIdentifier
    | AMPERSAND contextIdentifier // we might regret this one for interpretation :D
    | arrayAccess
    | arrayAccess DOT contextIdentifier
    | arrayAccess DOT contextRef
    ;
contextRef
    : AMPERSAND contextIdentifier
    ;
identifier
    : ID
    | ID DOT identifier
    | contextRef
    | funcCall
    | funcCall DOT identifier
    | arrayAccess
    | arrayAccess DOT identifier
    ;
identifierList
    : identifiers+=identifier (COMMA identifiers+=identifier)*
    ;
instancePath
    : identifier (COLON identifier)*
    ;
portPath
    : (instancePath (LEFT_LIGHT_ARROW|RIGHT_LIGHT_ARROW))? identifier ;
dictionaryPath
    : key=instancePath '#' identifier (SLASH frag=identifier)? ;
instanceList
    : instances+=instancePath
    | LS_BRACKET instances+=instancePath (COMMA instances+=instancePath)* RS_BRACKET
    ;
portList
    : instances+=portPath
    | LS_BRACKET instances+=portPath (COMMA instances+=portPath)* RS_BRACKET
    ;
type
    : typeName version? duVersions?
    ;
typeName
    : (ID DOT)? ID
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
REMOVE : 'remove' ;
START : 'start' ;
STOP : 'stop' ;
SET : 'set' ;
DETACH : 'detach' ;
ATTACH : 'attach' ;
BIND : 'bind' ;
UNBIND : 'unbind' ;
LET : 'let' ;
FUNCTION : 'function' ;
NETINIT : 'net-init' ;
NETMERGE : 'net-merge' ;
NETREMOVE : 'net-remove' ;
METAINIT : 'meta-init' ;
METAMERGE : 'meta-merge' ;
METAREMOVE : 'meta-remove' ;

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
