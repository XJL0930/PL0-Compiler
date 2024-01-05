import org.tjdx.*;

public class TestLex {
    public static void main(String[] args) {
//        LexAnalysis lexAnalysis = LexAnalysis.getLexAnalysisInstance("D:\\2023-2024-1 Files\\编译原理\\PL_0_Complier\\src\\main\\resources\\test01");
//        Token token;
//        while(true){
//            token = lexAnalysis.analysis();
//            if (token.getTokenType()== TokenType.EOF){
//                break;
//            }
//            System.out.println(token.toString());
//        }
        GrammarAnalysis grammarAnalysis = new GrammarAnalysis("D:\\2023 Fall\\CompileFundamention\\PL0-Compiler\\src\\main\\resources\\test04");
        grammarAnalysis.startAnalysis();
        System.out.println(grammarAnalysis.midCodeSet.getAllcode().size());
        for(int i=0;i<grammarAnalysis.midCodeSet.getAllcode().size();++i){
            MidCode code=grammarAnalysis.midCodeSet.getAllcode().get(i);
            if(!code.getOp().equals(":=")&& !code.getOp().contains("j")) {
                System.out.println(i + 100 + "  " + code.getDes() + " := " + code.getLeft() + "  " + code.getOp() + "  " + code.getRight());
            } else if (code.getOp().contains("j")) {
                System.out.println(i+100+"  "+code.getOp()+"  "+code.getLeft()+"  "+code.getRight()+"  "+(100+Integer.parseInt(code.getDes())));
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
    }
}
