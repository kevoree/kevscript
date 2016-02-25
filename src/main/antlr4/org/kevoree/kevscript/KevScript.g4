grammar KevScript;

/**
 * add package on TD
 * dealing with DU ?
 * network conf ?
 * how to deal with repository configuration (maven, npm, nuget private repo & co) ?
 * allowing list definition in let elements ?
*/

script : (add|set|attach|bind|detach|unbind|define)+ ;
add : ADD identifier KEYVAL_SEPARATOR type ;
set : SET identifier ASSIGN_SEPARATOR assignable ;
attach : ATTACH identifier (identifier_list | identifier) ;
detach : DETACH identifier (identifier_list | identifier) ;
bind : BIND identifier (identifier_list | identifier) ;
unbind : UNBIND identifier (identifier_list | identifier) ;
define : DEFINE_TOKEN ID BLOCK_START (ID KEYVAL_SEPARATOR assignable)* BLOCK_END ;

identifier_list :  LIST_START identifier (LIST_SEP identifier)* LIST_END ;
assignable : string | identifier;
string : SQ_STRING | DQ_STRING ; // TODO : escaping quotes in strings. keeping link break in text ok string values
identifier : (ID | BLOCK_START ID BLOCK_END | GENERATED_START ID) (VAR_SEP ID)* ;
type : ID (VERSION_SEP VERSION(VERSION_SEP VERSION)?)?;

ASSIGN_SEPARATOR: '=' ;
KEYVAL_SEPARATOR : ':' ;
LIST_START : '[' ;
LIST_SEP : ',' ;
LIST_END : ']' ;
BLOCK_START : '{' ;
BLOCK_END : '}' ;
VAR_SEP : '.' ;
GENERATED_START : '$' ;
VERSION_SEP : '/' ;
ADD : 'add';
SET : 'set' ;
DETACH : 'detach' ;
ATTACH : 'attach' ;
BIND : 'bind' ;
UNBIND : 'unbind' ;
DEFINE_TOKEN : 'let' ;
ID : [a-zA-Z_][a-zA-Z0-9_-]* ;
VERSION : [0-9]+('.'[0-9]+('.'[0-9]+)?)? ;
SQ_STRING : '\'' .*? '\'';
DQ_STRING : '"' .*? '"';
STRING  : DQUOTE ( STR_TEXT | EOL )* DQUOTE ;
fragment STR_TEXT: ( ~["\r\n\\] | ESC_SEQ )+ ;
fragment ESC_SEQ : '\\' ( [btf"\\] | EOF ) ;
fragment DQUOTE  : '"' ;
fragment EOL     : '\r'? '\n' ;
WS : [ \t\r\n]+ -> skip;