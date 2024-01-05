package PL0Compiler;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.tjdx.MidCode;
import org.tjdx.MidCodeSet;
import org.tjdx.PL0Compiler.StatuteType;
import org.tjdx.Symbol;
import org.tjdx.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.Math.abs;

public class PL0VisitorTest extends PL0Compiler.PL0BaseVisitor<Void> {
    private int number; // 临时变量标号
    private Stack<Symbol> symbols; //建立一个符号栈，方便对表达式进行规约
    public MidCodeSet midCodeSet; //中间代码集
    private Stack<StatuteType> statuteTypes;// 规约类型栈
    private List<String> names;  // 已定义的变量名、常量名
    public PL0VisitorTest(){
        this.number=0;
        symbols=new Stack<>();
        midCodeSet=new MidCodeSet();
        statuteTypes =new Stack<>();
        names =new ArrayList<>();
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
            while (temp != "-" && Integer.parseInt(temp)<=midCodeSet.getAddress()) {
                m = midCodeSet.getAllcode().get(Integer.parseInt(temp));
                temp = m.getDes();
                m.setDes(Integer.toString(t));
            }
        }
    }
    /**
     * 得到比价运算符的符号
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

    /**
     * 处理运算符表达式
     * @param s
     */
    private void statuteAddAndMul(StatuteType s){
        int depth=s.getDepth();
        Symbol s1=symbols.pop();
        Symbol s2=symbols.pop();
        if(s1.getLayer()!= s2.getLayer()){
            s1.setLayer(s1.getLayer()-1);
            symbols.push(s2);
            symbols.push(s1);
            statuteTypes.push(s);
            return;
        }
        String temp="T"+Integer.toString(number++);
        MidCode m=new MidCode();
        m.setDes(temp);
        m.setOp(s.getType());
        m.setRight(s1.getSymbol());
        m.setLeft(s2.getSymbol());
        midCodeSet.add(m);
        symbols.push(new Symbol(depth-1,temp));
    }

    /**
     * 对赋值语句进行规约和中间代码生成
     * @param s
     */
    private void statuteEqual(StatuteType s){
//        for(Symbol t:symbols){
//            System.out.println(t.getSymbol()+" "+t.getLayer());
//        }
//        System.out.println("++++++++++++++++++++++++");
        int depth=s.getDepth();
        Symbol s1=symbols.pop();
        Symbol s2=symbols.pop();
        if(s1.getLayer()!= s2.getLayer()){
            s1.setLayer(s1.getLayer()-1);
            symbols.push(s2);
            symbols.push(s1);
            statuteTypes.push(s);
            return;
        }
        MidCode m=new MidCode();
        m.setDes(s2.getSymbol());
        m.setOp(":=");
        m.setRight(s1.getSymbol());
        midCodeSet.add(m);
        symbols.push(new Symbol(depth-1,"T"+Integer.toString(number++)));
    }

    /**
     * 对布尔表达式进行规约和中间代码生成
     * @param statuteType
     */
    private void statueIF(StatuteType statuteType){
        int depth=statuteType.getDepth();

        Symbol temp1=symbols.pop();
        Symbol temp2=symbols.pop();
        Symbol temp3=symbols.pop();
        if(temp1.getLayer()!=temp3.getLayer()){
            temp1.setLayer(temp1.getLayer()-1);
            symbols.push(temp3);
            symbols.push(temp2);
            symbols.push(temp1);
            statuteTypes.push(statuteType);
            return;
        }
        String t3="T"+Integer.toString(number++);
        Symbol ms3=new Symbol(depth-1,t3);
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
        statuteTypes.push(statuteType);
    }

    /**
     * 对条件语句进行规约和中间代码生成
     * @param statuteType
     */
    private void statueTHEN(StatuteType statuteType){
        int depth=statuteType.getDepth();
        Symbol s_S1=symbols.pop();
        Symbol s_M=symbols.pop();
//        StatuteType s_then=statuteTypes.pop();
        Symbol s_E=symbols.pop();
//        StatuteType s_IF=statuteTypes.pop();
        backpatch(s_E.getTrueList(),s_M.getQuad());

        String S="T"+Integer.toString(number++);
        Symbol s_S=new Symbol(s_E.getLayer(),S);
//        System.out.println(s_E.getFlaseList()+s_S1.getNextList());
        s_S.setNextList(merge(s_E.getFlaseList(),s_S1.getNextList()));
        backpatch(s_S.getNextList(),nextquad());
        symbols.push(s_S);
    }

    /**
     * 对循环语句进行规约和中间代码生成
     * @param statuteType
     */
    private void statueDO(StatuteType statuteType){
        int layer=statuteType.getDepth();
        boolean flag=false;
        Symbol temp1=symbols.peek();
        while(temp1.getLayer()!=layer){
            symbols.pop();
            temp1=symbols.peek();
            flag=true;
        }
        if(flag) {
            String S2 = "T" + Integer.toString(number++);
            Symbol s_S2 = new Symbol(layer, S2);
            s_S2.setQuad(nextquad());
            s_S2.setNextList(-1);
            symbols.push(s_S2);
        }

        Symbol s_S1=symbols.pop();
        Symbol s_M2=symbols.pop();
//        Symbol s_DO=symbols.pop();
        Symbol s_E=symbols.pop();
        Symbol s_M1=symbols.pop();
//        Symbol s_WHILE=symbols.pop();
        backpatch(s_S1.getNextList(),s_M1.getQuad());
        System.out.println("***********");
        System.out.println(s_E.getTrueList());

        for(int i=0;i<midCodeSet.getAllcode().size();++i){
            MidCode code=midCodeSet.getAllcode().get(i);
            if(!code.getOp().equals(":=")&& !code.getOp().contains("j")) {
                System.out.println(i + 100 + "  " + code.getDes() + " := " + code.getLeft() + "  " + code.getOp() + "  " + code.getRight());
            } else if (code.getOp().contains("j")) {
                System.out.println(i+100+"  "+code.getOp()+"  "+code.getLeft()+"  "+code.getRight()+"  "+code.getDes());
            } else{
                String des= code.getDes();
                String src="";
                if(!code.getLeft().equals("~")){
                    src=code.getLeft();
                }else{
                    src=code.getRight();
                }
                System.out.println(i+100+"  "+des+" := "+src);
            }
        }
        System.out.println("***********");
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

    private void StatuteUminus(StatuteType statuteType){
        int layer=statuteType.getDepth();
        Symbol temp1=symbols.pop();
        String temp="T"+Integer.toString(number++);
        MidCode midCode=new MidCode();
        midCode.setDes(temp);
        midCode.setOp("uminus");
        midCode.setRight(temp1.getSymbol());
        midCodeSet.add(midCode);
        symbols.push(new Symbol(layer,temp));
    }

    /**
     * 进行语句规约类型的选择
     */
    private void statutePick(){
        for(Symbol s:symbols){
            System.out.println(s.getSymbol()+" "+s.getLayer());
        }
        System.out.println(statuteTypes.peek().getType());
        System.out.println("-----------------------");
        StatuteType statuteType=statuteTypes.pop();
        int depth=statuteType.getDepth();
        switch (statuteType.getType()){
            case "+":
                statuteAddAndMul(statuteType);
                break;
            case ":=":
                statuteEqual(statuteType);
                break;
            case "-":
                statuteAddAndMul(statuteType);
                break;
            case "*":
                statuteAddAndMul(statuteType);
                break;
            case "/":
                statuteAddAndMul(statuteType);
                break;
            case "THEN":
                statueTHEN(statuteType);
                break;
            case "IF":
                statueIF(statuteType);
                break;
            case "WHILE":
                statueIF(statuteType);
                break;
            case "DO":
                statueDO(statuteType);
                break;
            case "uminus":
                StatuteUminus(statuteType);
                break;
        }
    }
    @Override
    public Void visitProgram(PL0Compiler.PL0Parser.ProgramContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitProgramHeader(PL0Compiler.PL0Parser.ProgramHeaderContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubProgram(PL0Compiler.PL0Parser.SubProgramContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstantDeclaration(PL0Compiler.PL0Parser.ConstantDeclarationContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstantDefinition(PL0Compiler.PL0Parser.ConstantDefinitionContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitVariableDeclaration(PL0Compiler.PL0Parser.VariableDeclarationContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIdentifier(PL0Compiler.PL0Parser.IdentifierContext ctx) {
        symbols.push(new Symbol(ctx.depth(),ctx.getText()));

        // 获取标识符名称
        String name = ctx.getText();
        // 获取词法单元的开始位置信息
        Token startToken = ctx.getStart();
        int line = startToken.getLine();
        int charPositionInLine = startToken.getCharPositionInLine();
        charPositionInLine += name.length();

        // 获取父节点类型
        int parentType = ctx.getParent().getRuleIndex();
        if (parentType == 4 || parentType == 5) {
            // 常量定义/变量说明
            if (names.contains(name)) {
                // 标识符重复声明
                System.err.println("line "+line+":"+charPositionInLine+" "+"\""+name+"\""+" already defined");
                throw new ParseCancellationException("Syntax error detected.");
            }
            names.add(name);
        } else if (parentType != 1) {
            // 使用变量/常量
            if (!names.contains(name)) {
                // 使用了未定义的变量/常量
                System.err.println("line "+line+":"+charPositionInLine+" "+"\""+name+"\""+" not defined");
                throw new ParseCancellationException("Syntax error detected.");
            }
        }
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUnsignedInt(PL0Compiler.PL0Parser.UnsignedIntContext ctx) {
        symbols.push(new Symbol(ctx.depth(),ctx.getText()));
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitStatement(PL0Compiler.PL0Parser.StatementContext ctx) {
        visitChildren(ctx);
//        statutePick();
        return null;
    }

    @Override
    public Void visitAssignmentStatement(PL0Compiler.PL0Parser.AssignmentStatementContext ctx) {
        statuteTypes.push(new StatuteType(":=",ctx.depth()));
        visitChildren(ctx);
        statutePick();
        return null;
    }

    @Override
    public Void visitIfStatement(PL0Compiler.PL0Parser.IfStatementContext ctx) {
        statuteTypes.push(new StatuteType("IF",ctx.depth()));

        visitChildren(ctx);

        statutePick();
        return null;
    }

    @Override
    public Void visitWhileStatement(PL0Compiler.PL0Parser.WhileStatementContext ctx) {
        statuteTypes.push(new StatuteType("WHILE",ctx.depth()));
        Symbol m1=new Symbol(ctx.depth(),"M"+number++);
        m1.setQuad(nextquad());
        symbols.push(m1);

        visitChildren(ctx);

        statutePick();

        return null;
    }

    @Override
    public Void visitCompoundStatement(PL0Compiler.PL0Parser.CompoundStatementContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitEmptyStatement(PL0Compiler.PL0Parser.EmptyStatementContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpression(PL0Compiler.PL0Parser.ExpressionContext ctx) {
        visitChildren(ctx);
        if(ctx.expression()==null && ctx.term()!=null && ctx.getText().startsWith("-")){
            statuteTypes.push(new StatuteType("uminus", ctx.depth()));
        }
        statutePick();
        return null;
    }

    @Override
    public Void visitTerm(PL0Compiler.PL0Parser.TermContext ctx) {
        visitChildren(ctx);
        statutePick();
        return null;
    }

    @Override
    public Void visitFactor(PL0Compiler.PL0Parser.FactorContext ctx) {
        visitChildren(ctx);
        statutePick();
        return null;
    }

    @Override
    public Void visitCondition(PL0Compiler.PL0Parser.ConditionContext ctx) {
        visitChildren(ctx);

        statutePick();
        StatuteType temp=statuteTypes.pop();
        if(temp.getType().equals("IF")) {
            statuteTypes.push(new StatuteType("THEN", ctx.depth()));
        } else if (temp.getType().equals("WHILE")) {
            statuteTypes.push(new StatuteType("DO",ctx.depth()));
        }
        Symbol m = new Symbol(ctx.depth(), "M" + number++);
        m.setQuad(nextquad());
        symbols.push(m);

        return null;
    }

    @Override
    public Void visitAdditionOperator(PL0Compiler.PL0Parser.AdditionOperatorContext ctx) {
        statuteTypes.push(new StatuteType(ctx.getText(), ctx.depth()));
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMultiplicationOperator(PL0Compiler.PL0Parser.MultiplicationOperatorContext ctx) {
        statuteTypes.push(new StatuteType(ctx.getText(), ctx.depth()));
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRelationOperator(PL0Compiler.PL0Parser.RelationOperatorContext ctx) {
        symbols.push(new Symbol(ctx.depth(),ctx.getText()));
        visitChildren(ctx);
        return null;
    }
}
