import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import PL0Compiler.PL0VisitorTest;
import org.tjdx.MidCode;

import java.io.IOException;

public class TestRecognizerGeneratedByAntlr {
    public static void main(String[] args) throws IOException {
        CharStream input = CharStreams.fromFileName("D:\\2023 Fall\\CompileFundamention\\PL0-Compiler\\src\\main\\resources\\test01");
        PL0Compiler.PL0Lexer lexer = new PL0Compiler.PL0Lexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PL0Compiler.PL0Parser parser = new PL0Compiler.PL0Parser(tokens);

        PL0Compiler.PL0Parser.ProgramContext root = parser.program();
        PL0VisitorTest visitor = new PL0VisitorTest();
        visitor.visit(root);

        System.out.println("parser executed!");

//        visitor.statuteAll();

        for(int i=0;i<visitor.midCodeSet.getAllcode().size();++i){
            MidCode code=visitor.midCodeSet.getAllcode().get(i);
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
