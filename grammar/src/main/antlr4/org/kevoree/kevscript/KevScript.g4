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
    | let
    | func_decl
    | for_decl
    | func_call
    ;
instance
    : INSTANCE varName=ID ASSIGN (instanceName=expression?) type
    | INSTANCE varNames=var_identifier_list ASSIGN type
    ;
add
    : ADD identifier instance_list
    | ADD LS_BRACKET identifier_list RS_BRACKET
    ;
remove
    : REMOVE instance_list
    ;
start
    : START instance_list
    ;
stop
    : STOP instance_list
    ;
set
    : SET key=instance_path (SLASH frag=instance_path)? ASSIGN val=expression
    ;
attach
    : ATTACH groupId=identifier nodes=instance_list
    ;
detach
    : DETACH groupId=identifier nodes=instance_list
    ;
bind
    : BIND chan=identifier nodes=instance_list
    ;
unbind
    : UNBIND chan=identifier nodes=instance_list
    ;
let
    : LET var_identifier_list ASSIGN val=expression
    ;
netinit
    : NETINIT identifier (object_decl|identifier)
    ;
netmerge
    : NETMERGE identifier (object_decl|identifier)
    ;
netremove
    : NETREMOVE identifier identifier_list
    ;
metainit
    : METAINIT identifier (object_decl|identifier)
    ;
metamerge
    : METAMERGE identifier (object_decl|identifier)
    ;
metaremove
    : METAREMOVE identifier identifier_list
    ;
var_identifier_list
    : ID (COMMA ID)*
    ;
for_decl
    : FOR L_BRACKET (index=ID COMMA)? val=ID IN iterable R_BRACKET LC_BRACKET for_body RC_BRACKET
    ;
iterable
    : array_decl
    | identifier
    | context_identifier
    ;
for_body
    : statement*
    ;
object_decl
    : LC_BRACKET (values+=key_and_value (COMMA values+=key_and_value)*)? RC_BRACKET
    ;
key_and_value
    : key=ID COLON value=expression
    ;
array_decl
    : LS_BRACKET expression_list? RS_BRACKET
    ;
func_call
    : ID L_BRACKET parameters=expression_list? R_BRACKET
    ;
func_decl
    : FUNCTION functionName=ID L_BRACKET parameters=var_identifier_list? R_BRACKET LC_BRACKET func_body RC_BRACKET
    ;
func_body
    : (statement*) returnStatement?
    ;
returnStatement
    : RETURN expression
    ;
expression
    : string                            // a raw string
    | object_decl                       // a object declaration
    | context_identifier                // a context reference
    | concat = string (CONCAT string)+  // a concatenation of string values
    | array_decl                        // a list of values declaration
    | array_access
    | identifier
    | func_call
    ;
expression_list
    : expression (COMMA expression)*
    ;
array_access
    : ID LS_BRACKET NUMERIC_VALUE RS_BRACKET
    ;
context_identifier
    : ID
    | ID context_identifier
    | ID DOT context_ref
    | ID DOT context_identifier
    | AMPERSAND context_identifier // we might regret this one for interpretation :D
    | array_access
    | array_access DOT context_identifier
    | array_access DOT context_ref
    ;
context_ref
    : AMPERSAND context_identifier
    ;
identifier
    : ID
    | ID DOT identifier
    | context_ref
    | func_call
    | func_call DOT identifier
    | array_access
    | array_access DOT identifier
    ;
identifier_list
    : identifiers+=identifier (COMMA identifiers+=identifier)*
    ;
instance_path
    : identifier (COLON identifier)*
    ;
instance_list
    : instances+=instance_path
    | LS_BRACKET instances+=instance_path (COMMA instances+=instance_path)* RS_BRACKET
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
    : object_decl
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
    : [a-zA-Z_][a-zA-Z0-9_-]*
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