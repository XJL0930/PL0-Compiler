package org.tjdx.PL0Compiler;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class PL0ErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        System.err.println("line "+line+":"+charPositionInLine+" "+msg);

        // 抛出ParseCancellationException来终止解析过程
        throw new ParseCancellationException("Syntax error detected.");
    }
}
