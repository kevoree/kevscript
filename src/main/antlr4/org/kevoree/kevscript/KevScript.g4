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
    | function_call
    ;
add
    : ADD root_identifiers KEYVAL_SEPARATOR type
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
function_call
    : ID '(' parameters ')'
    ;

parameters
    : ((basic_identifier | string) ',') * (basic_identifier | string)
    | (basic_identifier | string)
    ;

value :
    BLOCK_START (ID KEYVAL_SEPARATOR assignable)* BLOCK_END
    | ID
    | string
    ;

parameter: ID;
function
    : FUNCTION ID '(' (parameter (',' parameter)*)? ')' BLOCK_START basic_operation* BLOCK_END
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
    ;
string
    : SQ_STRING
    | DQ_STRING
    ;
basic_identifier
    : ID
    ;
root_identifier
    : basic_identifier
    | (basic_identifier | BLOCK_START basic_identifier BLOCK_END)
    ;
root_identifiers
    : (root_identifier ',')* root_identifier
    | root_identifier
    ;
identifier
    : root_identifier (VAR_SEP basic_identifier)*
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