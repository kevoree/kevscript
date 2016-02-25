package org.kevoree.kevscript;

import org.antlr.v4.runtime.*;
import org.junit.Test;

import java.io.InputStream;

public class TestKevScript {

    private void tester(InputStream is) throws Exception {
        KevScriptLexer l = new KevScriptLexer(new ANTLRInputStream(is));
        KevScriptParser p = new KevScriptParser(new CommonTokenStream(l));
        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + ":" + charPositionInLine + " due to " + msg, e);
            }
        });
        p.kevscript();
    }

    @Test
    public void testMain() throws Exception {
        tester(getClass().getResourceAsStream("/main.kevs"));
    }

    @Test
    public void testAdd() throws Exception {
        tester(getClass().getResourceAsStream("/add.kevs"));
    }
}