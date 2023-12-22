import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.tjdx.PL0Compiler.PL0VisitorTest;

import java.io.IOException;

public class TestRecognizerGeneratedByAntlr {
    public static void main(String[] args) throws IOException {
        CharStream input = CharStreams.fromFileName("E:\\program\\PL0-Compiler\\src\\main\\resources\\test01");
        PL0Compiler.PL0Lexer lexer = new PL0Compiler.PL0Lexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PL0Compiler.PL0Parser parser = new PL0Compiler.PL0Parser(tokens);

        PL0Compiler.PL0Parser.ProgramContext root = parser.program();
        PL0VisitorTest visitor = new PL0VisitorTest();
        visitor.visit(root);

        System.out.println("parser executed!");
    }
}
