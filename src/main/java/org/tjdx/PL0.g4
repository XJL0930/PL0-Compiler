grammar PL0;

// <程序>→<程序⾸部> <分程序>
program: programHeader subProgram EOF;
// <程序⾸部>→PROGRAM <标识符>
programHeader: 'PROGRAM' identifier;
// <分程序>→[<常量说明>][<变量说明>]<语句>（注：[ ]中的项表⽰可选）
subProgram: constantDeclaration? variableDeclaration? statement;

//<常量说明>→CONST <常量定义>{，<常量定义>} ; （注： { }中的项表⽰可重复若⼲次）
constantDeclaration: 'CONST' constantDefinition (',' constantDefinition)* ';' ;
//<常量定义>→<标识符>:=<⽆符号整数>
constantDefinition: identifier ':=' unsignedInt;
//<变量说明>→VAR<标识符>{，<标识符>};
variableDeclaration: 'VAR' identifier (',' identifier)* ';';

//<标识符>→<字⺟>{<字⺟> | <数字>}
identifier: LETTER (LETTER | DIGIT)*;
//<⽆符号整数>→<数字>{<数字>}
unsignedInt: DIGIT+;
//<字⺟>→a | b …| x | y | z
LETTER: [a-zA-Z];
//<数字>→0 | 1 | … | 8| 9
DIGIT: [0-9];

//<语句>→<赋值语句> | <条件语句 >| <循环语句> | <复合语句> | <空语句>
statement: assignmentStatement | ifStatement | whileStatement | compoundStatement | emptyStatement;
//<赋值语句>→<标识符>:=<表达式>
assignmentStatement: identifier ':=' expression;
//<条件语句>→IF <条件> THEN <语句>
ifStatement: 'IF' condition 'THEN' statement;
//<循环语句>→WHILE <条件> DO <语句>
whileStatement: 'WHILE' condition 'DO' statement;
//<复合语句>→BEGIN <语句>{; <语句>} END
compoundStatement: 'BEGIN' statement (';' statement)* 'END';
//<空语句>
emptyStatement: ;

//<表达式>→[+|-]项 | <表达式> <加法运算符> <项>
expression: ('+' | '-')? term | expression additionOperator term;
//<项>→<因⼦> | <项><乘法运算符> <因⼦>
term: factor | term multiplicationOperator factor;
//<因⼦>→<标识符> |<⽆符号整数> | (<表达式>)
factor: identifier | unsignedInt | '(' expression ')';
//<条件>→<表达式> <关系运算符> <表达式>
condition: expression relationOperator expression;

//<加法运算符>→ + | -
additionOperator: '+' | '-';
//<乘法运算符>→ * | /
multiplicationOperator: '*' | '/';
//<关系运算符>→ = | <> | < | <= | > | >=
relationOperator: '=' | '<>' | '<' | '<=' | '>' | '>=';

//空格 回车 制表符
EMPTY: [ \t\r\n]+ -> skip;
