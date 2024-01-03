package org.tjdx;

public enum TokenType {
    PROGRAM,BEGIN,END,CONST,VAR,WHILE,DO,IF ,THEN,    //保留字
    ID,INT,SPACE,NEWLINE,TABLE,EOF,    //标识符、整常数、转义符
    PLUS,SUB,MULTI,DIV,   //一元运算符
    ASSIGN,   //赋值符
    EQUAL,UNEQUAL,GREATER,GREQUAL,SMALLER,SMEQUAL,    //比较符
    LPAR,RPAR,SEMI,COMMA    //界符

}
