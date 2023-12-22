import org.tjdx.GrammarAnalysis;
import org.tjdx.LexAnalysis;
import org.tjdx.Token;
import org.tjdx.TokenType;

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
        GrammarAnalysis grammarAnalysis = new GrammarAnalysis("D:\\2023-2024-1 Files\\编译原理\\PL_0_Complier\\src\\main\\resources\\test01");
        grammarAnalysis.startAnalysis();
    }
}
