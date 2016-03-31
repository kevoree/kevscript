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
    public void testInstance() throws Exception {
        tester(getClass().getResourceAsStream("/instance.kevs"));
    }

    @Test
    public void testAdd() throws Exception {
        tester(getClass().getResourceAsStream("/add.kevs"));
    }

    @Test
    public void testAttachDetachBindUnbind() throws Exception {
        tester(getClass().getResourceAsStream("/attach_detach_bind_unbind.kevs"));
    }

    @Test
    public void testAttachModelConnector() throws Exception {
        tester(getClass().getResourceAsStream("/attach_model_connector.kevs"));
    }

    @Test
    public void testDetachModelConnector() throws Exception {
        tester(getClass().getResourceAsStream("/detach_model_connector.kevs"));
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
    public void testFunctionNative() throws Exception {
        tester(getClass().getResourceAsStream("/function_native.kevs"));
    }

    @Test
    public void testImport() throws Exception {
        tester(getClass().getResourceAsStream("/import.kevs"));
    }

    @Test
    public void testLet() throws Exception {
        tester(getClass().getResourceAsStream("/let.kevs"));
    }

    @Test
    public void testMetaInit() throws Exception {
        tester(getClass().getResourceAsStream("/meta-init.kevs"));
    }

    @Test
    public void testMetaMerge() throws Exception {
        tester(getClass().getResourceAsStream("/meta-merge.kevs"));
    }

    @Test
    public void testMetaRemove() throws Exception {
        tester(getClass().getResourceAsStream("/meta-remove.kevs"));
    }
    @Test
    public void testNetInit() throws Exception {
        tester(getClass().getResourceAsStream("/net-init.kevs"));
    }

    @Test
    public void testNetMerge() throws Exception {
        tester(getClass().getResourceAsStream("/net-merge.kevs"));
    }

    @Test
    public void testNetRemove() throws Exception {
        tester(getClass().getResourceAsStream("/net-remove.kevs"));
    }

    @Test
    public void testRemove() throws Exception {
        tester(getClass().getResourceAsStream("/remove.kevs"));
    }

    @Test
    public void testReplaceModelConnector() throws Exception {
        tester(getClass().getResourceAsStream("/replace_model_connector.kevs"));
    }

    @Test
    public void testSensorNetwork() throws Exception {
        tester(getClass().getResourceAsStream("/sensor_network.kevs"));
    }

    @Test
    public void testSet() throws Exception {
        tester(getClass().getResourceAsStream("/set.kevs"));
    }

    @Test
    public void testStop() throws Exception {
        tester(getClass().getResourceAsStream("/stop.kevs"));
    }

    @Test
    public void testTime() throws Exception {
        tester(getClass().getResourceAsStream("/time.kevs"));
    }


}