package org.tjdx;

import java.util.HashMap;
import java.util.Stack;

public class GrammarAnalysis {
    private int number;// 临时变量标号
    private LexAnalysis lexAnalysis;
    private Token token;    //现在分析的token
    Stack<Symbol> symbols; //建立一个符号栈，方便对表达式进行规约
    public MidCodeSet midCodeSet; //中间代码集

    private HashMap<String,Integer> symbolMap;   //0:程序名 1：常量 2：变量

    private enum ExpressionType{
        assignment,condition,loop,compound
    } // 枚举类，表示表达式类型

    public GrammarAnalysis(String filePath) {
        this.lexAnalysis = LexAnalysis.getLexAnalysisInstance(filePath);
        this.token = null;
        this.symbolMap = new HashMap<>();
        this.midCodeSet=new MidCodeSet();
        this.number=0;
        symbols=new Stack<>();
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
        statementAnalysis(0);
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
    private void statementAnalysis(int layer){
        if (token.getTokenType()==TokenType.ID){
            assignmentStatementAnalysis(layer);
        }
        else if(token.getTokenType()==TokenType.IF){
            conditionStatementAnalysis(layer);
        }
        else if(token.getTokenType()==TokenType.WHILE){
            loopStatementAnalysis(layer);
        }
        else if(token.getTokenType()==TokenType.BEGIN){
            compoundStatementAnalysis(layer);
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
    private void assignmentStatementAnalysis(int layer){
        ExpressionType expressionType=ExpressionType.assignment;
        if (token.getTokenType()==TokenType.ID){
            if(!symbolMap.containsKey(token.getTokenValue()) || symbolMap.get(token.getTokenValue())!=2){
                throw new GrammarException(11, token.getLineNum(),token.getColumnNum());
            }
            else{
                symbols.push(new Symbol(layer,token.getTokenValue()));
                readToken();
            }
        }
        else{
            throw new GrammarException(1, token.getLineNum(),token.getColumnNum());
        }
        if(token.getTokenType()==TokenType.ASSIGN){
            symbols.push(new Symbol(layer,":="));
            readToken();
        }
        else{
            throw new GrammarException(4, token.getLineNum(),token.getColumnNum());
        }

        expressionAnalysis(expressionType,layer+1);

        /**
         * 进行赋值语句的规约，生成中间代码
         */
        StatuteAssign(layer);
    }
    /**
     * <表达式>--→[+|-]<项> <表达式'>
     * */
    private void expressionAnalysis(ExpressionType expressionType,int layer){
        boolean flag=false;
        TokenType type=token.getTokenType();
        // [+|-] --→ +|-|ε
        if (type==TokenType.PLUS){
            readToken();
        }
        else if(type==TokenType.SUB){
            if (expressionType==ExpressionType.assignment){
                symbols.push(new Symbol(layer,"uminus"));
                flag=true;
            }
            readToken();
        }
        else if(type==TokenType.ID || type==TokenType.INT||type==TokenType.LPAR){
            if(type==TokenType.ID && !symbolMap.containsKey(token.getTokenValue())){
                throw new GrammarException(11, token.getLineNum(),token.getColumnNum());
            }
        }
        else{
            throw new GrammarException(12, token.getLineNum(),token.getColumnNum());
        }
        itemAnalysis(expressionType,layer);

        /**
         * 对取负数操作进行特殊处理，在处理+、-之前对其进行规约和生成中间代码
         */
        if(flag){
            StatuteUminus(layer);
        }

        expression_Analysis(expressionType,layer);

    }
    /**
     * <项> --→ <因⼦> <项'>
     * */
    private void itemAnalysis(ExpressionType expressionType,int layer){
        divisorAnalysis(expressionType,layer+1);
        item_Analysis(expressionType,layer+1);
    }
    /**
     * <因⼦>--→<标识符> |<⽆符号整数> | (<表达式>)
     * */
    private void divisorAnalysis(ExpressionType expressionType,int layer){
//        String temp="~";
        boolean flag=false;
        if(token.getTokenType()==TokenType.ID){
            if(!symbolMap.containsKey(token.getTokenValue())){
                throw new GrammarException(11, token.getLineNum(),token.getColumnNum());
            }
            else{
                symbols.push(new Symbol(layer,token.getTokenValue()));
                readToken();
            }
        }
        else if(token.getTokenType()==TokenType.INT){
            symbols.push(new Symbol(layer,token.getTokenValue()));
            readToken();
        }
        else if(token.getTokenType()==TokenType.LPAR){
            symbols.push(new Symbol(layer,"("));

            readToken();

            expressionAnalysis(expressionType,layer+1);

            if (token.getTokenType()==TokenType.RPAR){

                /**
                 * 对括号表达式进行规约和中间代码生成，分括号内有无运算符号两种情况讨论
                 */
                StatutePar(layer);

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
    private void item_Analysis(ExpressionType expressionType ,int layer){

        if(token.getTokenType()==TokenType.MULTI||token.getTokenType()==TokenType.DIV){
            StatuteCal(1,layer);
            if(token.getTokenType()==TokenType.MULTI) {
                symbols.push(new Symbol(layer,"*"));
            }else{
                symbols.push(new Symbol(layer,"/"));
            }
            readToken();
            divisorAnalysis(expressionType,layer);
            item_Analysis(expressionType,layer);
        }
        else if(token.getTokenType()==TokenType.PLUS||token.getTokenType()==TokenType.SUB||
                token.getTokenType()==TokenType.THEN||token.getTokenType()==TokenType.DO||
                token.getTokenType()==TokenType.EOF||token.getTokenType()==TokenType.SEMI||token.getTokenType()==TokenType.END||
                token.getTokenType()==TokenType.EQUAL||token.getTokenType()==TokenType.UNEQUAL||
                token.getTokenType()==TokenType.SMALLER||token.getTokenType()==TokenType.SMEQUAL||
                token.getTokenType()==TokenType.GREATER||token.getTokenType()==TokenType.GREQUAL||
                token.getTokenType()==TokenType.RPAR){
                /**
                 * 对乘除表达式进行规约和中间代码生成
                 */
                StatuteCal(1,layer);

            return;
        }
        else{
            throw new GrammarException(15, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     * <表达式'>--→<加法运算符> <项> <表达式'>|ε
     * */
    private void expression_Analysis(ExpressionType expressionType,int layer){
        if(token.getTokenType()==TokenType.PLUS||token.getTokenType()==TokenType.SUB){
            StatuteCal(0,layer);
            if(token.getTokenType()==TokenType.PLUS) {
                symbols.push(new Symbol(layer,"+"));
            }else{
                symbols.push(new Symbol(layer,"-"));
            }
            readToken();
            itemAnalysis(expressionType,layer);
            expression_Analysis(expressionType,layer);
        }
        else if (
                token.getTokenType()==TokenType.THEN||token.getTokenType()==TokenType.DO||
                token.getTokenType()==TokenType.EOF||token.getTokenType()==TokenType.SEMI||token.getTokenType()==TokenType.END||
                token.getTokenType()==TokenType.EQUAL||token.getTokenType()==TokenType.UNEQUAL||
                token.getTokenType()==TokenType.SMALLER||token.getTokenType()==TokenType.SMEQUAL||
                token.getTokenType()==TokenType.GREATER||token.getTokenType()==TokenType.GREQUAL||
                token.getTokenType()==TokenType.RPAR){
                /**
                 * 对加减表达式进行规约和中间代码生成
                 */
                StatuteCal(0,layer);
            return;
        }
        else{
            throw new GrammarException(16, token.getLineNum(),token.getColumnNum());
        }
    }
    /**
     * <条件语句>--→IF <条件> THEN <语句>
     * */
    private void conditionStatementAnalysis(int layer){
        if(token.getTokenType()==TokenType.IF){
            symbols.push(new Symbol(layer,"IF"));
            readToken();
        }
        else{
            throw new GrammarException(17, token.getLineNum(),token.getColumnNum());
        }
        conditionAnalysis(layer+1);

        if(token.getTokenType()==TokenType.THEN){
            symbols.push(new Symbol(layer,"THEN"));
            readToken();
        }
        else{
            throw new GrammarException(18, token.getLineNum(),token.getColumnNum());
        }
        Symbol m =new Symbol(layer,"M"+number++);
        m.setQuad(nextquad());
        symbols.push(m);

        statementAnalysis(layer+1);

        /**
         * 对条件语句进行规约和中间代码生成
         */
        StatuteCon(layer);
    }
    /**
     * <条件>→<表达式> <关系运算符> <表达式>
     * */
    private void conditionAnalysis(int layer){
        ExpressionType expressionType=ExpressionType.condition;
        expressionAnalysis(expressionType,layer+1);

        if (
            token.getTokenType()==TokenType.EQUAL||token.getTokenType()==TokenType.UNEQUAL||
            token.getTokenType()==TokenType.SMALLER||token.getTokenType()==TokenType.SMEQUAL||
            token.getTokenType()==TokenType.GREATER||token.getTokenType()==TokenType.GREQUAL){
            symbols.push(new Symbol(layer,getCompareName(token.getTokenType())));
            readToken();
        }
        else{
            throw new GrammarException(19, token.getLineNum(),token.getColumnNum());
        }

//        for (Symbol s : symbols) {
//            System.out.println(s.getSymbol()+" "+s.getLayer());
//        }
//        System.out.println("-------------------");

        expressionAnalysis(expressionType,layer+1);

        /**
         * 对条件表达式进行处理
         */
        StatuteBool(layer);

    }
    /**
     * <循环语句>→WHILE <条件> DO <语句>
     * */
    private void loopStatementAnalysis(int layer){
        if(token.getTokenType()==TokenType.WHILE){
            symbols.push(new Symbol(layer,"WHILE"));
            readToken();
        }
        else{
            throw new GrammarException(20, token.getLineNum(),token.getColumnNum());
        }

        Symbol m1=new Symbol(layer,"M"+number++);
        m1.setQuad(nextquad());
        symbols.push(m1);

        conditionAnalysis(layer+1);

        if(token.getTokenType()==TokenType.DO){
            symbols.push(new Symbol(layer,"DO"));
            readToken();
        }
        else{
            throw new GrammarException(21, token.getLineNum(),token.getColumnNum());
        }
        Symbol m2 =new Symbol(layer,"M"+number++);
        m2.setQuad(nextquad());
        symbols.push(m2);

        statementAnalysis(layer+1);

        /**
         * 对循环语句进行规约和中间代码生成
         */
        StatuteLoop(layer);
    }
    /**
     * <复合语句>→BEGIN <语句>{; <语句>} END
     * */
    private void compoundStatementAnalysis(int layer){
        if(token.getTokenType()==TokenType.BEGIN){
            readToken();
        }
        else{
            throw new GrammarException(22, token.getLineNum(),token.getColumnNum());
        }
        statementAnalysis(layer+1);
        while(true){
            if (token.getTokenType()==TokenType.SEMI){
                readToken();
                statementAnalysis(layer+1);
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

    /** ---------------------------------------------------------------------- **/
    /** 以下为语义分析及中间代码生成函数 */
    /** ---------------------------------------------------------------------- **/

    /**
     * 计算语句规约，1表示乘除，0表示加减
     * */
    private void StatuteCal(int num, int layer){
//        for (Symbol s : symbols) {
//            System.out.println(s.getSymbol()+" "+s.getLayer());
//        }
//        System.out.println("-------------------");
        if(symbols.size()>=3){
            MidCode midCode=new MidCode();

            Symbol temp1=symbols.pop();
            Symbol temp2=symbols.pop();
            Symbol temp3=symbols.pop();
            if(num==0) {
                if ((temp2.getSymbol().equals("+") || temp2.getSymbol().equals("-"))
                        && temp3.getLayer() == temp1.getLayer()
                ) {
                    midCode.setRight(temp1.getSymbol());
                    midCode.setOp(temp2.getSymbol());
                    midCode.setLeft(temp3.getSymbol());
                    String temp = "T" + Integer.toString(number++);
                    midCode.setDes(temp);
                    midCodeSet.add(midCode);
                    symbols.push(new Symbol(layer, temp));
                } else {
                    symbols.push(temp3);
                    symbols.push(temp2);
                    symbols.push(temp1);
                }
            }else{
                if ((temp2.getSymbol().equals("*") || temp2.getSymbol().equals("/"))
                        && temp3.getLayer() == temp1.getLayer()
                ) {
                    midCode.setRight(temp1.getSymbol());
                    midCode.setOp(temp2.getSymbol());
                    midCode.setLeft(temp3.getSymbol());
                    String temp = "T" + Integer.toString(number++);
                    midCode.setDes(temp);
                    midCodeSet.add(midCode);
                    symbols.push(new Symbol(layer, temp));
                } else {
                    symbols.push(temp3);
                    symbols.push(temp2);
                    symbols.push(temp1);
                }
            }
        }
    }

    /**
     * 对布尔表达式进行规约和中间代码生成
     * @param layer
     */
    private void StatuteBool(int layer){
        Symbol temp1=symbols.pop();
        Symbol temp2=symbols.pop();
        Symbol temp3=symbols.pop();

        String t3="T"+Integer.toString(number++);
        Symbol ms3=new Symbol(layer-1,t3);
        ms3.setFlaseList(nextquad()+1);
        ms3.setTrueList(nextquad());
        symbols.push(ms3);

        MidCode mc3=new MidCode();
        mc3.setDes("-");
        mc3.setLeft(temp3.getSymbol());
        mc3.setRight(temp1.getSymbol());
        mc3.setOp("j"+temp2.getSymbol());
        midCodeSet.add(mc3);
        MidCode mc4=new MidCode();
        mc4.setDes("-");
        mc4.setOp("j");
        midCodeSet.add(mc4);
    }

    /**
     * 对条件语句进行规约和中间代码生成
     * @param layer
     */
    private void StatuteCon(int layer){
        Symbol temp1=symbols.peek();
        while(temp1.getLayer()!=layer){
            symbols.pop();
            temp1=symbols.peek();
        }
        String S2="T"+Integer.toString(number++);
        Symbol s_S2=new Symbol(layer,S2);
        symbols.push(s_S2);

        Symbol s_S1=symbols.pop();
        Symbol s_M=symbols.pop();
        Symbol s_then=symbols.pop();
        Symbol s_E=symbols.pop();
        Symbol s_IF=symbols.pop();
        backpatch(s_E.getTrueList(),s_M.getQuad());
        String S="T"+Integer.toString(number++);
        Symbol s_S=new Symbol(layer,S);
        s_S.setNextList(merge(s_E.getFlaseList(),s_S1.getNextList()));

        backpatch(s_S.getNextList(),nextquad());
        symbols.push(s_S);
    }
    /**
     * 对循环语句进行规约和中间代码生成
     * @param layer
     */
    private void StatuteLoop(int layer){
        Symbol temp1=symbols.peek();
        while(temp1.getLayer()!=layer){
            symbols.pop();
            temp1=symbols.peek();
        }
        String S2="T"+Integer.toString(number++);
        Symbol s_S2=new Symbol(layer,S2);
        s_S2.setQuad(nextquad());
        s_S2.setNextList(-1);
        symbols.push(s_S2);

        Symbol s_S1=symbols.pop();
        Symbol s_M2=symbols.pop();
        Symbol s_DO=symbols.pop();
        Symbol s_E=symbols.pop();
        Symbol s_M1=symbols.pop();
        Symbol s_WHILE=symbols.pop();
        backpatch(s_S1.getNextList(),s_M1.getQuad());
        backpatch(s_E.getTrueList(),s_M2.getQuad());
        String S="T"+Integer.toString(number++);
        Symbol s_S=new Symbol(layer,S);
        s_S.setNextList(s_E.getFlaseList());
        symbols.push(s_S);
        MidCode midCode=new MidCode();
        midCode.setOp("j");
        midCode.setDes(Integer.toString(s_M1.getQuad()));
        midCodeSet.add(midCode);
        backpatch(s_S.getNextList(),nextquad());
    }

    /**
     * 对赋值语句进行规约和中间代码生成
     * @param layer
     */
    private void StatuteAssign(int layer){
        MidCode midCode=new MidCode();
        midCode.setRight(symbols.pop().getSymbol());
        midCode.setOp(symbols.pop().getSymbol());
        midCode.setDes(symbols.pop().getSymbol());
        midCodeSet.add(midCode);
        symbols.push(new Symbol(layer,"T"+Integer.toString(number++)));
    }

    /**
     * 对数值取相反数表达式进行处理
     * @param layer
     */
    private void StatuteUminus(int layer){
        Symbol temp1=symbols.pop();
        Symbol temp2=symbols.pop();
        String temp="T"+Integer.toString(number++);
        MidCode midCode=new MidCode();
        midCode.setDes(temp);
        midCode.setOp("uminus");
        midCode.setRight(temp1.getSymbol());
        midCodeSet.add(midCode);
        symbols.push(new Symbol(layer,temp));
    }

    /**
     * 对括号表达式进行处理
     * @param layer
     */
    private void StatutePar(int layer){
        Symbol temp1=symbols.pop();
        Symbol temp2=symbols.pop();
        MidCode midCode=new MidCode();
        String temp="T"+Integer.toString(number++);
        midCode.setDes(temp);
        if(temp2.getSymbol().equals("(")){
            midCode.setOp(":=");
            midCode.setRight(temp1.getSymbol());
        }else{
            Symbol temp3=symbols.pop();
            symbols.pop();
            midCode.setRight(temp1.getSymbol());
            midCode.setOp(temp2.getSymbol());
            midCode.setLeft(temp3.getSymbol());
        }
        midCodeSet.add(midCode);
        symbols.push(new Symbol(layer,temp));
    }
    /**
     * 用于布尔语句翻译的生成链表
     * @param n
     */
    private int makeList(int n){
        midCodeSet.getAllcode().get(n).setDes("-");
        return n;
    }

    /**
     * 用于布尔语句翻译的生成合并
     */
    private int merge(int n1,int n2){
        MidCode m=midCodeSet.getAllcode().get(n1);
        String temp=m.getDes();
        while(temp!="-"){
            m=midCodeSet.getAllcode().get(Integer.parseInt(temp));
            temp=m.getDes();
        }
        if(n2!=-1)
            m.setDes(Integer.toString(n2));
        return n1;
    }

    /**
     * 返回下一条将要生成的三地址代码地址
     * @return
     */
    private int nextquad(){
        return midCodeSet.getAddress()+1;
    }

    /**
     * 回填函数，将以p为表头的链表都回填为t
     * @param p
     * @param t
     */
    private void backpatch(int p,int t){
        if(p!=-1) {
            MidCode m = midCodeSet.getAllcode().get(p);
            String temp = m.getDes();
            m.setDes(Integer.toString(t));
            while (temp != "-") {
                m = midCodeSet.getAllcode().get(Integer.parseInt(temp));
                temp = m.getDes();
                m.setDes(Integer.toString(t));
            }
        }
    }

    /**
     * 得到比较运算符的符号
     * @param t
     * @return
     */
    private String getCompareName(TokenType t){
        switch(t){
            case EQUAL:
                return "=";
            case UNEQUAL:
                return "!=";
            case SMALLER:
                return "<";
            case SMEQUAL:
                return "<=";
            case GREATER:
                return ">";
            case GREQUAL:
                return ">=";
        }
        return "error";
    }

}