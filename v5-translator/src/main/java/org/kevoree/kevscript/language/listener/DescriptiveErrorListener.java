package org.kevoree.kevscript.language.listener;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class DescriptiveErrorListener extends BaseErrorListener {
    public static DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();


    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e) {

        log(recognizer, line, charPositionInLine, msg);
    }

    private void log(Recognizer<?, ?> recognizer, long line, long charPositionInLine, String msg) {
        String sourceName = recognizer.getInputStream().getSourceName();
        if (!sourceName.isEmpty()) {
            sourceName = String.format("%s:%d:%d: ", sourceName, line, charPositionInLine);
        }

        System.err.println(sourceName + "line " + line + ":" + charPositionInLine + " " + msg);
    }

    /*@Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int line, int charPositionInLine, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        log(recognizer, line, charPositionInLine, "a");
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int line, int charPositionInLine, BitSet conflictingAlts, ATNConfigSet configs) {
        log(recognizer, line, charPositionInLine, "b");
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int line, int charPositionInLine, int prediction, ATNConfigSet configs) {
        log(recognizer, line, charPositionInLine, "c");
    }*/
}