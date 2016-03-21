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
import org.kevoree.kevscript.language.excpt.NameCollisionException;
import org.kevoree.kevscript.language.excpt.PortPathNotFound;
import org.kevoree.kevscript.language.listener.DescriptiveErrorListener;
import org.kevoree.kevscript.language.processor.CommandsToString;
import org.kevoree.kevscript.language.visitors.KevscriptVisitor;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Preconditions :
 *   - grammatically valid script
 *
 * Controls :
 *   - local variable names collision
 *   - does not throw error on missing instance names (this will be check on a next phase)
 */
public class Phase1TestCases {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAdd0()  throws Exception {
        analyzeDirectory("phase1/add_0");
    }

    @Test
    public void testInstance1Error1() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage("node0 already declared in this scope");
        interpretPhase1(pathToString("/phase1/instance_1/error1.kevs"));
    }

    @Test
    public void testRealWorld0()  throws Exception {
        analyzeDirectory("phase1/real_world_0");
    }

    @Test
    public void testFor()  throws Exception {
        analyzeDirectory("phase1/for");
    }

    @Test
    public void testFunctionReturn()  throws Exception {
        analyzeDirectory("phase1/function_return");
    }

    @Test
    public void testForFunctionReturn()  throws Exception {
        analyzeDirectory("phase1/for_function_return");
    }

    @Test
    public void testFunction()  throws Exception {
        analyzeDirectory("phase1/function");
    }

    @Test
    public void testFunctionError1() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("Wrong number of parameters");
        interpretPhase1(pathToString("/phase1/function_error/function_err_1.kevs"));
    }

    @Test
    public void testFunctionError2() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("Wrong number of parameters");
        interpretPhase1(pathToString("/phase1/function_error/function_err_2.kevs"));
    }

    @Test
    public void testAttach0Test1() throws Exception {
        interpretPhase1(pathToString("/phase1/attach_0/test1.kevs"));
    }

    @Test
    public void testAttach0Test2() throws Exception {
        interpretPhase1(pathToString("/phase1/attach_0/test2.kevs"));
    }

    @Test
    public void testAttach1()  throws Exception {
        analyzeDirectory("phase1/attach_1");
    }

    @Test
    public void testDetachError1() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance node1 not found");
        interpretPhase1(pathToString("/phase1/detach/error1.kevs"));
    }

    @Test
    public void testDetachError2() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance connector0 not found");
        interpretPhase1(pathToString("/phase1/detach/error2.kevs"));
    }

    @Test
    public void testBind()  throws Exception {
        analyzeDirectory("phase1/bind/valid");
    }

    @Test
    public void testBindError1() throws Exception {
        exception.expect(PortPathNotFound.class);
        exception.expectMessage("instance connector0 not found");
        interpretPhase1(pathToString("/phase1/bind/error1.kevs"));
    }

    @Test
    public void testBindTest1() throws Exception {
        interpretPhase1(pathToString("/phase1/bind/test1.kevs"));
    }

    @Test
    public void testBindTest2() throws Exception {
        interpretPhase1(pathToString("/phase1/bind/test2.kevs"));
    }

    @Test
    public void testBindTest3() throws Exception {
        interpretPhase1(pathToString("/phase1/bind/test3.kevs"));
    }

    @Test
    public void testDetach()  throws Exception {
        analyzeDirectory("phase1/detach/valid");
    }

    @Test
    public void testUnbind()  throws Exception {
        analyzeDirectory("phase1/unbind/valid");
    }

    @Test
    public void testUnbindError1() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance node1 not found");
        interpretPhase1(pathToString("/phase1/unbind/error1.kevs"));
    }

    @Test
    public void testUnbindError2() throws Exception {
        exception.expect(CustomException.class);
        exception.expectMessage("instance chan0 not found");
        interpretPhase1(pathToString("/phase1/unbind/error2.kevs"));
    }

    private void analyzeDirectory(String add_0) throws IOException {
        final String newStr = pathToString("/" + add_0 + "/new.kevs");
        final String oldStr = pathToString("/" + add_0 + "/old.kevs");
        assertEquals(oldStr.trim(), interpretPhase1(newStr).trim());
    }

    private String pathToString(String name1) throws IOException {
        final InputStream newKev =  getClass().getResourceAsStream(name1);
        return IOUtils.toString(newKev);
    }

    private String interpretPhase1(String expression) {
        final KevScriptLexer lexer = new KevScriptLexer(new ANTLRInputStream(expression));
        final KevScriptParser parser = new KevScriptParser(new CommonTokenStream(lexer));
        lexer.removeErrorListeners();
        lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);
        parser.removeErrorListeners();
        parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
        final ParseTree tree = parser.script();
        return new CommandsToString().proceed(new KevscriptVisitor().visit(tree));
    }
}
