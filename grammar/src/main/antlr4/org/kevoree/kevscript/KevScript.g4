grammar KevScript;

script
    : (basic_operation | function_operation)*
    ;
basic_operation
    : add
    | attach
    | start
    | detach
    | stop
    | set
    | bind
    | unbind
    | let_operation
    | function_call
    | for_loop
    | netinit
    | netmerge
    | netremove
    | remove
    ;
add
    : ADD list_add_members=left_add_definitions KEYVAL_OP typeDef=type
    ;
left_add_definitions
    : (left_add_definition COMMA)* left_add_definition
    | left_add_definition
    ;
left_add_definition
    : (short_identifier|AT assignable) (DOT (short_identifier|AT assignable))?
    ;
special_internal_operation
    : AT short_identifier LBRACKET RBRACKET
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
    : SET key=long_identifier (SLASH frag=short_identifier)? ASSIGN_OP val=assignable
    ;
attach
    : ATTACH groupId=long_identifier nodes=long_identifier+
    ;
detach
    : DETACH groupId=long_identifier nodes=long_identifier+
    ;
bind
    : BIND chan=long_identifier nodes=long_identifier+
    ;
unbind
    : UNBIND chan=long_identifier nodes=long_identifier+
    ;
let_operation
    : DEFINE_TOKEN varName=long_identifier ASSIGN_OP val=assignable
    ;
function_call
    : ID LBRACKET assignables? RBRACKET
    ;
for_loop
    : FOR LBRACKET (index=short_identifier COMMA)? val=short_identifier IN LSQUARE_BRACKET assignables RSQUARE_BRACKET RBRACKET BLOCK_START basic_operation* BLOCK_END
    ;
netinit
    : NETINIT short_identifier object
    ;
netmerge
    : NETMERGE short_identifier object
    ;
netremove
    : NETREMOVE short_identifier long_identifier+
    ;
object :
    BLOCK_START ((keyAndValue COMMA)* keyAndValue)? BLOCK_END
    ;
keyAndValue
    : long_identifier KEYVAL_OP assignable
    ;
function_operation
    : FUNCTION functionName=long_identifier LBRACKET assignables? RBRACKET BLOCK_START basic_operation* ('return' assignable)? BLOCK_END
    ;
assignable
    : string
    | long_identifier
    | AT assignable
    | object
    | BLOCK_START long_identifier BLOCK_END
    | assignable CONCAT assignable
    | special_internal_operation
    | function_call
    | array
    | assignable LSQUARE_BRACKET NUMERIC_VALUE RSQUARE_BRACKET (DOT assignable )?
    ;
array
    : LSQUARE_BRACKET assignables? RSQUARE_BRACKET
    ;
assignables
    : (assignable COMMA)* assignable
    | assignable
    ;
string
    : SQ_STRING
    | DQ_STRING
    ;
short_identifier
    : ID
    ;
left_hand_identifier
    : short_identifier (DOT short_identifier) ?
    | short_identifier (DOT BLOCK_START short_identifier BLOCK_END) ?
    | BLOCK_START short_identifier BLOCK_END
    ;
left_hand_identifiers
    : (left_hand_identifier COMMA)* left_hand_identifier
    | left_hand_identifier
    ;
long_identifier
    : (short_identifier|AT assignable) (DOT (short_identifier|AT assignable))*
    ;
type
    : (ID DOT)? ID (SLASH (NUMERIC_VALUE|long_identifier) (object|long_identifier)?)?
    ;

ASSIGN_OP : '=' ;
KEYVAL_OP : ':' ;
COMMA : ',' ;
DOT : '.' ;
CONCAT : '+' ;
AT : '@' ;
SLASH : '/' ;
LSQUARE_BRACKET : '[' ;
RSQUARE_BRACKET : ']' ;
BLOCK_START : '{' ;
BLOCK_END : '}' ;
RBRACKET : ')' ;
LBRACKET : '(' ;
FOR : 'for' ;
IN : 'in' ;
ADD : 'add' ;
REMOVE : 'remove' ;
START : 'start' ;
STOP : 'stop' ;
SET : 'set' ;
DETACH : 'detach' ;
ATTACH : 'attach' ;
BIND : 'bind' ;
UNBIND : 'unbind' ;
DEFINE_TOKEN : 'let' ;
FUNCTION : 'function' ;
NETINIT : 'net-init' ;
NETMERGE : 'net-merge' ;
NETREMOVE : 'net-remove' ;
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
NUMERIC_VALUE
    : [0-9]+
    ;
ID
    : [a-zA-Z_][a-zA-Z0-9_-]*
    ;
WS
    : [ \t\r\n]+ -> skip
    ;