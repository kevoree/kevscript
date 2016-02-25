grammar KevScript;

// entry point
kevscript
    :
    (   addStmt
    |   setStmt
    |   repoStmt
    |   attachStmt
    |   detachStmt
    |   bindStmt
    |   unbindStmt
    |   moveStmt
    |   removeStmt
    |   networkStmt
    |   includeStmt
    |   startStmt
    |   stopStmt
    |   pauseStmt
    )* COMMENT* EOF
    ;

// statement rules
addStmt
    :   ADD declList ':' ID ('.' ID)* ('/' SEMVER)?
    ;
setStmt
    :   SET ID '.' ID ('.' ID)? ('/' identifier)? '=' (SQ_STRING | DQ_STRING)
    ;
repoStmt
    :   REPO ADDRESS
    ;
attachStmt
    :   ATTACH instanceList identifier
    ;
detachStmt
    :   DETACH instanceList identifier
    ;
bindStmt
    :   BIND triplePath identifier
    ;
unbindStmt
    :   UNBIND triplePath identifier
    ;
moveStmt
    :   MOVE instanceList identifier
    ;
removeStmt
    :   REMOVE instanceList
    ;
networkStmt
    :   NETWORK triplePath
    ;
includeStmt
    :   INCLUDE ID ':'  (':' STRING_2)? (':' SEMVER)?
    ;
startStmt
    :   START instanceList
    ;
stopStmt
    :   STOP instanceList
    ;
pauseStmt
    :   PAUSE instanceList
    ;

// shared rules
identifier
    :   ID
    |   '*'
    ;
instanceList
    :   instance (',' instance)*
    ;
declList
    :   declInstance (',' declInstance)*
    ;
instance
    :   identifier ('.' identifier)*
    ;
declInstance
    :   ID ('.' ID)*
    ;
triplePath
    :   ID '.' ID '.' ID;

// KevScript tokens
ADD:     'add';
SET:     'set';
REPO:    'repo';
ATTACH:  'attach';
DETACH:  'detach';
BIND:    'bind';
UNBIND:  'unbind';
MOVE:    'move';
REMOVE:  'remove';
NETWORK: 'network';
INCLUDE: 'include';
START:   'start';
STOP:    'stop';
PAUSE:   'pause';

SEMVER
    :   ('0'|[1-9][0-9]*)'.'('0'|[1-9][0-9]*)'.'('0'|[1-9][0-9]*)('-'[0-9a-zA-Z\-]+('.'[0-9a-zA-Z\-]+)*)?('+'[0-9A-Za-z\-]+('.'[0-9a-zA-Z\-]+)*)?;
ID
    :   [a-zA-Z][a-zA-Z0-9_]+;
ADDRESS
    :   ('"' ~[\r\n]* '"')|('\'' ~[\r\n]* '\'');
fragment STRING_2
    :   [a-zA-Z0-9.%@_-]+;
SQ_STRING
    :   '\'' .*? '\'';
DQ_STRING
    :   '"' .*? '"';
WS
    :   [ \t\r\n]+ -> skip;
COMMENT
    :   [ \t]* '//' ~[\r\n]* -> skip;