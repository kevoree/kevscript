package kevscript;

import org.antlr.v4.runtime.*;
import org.junit.Test;
import org.kevoree.kevscript.KevScriptLexer;
import org.kevoree.kevscript.KevScriptParser;

import java.io.InputStream;

public class KevScriptTest {

    private void tester(InputStream is) throws Exception {
        KevScriptLexer l = new KevScriptLexer(new ANTLRInputStream(is));
        KevScriptParser p = new KevScriptParser(new CommonTokenStream(l));
        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + ":" + charPositionInLine + " due to " + msg, e);
            }
        });
        p.script();
    }

    @Test
    public void testAdd() throws Exception {
        tester(getClass().getResourceAsStream("/add.kevs"));
    }

    @Test
    public void testFor() throws Exception {
        tester(getClass().getResourceAsStream("/for.kevs"));
    }

    @Test
    public void testFunction() throws Exception {
        tester(getClass().getResourceAsStream("/function.kevs"));
    }

    @Test
    public void testFunctionCall() throws Exception {
        tester(getClass().getResourceAsStream("/function_call.kevs"));
    }

    @Test
    public void testLet() throws Exception {
        tester(getClass().getResourceAsStream("/let.kevs"));
    }
    @Test
    public void testNetInit() throws Exception {
        tester(getClass().getResourceAsStream("/net-init.kevs"));
    }

    @Test
    public void testNetmerge() throws Exception {
        tester(getClass().getResourceAsStream("/net-merge.kevs"));
    }

    @Test
    public void testNetremove() throws Exception {
        tester(getClass().getResourceAsStream("/net-remove.kevs"));
    }
}