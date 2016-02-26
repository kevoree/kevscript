grammar KevScript;

script
    : (basic_operation | function)+
    ;
basic_operation
    : add
    | remove
    | start
    | stop
    | set
    | attach
    | bind
    | detach
    | unbind
    | define
    | function_call
    | loop
    ;
add
    : ADD left_hand_identifiers KEYVAL_OP typeDef=type
    ;
remove
    : REMOVE left_hand_identifiers
    ;
start
    : START left_hand_identifiers
    ;
stop
    : STOP left_hand_identifiers
    ;
set
    : SET key=identifier ASSIGN_OP val=assignable
    ;
attach
    : ATTACH groupId=identifier nodes=identifier+
    ;
detach
    : DETACH groupId=identifier nodes=identifier+
    ;
bind
    : BIND chan=identifier nodes=identifier+
    ;
unbind
    : UNBIND chan=identifier nodes=identifier+
    ;
define
    : DEFINE_TOKEN varName=identifier ASSIGN_OP val=assignable
    ;
function_call
    : ID LBRACKET assignables? RBRACKET
    ;
loop
    : FOR LBRACKET index=basic_identifier COMMA val=basic_identifier IN LSQUARE_BRACKET assignables RSQUARE_BRACKET RBRACKET BLOCK_START basic_operation* BLOCK_END
    ;
object :
    BLOCK_START ((keyAndValue COMMA)* keyAndValue)? BLOCK_END
    ;
keyAndValue
    : identifier KEYVAL_OP assignable
    ;
function
    : FUNCTION functionName=identifier LBRACKET assignables? RBRACKET BLOCK_START basic_operation* BLOCK_END
    ;
assignable
    : string
    | identifier
    | object
    | BLOCK_START identifier BLOCK_END
    | assignable CONCAT assignable
    ;
assignables
    : (assignable COMMA)* assignable
    | assignable
    ;
string
    : SQ_STRING
    | DQ_STRING
    ;
basic_identifier
    : ID
    ;
left_hand_identifier
    : basic_identifier (DOT basic_identifier) ?
    | basic_identifier (DOT BLOCK_START basic_identifier BLOCK_END) ?
    | BLOCK_START basic_identifier BLOCK_END
    ;
left_hand_identifiers
    : (left_hand_identifier COMMA)* left_hand_identifier
    | left_hand_identifier
    ;
identifier
    : basic_identifier (DOT basic_identifier)*
    ;
type
    : (ID DOT)? ID (VERSION_SEP VERSION  (object|identifier)?)?
    ;
ASSIGN_OP
    : '='
    ;
KEYVAL_OP
    : ':'
    ;
LSQUARE_BRACKET
    : '['
    ;
RSQUARE_BRACKET
    : ']'
    ;
FOR
    : 'for'
    ;
IN
    : 'in'
    ;
COMMA
    : ','
    ;
BLOCK_START
    : '{'
    ;
BLOCK_END
    : '}'
    ;
DOT
    : '.'
    ;
VERSION_SEP
    : '/'
    ;
ADD
    : 'add'
    ;
REMOVE
    : 'remove'
    ;
START
    : 'start'
    ;
STOP
    : 'stop'
    ;
SET
    : 'set'
    ;
DETACH
    : 'detach'
    ;
ATTACH
    : 'attach'
    ;
BIND
    : 'bind'
    ;
UNBIND
    : 'unbind'
    ;
DEFINE_TOKEN
    : 'let'
    ;
FUNCTION
    : 'function'
    ;
ID
    : [a-zA-Z_][a-zA-Z0-9_-]*
    ;
VERSION
    : NUMERIC_VALUE
    ;
SQ_STRING
    : '\'' .*? '\''
    ;
DQ_STRING
    : '"' .*? '"'
    ;
COMMENT
    : '/*' .*? '*/' -> skip
    ;
SINGLELINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;
RBRACKET
    : ')'
    ;
LBRACKET
    : '('
    ;
CONCAT
    : '+'
    ;
NUMERIC_VALUE
    : [0-9]+
    ;
WS
    : [ \t\r\n]+ -> skip
    ;