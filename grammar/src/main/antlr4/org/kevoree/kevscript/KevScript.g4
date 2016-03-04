grammar KevScript;

script
    : (basic_operation | function_operation)*
    ;
basic_operation
    : add           // makes reference to instances
    | remove        // makes reference to instances
    | attach        // makes reference to nodes and groups
    | detach        // makes reference to nodes and groupes
    | start         // makes reference to instances
    | stop          // makes reference to instances
    | set           // makes reference to instances
    | bind          // makes reference to instances and channels
    | unbind        // makes reference to instances and channels
    | netinit       // makes reference to a node
    | netmerge      // makes reference to a node
    | netremove     // makes reference to a node
    | metainit      // makes reference to a node
    | metamerge     // makes reference to a node
    | metaremove    // makes reference to a node
    | for_loop      // loop over an array of (objects, array, string, and instances)
    | let_operation // defined values (objects/array/strings/functions)
    | function_call // execute a function + might return a value (takes objects, arrays, string, functions and instances in parameters)
    ;
add
    : ADD list_add_members=left_add_definitions KEYVAL_OP typeDef=type
    ;
left_add_definitions
    : (members+=left_add_definition COMMA)* members+=left_add_definition
    | members+=left_add_definition
    ;
left_add_definition
    : long_identifier_chunk (DOT long_identifier_chunk)?
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
    : ATTACH groupId=long_identifier nodes=long_identifiers
    ;
detach
    : DETACH groupId=long_identifier nodes=long_identifiers
    ;
long_identifiers
    : long_identifier+
    ;
bind
    : BIND chan=long_identifier nodes=long_identifiers
    ;
unbind
    : UNBIND chan=long_identifier nodes=long_identifiers
    ;
let_operation
    : DEFINE_TOKEN varName=long_identifier ASSIGN_OP val=assignable
    ;
function_call
    : ID LBRACKET parameters=assignables? RBRACKET
    ;
for_loop
    : FOR LBRACKET (index=short_identifier COMMA)? val=short_identifier IN LSQUARE_BRACKET iterator=assignables RSQUARE_BRACKET RBRACKET BLOCK_START for_body BLOCK_END
    ;
for_body : basic_operation* ;
netinit
    : NETINIT short_identifier object
    ;
netmerge
    : NETMERGE short_identifier object
    ;
netremove
    : NETREMOVE short_identifier long_identifiers
    ;
metainit
    : METAINIT short_identifier object
    ;
metamerge
    : METAMERGE short_identifier object
    ;
metaremove
    : METAREMOVE short_identifier long_identifiers
    ;
object :
    BLOCK_START (values+=keyAndValue COMMA)* values+=keyAndValue BLOCK_END
    | BLOCK_START BLOCK_END
    ;

keyAndValue
    : key=short_identifier KEYVAL_OP value=assignable
    ;
function_operation
    : FUNCTION functionName=long_identifier LBRACKET parametersNames=assignables? RBRACKET BLOCK_START function_body (RETURN assignable)? BLOCK_END
    ;

function_body : basic_operation* ;
assignable
    : string                                                                        // a raw string
    | long_identifier                                                               // a reference to a ressource
    | dereference                                                                   // a reference to the value of a ressource
    | object                                                                        // a object declaration
    | context=BLOCK_START long_identifier BLOCK_END                                 // an external context reference
    | concat = assignable CONCAT assignable                                         // a concatenation of two assignables values
    | special_internal_operation                                                    // a call to an internal special function
    | array                                                                         // a list of values declaration
    | assignable LSQUARE_BRACKET NUMERIC_VALUE RSQUARE_BRACKET (DOT assignable )?   // an array index resolution
    | function_call                                                                 // a function call
    ;
dereference : AT assignable ;
array
    : LSQUARE_BRACKET assignables RSQUARE_BRACKET
    | LSQUARE_BRACKET RSQUARE_BRACKET
    ;
assignables
    : (assignable COMMA)* assignable
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
    : identifiers+=long_identifier_chunk (DOT identifiers+=long_identifier_chunk)*
    ;
long_identifier_chunk
    : short_identifier
    | dereference
    ;
type
    : (ID DOT)? ID (SLASH (NUMERIC_VALUE|long_identifier) (object|long_identifier)?)?
    ;
string
    : value=STRING
    ;

RETURN : 'return' ;
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
STRING : '"' ~["]* '"';
WS
    : [ \t\r\n]+ -> skip
    ;