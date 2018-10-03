grammar FunLang;

file
    : mainBlock = block EOF
    ;

block
    : (statement)*
    ;

blockWithBraces
    : '{' body=block '}'
    ;

statement
    : function
    | variable
    | expression
    | whileLoop
    | ifStatement
    | assignment
    | returnStatement
    ;

function
    : 'fun' name=IDENTIFIER '(' parameters=parameterNames ')' blockWithBraces
    ;

IDENTIFIER
    : (LETTER | '_')(LETTER | '_' | DIGIT)*
    ;

parameterNames
    : ((IDENTIFIER ',')* IDENTIFIER)?
    ;

variable
    : 'var' name=IDENTIFIER ('=' value = expression)?
    ;

expression
    : functionCall | IDENTIFIER | LITERAL | '(' inBraces = expression ')'
    | left = expression
      operator = ('*'|'/'|'%')
      right = expression
    | left = expression
      operator = ('+'|'-')
      right = expression
    | left = expression
      operator = ('>'|'<'|'>='|'<=')
      right = expression
    | left = expression
      operator = ('=='|'!=')
      right = expression
    | left = expression
      operator = '&&'
      right = expression
    | left = expression
      operator = '||'
      right = expression
    ;

functionCall
    : name = IDENTIFIER '(' functionArguments = arguments ')'
    ;

arguments
    : ((expression ',')* expression)?
    ;

whileLoop
    : 'while' '(' condition = expression ')' blockWithBraces
    ;

ifStatement
    : 'if' '(' condition = expression ')' ifBody = blockWithBraces ('else' elseBody = blockWithBraces)?
    ;

assignment
    : name = IDENTIFIER '=' value = expression
    ;

returnStatement
    : 'return' expression
    ;

LITERAL
    : ('+'|'-')? [1-9] DIGIT* | '0'
    ;

fragment LETTER
    : [A-Za-z]
    ;

fragment DIGIT
    : [0-9]
    ;

WS: (' ' | '\t' | '\r'| '\n' | '//' (.)*? '\n') -> skip;