grammar KevScript;

script
    : (basic_operation | function)+
    ;
basic_operation
    : add
    | set
    | attach
    | bind
    | detach
    | unbind
    | define
    ;
add
    : ADD identifier KEYVAL_SEPARATOR type
    ;
set
    : SET identifier ASSIGN_SEPARATOR assignable
    ;
attach
    : ATTACH identifier (identifier_list | identifier)
    ;
detach
    : DETACH identifier (identifier_list | identifier)
    ;
bind
    : BIND identifier (identifier_list | identifier)
    ;
unbind
    : UNBIND identifier (identifier_list | identifier)
    ;
define
    : DEFINE_TOKEN ID ASSIGN_SEPARATOR value
    ;

value :
    BLOCK_START (ID KEYVAL_SEPARATOR assignable)* BLOCK_END
    | ID
    | string
    ;

parameter: ID;
function
    : FUNCTION '(' (parameter (',' parameter)*)? ')' BLOCK_START basic_operation* BLOCK_END
    ;


identifier_list
    :  LIST_START identifier (LIST_SEP identifier)* LIST_END
    ;
assignable
    : string
    | identifier
    ;
string
    : SQ_STRING
    | DQ_STRING
    ; // TODO : escaping quotes in strings. keeping link break in text ok string values
basic_identifier
    : ID
    ;
identifier
    : (basic_identifier | BLOCK_START basic_identifier BLOCK_END) (VAR_SEP basic_identifier)*
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