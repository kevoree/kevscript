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
    : ADD left_hand_identifiers KEYVAL_SEPARATOR typeDef=type
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
    : SET key=identifier ASSIGN_SEPARATOR val=assignable
    ;
attach
    : ATTACH groupId=identifier (nodes=identifier_list | node=identifier)
    ;
detach
    : DETACH groupId=identifier (nodes=identifier_list | node=identifier)
    ;
bind
    : BIND chan=identifier (ports=identifier_list | port=identifier)
    ;
unbind
    : UNBIND chan=identifier (ports=identifier_list | port=identifier)
    ;
define
    : DEFINE_TOKEN varName=identifier ASSIGN_SEPARATOR val=assignable
    ;
function_call
    : ID '(' assignables ')'
    ;
loop
    : 'for' '(' index=identifier ',' val=identifier ')' 'in'
    '[' assignables ']'
    '{' basic_operation* '}'
    ;

object :
    BLOCK_START (keyAndValue ',')* keyAndValue BLOCK_END
    ;
keyAndValue
    : identifier KEYVAL_SEPARATOR assignable
    ;

parameter
    : ID;
function
    : FUNCTION functionName=identifier '(' (parameter (',' parameter)*)? ')' BLOCK_START basic_operation* BLOCK_END
    ;

identifier_list
    :  LIST_START identifiers LIST_END
    ;
identifiers
    : identifier (LIST_SEP identifier)*
    ;
assignable
    : string
    | identifier
    | object
    | BLOCK_START identifier BLOCK_END
    | assignable '+' assignable
    ;
assignables
    : (assignable ',')* assignable
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
    : basic_identifier ('.' basic_identifier) ?
    | basic_identifier ('.' BLOCK_START basic_identifier BLOCK_END) ?
    | BLOCK_START basic_identifier BLOCK_END
    ;
left_hand_identifiers
    : (left_hand_identifier ',')* left_hand_identifier
    | left_hand_identifier
    ;
identifier
    : basic_identifier (VAR_SEP basic_identifier)*
    ;
type
    : (ID '.')*ID (VERSION_SEP VERSION(VERSION_SEP VERSION)?)?
    ;

ASSIGN_SEPARATOR
    : '='
    ;
KEYVAL_SEPARATOR
    : ':'
    ;
LIST_START
    : '['
    ;
LIST_SEP
    : ','
    ;
LIST_END
    : ']'
    ;
BLOCK_START
    : '{'
    ;
BLOCK_END
    : '}'
    ;
VAR_SEP
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
    : [0-9]+('.'[0-9]+('.'[0-9]+)?)?
    ;
SQ_STRING
    : '\'' .*? '\''
    ;
DQ_STRING
    : '"' .*? '"'
    ;
WS
    : [ \t\r\n]+ -> skip
    ;