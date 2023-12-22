package org.tjdx;

import java.util.HashMap;

public class GrammarAnalysis {
    private LexAnalysis lexAnalysis;
    private Token token;    //现在分析的token

    private HashMap<String,Integer> symbolMap;   //0:程序名 1：常量 2：变量

    public GrammarAnalysis(String filePath) {
        this.lexAnalysis = LexAnalysis.getLexAnalysisInstance(filePath);
        this.token = null;
        this.symbolMap = new HashMap<>();
    }

    private void readToken(){
        this.token = this.lexAnalysis.analysis();
        while(this.token.getTokenType()== TokenType.SPACE||this.token.getTokenType()== TokenType.NEWLINE
                ||this.token.getTokenType()== TokenType.TABLE){
            this.token = this.lexAnalysis.analysis();
        }
    }

    /**
     * <程序>--→<程序⾸部> <分程序>
     * */
    public void startAnalysis(){
        readToken();
        programPreludeAnalysis();
        subProgramAnalysis();
        System.out.println("语法分析完毕！无语法错误");
    }
    /**
     * <程序⾸部>--→PROGRAM <标识符>
     * */
    private void programPreludeAnalysis() {
        if (token.getTokenType()==TokenType.PROGRAM){
            readToken();
        }
        else{
            throw new GrammarException(0, token.getLineNum(),token.getColumnNum());
        }
        if(token.getTokenType()== TokenType.ID){
            this.symbolMap.put(token.getTokenValue(), 0);
            readToken();
        }
        else{
            throw new GrammarException(1, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     * <分程序>--→[<常量说明>][<变量说明>]<语句>
     * */
    private void subProgramAnalysis(){
        constantDescription_optional_Analysis();
        variableDescription_optional_Analysis();
        statementAnalysis();
    }
    /**
     * [<常量说明>]--→<常量说明>|ε
     * */
    private void constantDescription_optional_Analysis(){
        if (token.getTokenType()== TokenType.CONST){
            constantDescriptionAnalysis();
        }
        else if(token.getTokenType()== TokenType.VAR||token.getTokenType()== TokenType.ID||
                token.getTokenType()== TokenType.IF||token.getTokenType()== TokenType.WHILE||
                token.getTokenType()== TokenType.BEGIN||token.getTokenType()== TokenType.EOF){
            return;
        }
        else{
            throw new GrammarException(2, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     * <常量说明>--→CONST <常量定义>{，<常量定义>};
     * */
    private void constantDescriptionAnalysis(){
        if (token.getTokenType()== TokenType.CONST){
            readToken();
        }
        else{
            throw new GrammarException(3, token.getLineNum(),token.getColumnNum());
        }
        constantDefinitionAnalysis();
        while(true){
            if (token.getTokenType()==TokenType.COMMA){
                readToken();
                constantDefinitionAnalysis();
            }
            else if(token.getTokenType()==TokenType.SEMI){
                readToken();
                break;
            }
            else{
                throw new GrammarException(6, token.getLineNum(),token.getColumnNum());
            }
        }
    }
    /**
     * <常量定义>--→<标识符>:=<⽆符号整数>
     * */
    private void constantDefinitionAnalysis(){
        if (token.getTokenType()==TokenType.ID){
            if (symbolMap.containsKey( token.getTokenValue())){
                throw new GrammarException(9, token.getLineNum(),token.getColumnNum());
            }
            else{
                this.symbolMap.put(token.getTokenValue(), 1);
                readToken();
            }
        }
        else{
            throw new GrammarException(1, token.getLineNum(),token.getColumnNum());
        }
        if(token.getTokenType()==TokenType.ASSIGN){
            readToken();
        }
        else{
            throw new GrammarException(4, token.getLineNum(),token.getColumnNum());
        }
        if(token.getTokenType()==TokenType.INT){
            readToken();
        }
        else{
            throw new GrammarException(5, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     * [<变量说明>]--→<变量说明>|ε
     * */
    private void variableDescription_optional_Analysis(){
        if(token.getTokenType()==TokenType.VAR){
            variableDescriptionAnalysis();
        }
        else if (token.getTokenType()== TokenType.ID||
                token.getTokenType()== TokenType.IF||token.getTokenType()== TokenType.WHILE||
                token.getTokenType()== TokenType.BEGIN||token.getTokenType()== TokenType.EOF){
            return;
        }
        else{
            throw new GrammarException(7, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     * <变量说明>--→VAR<标识符>{，<标识符>};
     * */
    private void variableDescriptionAnalysis(){
        if(token.getTokenType()==TokenType.VAR){
            readToken();
        }
        else{
            throw new GrammarException(8, token.getLineNum(),token.getColumnNum());
        }
        if(token.getTokenType()==TokenType.ID){
            if (symbolMap.containsKey( token.getTokenValue())){
                throw new GrammarException(9, token.getLineNum(),token.getColumnNum());
            }
            else{
                this.symbolMap.put(token.getTokenValue(), 2);
                readToken();
            }
        }
        else{
            throw new GrammarException(1, token.getLineNum(),token.getColumnNum());
        }
        while(true){
            if(token.getTokenType()==TokenType.COMMA){
                readToken();
                if(token.getTokenType()==TokenType.ID){
                    if (symbolMap.containsKey( token.getTokenValue())){
                        throw new GrammarException(9, token.getLineNum(),token.getColumnNum());
                    }
                    else{
                        this.symbolMap.put(token.getTokenValue(), 2);
                        readToken();
                    }
                }
                else{
                    throw new GrammarException(1, token.getLineNum(),token.getColumnNum());
                }
            }
            else if(token.getTokenType()==TokenType.SEMI){
                readToken();
                break;
            }
            else{
                throw new GrammarException(6, token.getLineNum(),token.getColumnNum());
            }
        }
    }
    /**
     * <语句>--→<赋值语句> | <条件语句 >| <循环语句> | <复合语句> | ε
     * */
    private void statementAnalysis(){
        if (token.getTokenType()==TokenType.ID){
            assignmentStatementAnalysis();
        }
        else if(token.getTokenType()==TokenType.IF){
            conditionStatementAnalysis();
        }
        else if(token.getTokenType()==TokenType.WHILE){
            loopStatementAnalysis();
        }
        else if(token.getTokenType()==TokenType.BEGIN){
            compoundStatementAnalysis();
        }
        else if(token.getTokenType()==TokenType.EOF||token.getTokenType()==TokenType.SEMI||token.getTokenType()==TokenType.END){
            return;
        }
        else{
            throw new GrammarException(10, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     * <赋值语句>--→<标识符>:=<表达式>
     * */
    private void assignmentStatementAnalysis(){
        if (token.getTokenType()==TokenType.ID){
            if(!symbolMap.containsKey(token.getTokenValue()) || symbolMap.get(token.getTokenValue())!=2){
                throw new GrammarException(11, token.getLineNum(),token.getColumnNum());
            }
            else{
                readToken();
            }
        }
        else{
            throw new GrammarException(1, token.getLineNum(),token.getColumnNum());
        }
        if(token.getTokenType()==TokenType.ASSIGN){
            readToken();
        }
        else{
            throw new GrammarException(4, token.getLineNum(),token.getColumnNum());
        }
        expressionAnalysis();
    }
    /**
     * <表达式>--→[+|-]<项> <表达式'>
     * */
    private void expressionAnalysis(){
        // [+|-] --→ +|-|ε
        if (token.getTokenType()==TokenType.PLUS){
            readToken();
        }
        else if(token.getTokenType()==TokenType.SUB){
            readToken();
        }
        else if(token.getTokenType()==TokenType.ID || token.getTokenType()==TokenType.INT||token.getTokenType()==TokenType.LPAR){
            if(token.getTokenType()==TokenType.ID && !symbolMap.containsKey(token.getTokenValue())){
                throw new GrammarException(11, token.getLineNum(),token.getColumnNum());
            }
        }
        else{
            throw new GrammarException(12, token.getLineNum(),token.getColumnNum());
        }
        itemAnalysis();
        expression_Analysis();
    }
    /**
     * <项> --→ <因⼦> <项'>
     * */
    private void itemAnalysis(){
        divisorAnalysis();
        item_Analysis();
    }
    /**
     * <因⼦>--→<标识符> |<⽆符号整数> | (<表达式>)
     * */
    private void divisorAnalysis(){
        if(token.getTokenType()==TokenType.ID){
            if(!symbolMap.containsKey(token.getTokenValue())){
                throw new GrammarException(11, token.getLineNum(),token.getColumnNum());
            }
            else{
                readToken();
            }
        }
        else if(token.getTokenType()==TokenType.INT){
            readToken();
        }
        else if(token.getTokenType()==TokenType.LPAR){
            readToken();
            expressionAnalysis();
            if (token.getTokenType()==TokenType.RPAR){
                readToken();
            }
            else{
                throw new GrammarException(13, token.getLineNum(),token.getColumnNum());
            }
        }
        else{
            throw new GrammarException(14, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     *  <项'> → <乘法运算符> <因⼦> <项'> |ε
     * */
    private void item_Analysis(){
        if(token.getTokenType()==TokenType.MULTI||token.getTokenType()==TokenType.DIV){
            readToken();
            divisorAnalysis();
            item_Analysis();
        }
        else if(token.getTokenType()==TokenType.PLUS||token.getTokenType()==TokenType.SUB||
                token.getTokenType()==TokenType.THEN||token.getTokenType()==TokenType.DO||
                token.getTokenType()==TokenType.EOF||token.getTokenType()==TokenType.SEMI||token.getTokenType()==TokenType.END||
                token.getTokenType()==TokenType.EQUAL||token.getTokenType()==TokenType.UNEQUAL||
                token.getTokenType()==TokenType.SMALLER||token.getTokenType()==TokenType.SMEQUAL||
                token.getTokenType()==TokenType.GREATER||token.getTokenType()==TokenType.GREQUAL||
                token.getTokenType()==TokenType.RPAR){
            return;
        }
        else{
            throw new GrammarException(15, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     * <表达式'>--→<加法运算符> <项> <表达式'>|ε
     * */
    private void expression_Analysis(){
        if(token.getTokenType()==TokenType.PLUS||token.getTokenType()==TokenType.SUB){
            readToken();
            itemAnalysis();
            expression_Analysis();
        }
        else if (
                token.getTokenType()==TokenType.THEN||token.getTokenType()==TokenType.DO||
                token.getTokenType()==TokenType.EOF||token.getTokenType()==TokenType.SEMI||token.getTokenType()==TokenType.END||
                token.getTokenType()==TokenType.EQUAL||token.getTokenType()==TokenType.UNEQUAL||
                token.getTokenType()==TokenType.SMALLER||token.getTokenType()==TokenType.SMEQUAL||
                token.getTokenType()==TokenType.GREATER||token.getTokenType()==TokenType.GREQUAL||
                token.getTokenType()==TokenType.RPAR){
            return;
        }
        else{
            throw new GrammarException(16, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     * <条件语句>--→IF <条件> THEN <语句>
     * */
    private void conditionStatementAnalysis(){
        if(token.getTokenType()==TokenType.IF){
            readToken();
        }
        else{
            throw new GrammarException(17, token.getLineNum(),token.getColumnNum());
        }
        conditionAnalysis();
        if(token.getTokenType()==TokenType.THEN){
            readToken();
        }
        else{
            throw new GrammarException(18, token.getLineNum(),token.getColumnNum());
        }
        statementAnalysis();
    }
    /**
     * <条件>→<表达式> <关系运算符> <表达式>
     * */
    private void conditionAnalysis(){
        expressionAnalysis();
        if (
            token.getTokenType()==TokenType.EQUAL||token.getTokenType()==TokenType.UNEQUAL||
            token.getTokenType()==TokenType.SMALLER||token.getTokenType()==TokenType.SMEQUAL||
            token.getTokenType()==TokenType.GREATER||token.getTokenType()==TokenType.GREQUAL){
            readToken();
        }
        else{
            throw new GrammarException(19, token.getLineNum(),token.getColumnNum());
        }
        expressionAnalysis();
    }
    /**
     * <循环语句>→WHILE <条件> DO <语句>
     * */
    private void loopStatementAnalysis(){
        if(token.getTokenType()==TokenType.WHILE){
            readToken();
        }
        else{
            throw new GrammarException(20, token.getLineNum(),token.getColumnNum());
        }
        conditionAnalysis();
        if(token.getTokenType()==TokenType.DO){
            readToken();
        }
        else{
            throw new GrammarException(21, token.getLineNum(),token.getColumnNum());
        }
        statementAnalysis();
    }
    /**
     * <复合语句>→BEGIN <语句>{; <语句>} END
     * */
    private void compoundStatementAnalysis(){
        if(token.getTokenType()==TokenType.BEGIN){
            readToken();
        }
        else{
            throw new GrammarException(22, token.getLineNum(),token.getColumnNum());
        }
        statementAnalysis();
        while(true){
            if (token.getTokenType()==TokenType.SEMI){
                readToken();
                statementAnalysis();
            }
            else if(token.getTokenType()==TokenType.END){
                readToken();
                break;
            }
            else{
                throw new GrammarException(23, token.getLineNum(),token.getColumnNum());
            }
        }
    }
}