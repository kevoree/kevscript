import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.kevoree.kevscript.KevScriptLexer;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.excpt.CustomException;
import org.kevoree.kevscript.language.listener.DescriptiveErrorListener;
import org.kevoree.kevscript.language.visitors.KevscriptVisitor;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by mleduc on 02/03/16.
 */
public class TestCases {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAdd0()  throws Exception {
        analyzeDirectory("add_0");
    }

    @Test
    public void testRealWorld0()  throws Exception {
        analyzeDirectory("real_world_0");
    }

    @Test
    public void testFor()  throws Exception {
        analyzeDirectory("for");
    }

    @Test()
    public void testAdd1Error1() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance node0 already declared in this scope");
        interpret(pathToString("/add_1/error1.kevs"));
    }

    @Test
    public void testAttach0Error1() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance node1 not found");
        interpret(pathToString("/attach_0/error1.kevs"));
    }

    @Test
    public void testAttach0Error2() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance group0 not found");
        interpret(pathToString("/attach_0/error2.kevs"));
    }

    @Test
    public void testAttach1()  throws Exception {
        analyzeDirectory("attach_1");
    }

    @Test
    public void testDetachError1() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance node1 not found");
        interpret(pathToString("/attach_0/error1.kevs"));
    }

    @Test
    public void testDetachError2() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance group0 not found");
        interpret(pathToString("/attach_0/error2.kevs"));
    }

    @Test
    public void testBind()  throws Exception {
        analyzeDirectory("bind/valid");
    }

    @Test
    public void testBindError1() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance node1 not found");
        interpret(pathToString("/bind/error1.kevs"));
    }

    @Test
    public void testBindError2() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance chan0 not found");
        interpret(pathToString("/bind/error2.kevs"));
    }

    @Test
    public void testDetach()  throws Exception {
        analyzeDirectory("detach/valid");
    }

    @Test
    public void testUnbind()  throws Exception {
        analyzeDirectory("unbind/valid");
    }

    @Test
    public void testUnbindError1() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance node1 not found");
        interpret(pathToString("/unbind/error1.kevs"));
    }

    @Test
    public void testUnbindError2() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance chan0 not found");
        interpret(pathToString("/unbind/error2.kevs"));
    }

    private void analyzeDirectory(String add_0) throws IOException {
        final String newStr = pathToString("/" + add_0 + "/new.kevs");
        final String oldStr = pathToString("/" + add_0 + "/old.kevs");
        assertEquals(oldStr.trim(), interpret(newStr).trim());
    }

    private String pathToString(String name1) throws IOException {
        final InputStream newKev =  getClass().getResourceAsStream(name1);
        return IOUtils.toString(newKev);
    }

    private String interpret(String expression) {
        final KevScriptLexer lexer = new KevScriptLexer(new ANTLRInputStream(expression));
        final KevScriptParser parser = new KevScriptParser(new CommonTokenStream(lexer));
        lexer.removeErrorListeners();
        lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);
        parser.removeErrorListeners();
        parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
        final ParseTree tree = parser.script();
        return new KevscriptVisitor().visit(tree);
    }
}
