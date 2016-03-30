import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.kevoree.kevscript.KevScriptLexer;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.excpt.NameCollisionException;
import org.kevoree.kevscript.language.excpt.PortPathNotFound;
import org.kevoree.kevscript.language.excpt.WrongNumberOfArguments;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.listener.DescriptiveErrorListener;
import org.kevoree.kevscript.language.processor.CommandsToString;
import org.kevoree.kevscript.language.visitors.KevscriptVisitor;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Preconditions :
 * - grammatically valid script
 * <p>
 * Controls :
 * - local variable names collision
 * - does not throw error on missing instance names (this will be check on a next phase)
 */
public class Phase1Test {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAdd0() throws Exception {
        analyzeDirectory("phase1/add_0");
    }

    @Test
    public void testInstance1Error1() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage("node0 already declared in this scope");
        interpretPhase1(pathToString("/phase1/instance_1/error1.kevs"));
    }

    @Test
    public void testLetRec1() throws Exception {
        analyzeDirectory("phase1/let/rec1");
    }

    @Test
    public void testLetError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage("a is expected to be of type InstanceExpression");
        interpretPhase1(pathToString("/phase1/let/rec_error/error1.kevs"));
    }

    @Test
    public void testLetError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage("b is expected to be of type InstanceExpression");
        interpretPhase1(pathToString("/phase1/let/rec_error/error2.kevs"));
    }

    @Test
    public void testLetArray() throws Exception {
        analyzeDirectory("phase1/let/array");
    }

    @Test
    public void testLetObjects() throws Exception {
        analyzeDirectory("phase1/let/objects");
    }

    @Test
    public void testLetArrayObject() throws Exception {
        analyzeDirectory("phase1/let/array_object");
    }


    @Test
    public void testMoveTest1() throws Exception {
        analyzeDirectory("phase1/move/test1");
    }

    @Test
    public void testMoveTest2() throws Exception {
        analyzeDirectory("phase1/move/test2");
    }

    @Test
    public void testMoveTest3() throws Exception {
        analyzeDirectory("phase1/move/test3");
    }

    @Test
    public void testNativeFunctionTest1() throws Exception {
        analyzeDirectory("phase1/native_function/test1");
    }

    @Test
    public void testNetInitTest1() throws Exception {
        analyzeDirectory("phase1/net-init/test1");
    }

    @Test
    public void testNetInitTest2() throws Exception {
        analyzeDirectory("phase1/net-init/test2");
    }

    @Test
    public void testNetInitError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage("b is expected to be of type ObjectDeclExpression");
        interpretPhase1(pathToString("/phase1/net-init/error1.kevs"));
    }

    @Test
    public void testNetInitError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage("b is expected to be of type ObjectDeclExpression");
        interpretPhase1(pathToString("/phase1/net-init/error2.kevs"));
    }

    @Test
    public void testNetMergeTest1() throws Exception {
        analyzeDirectory("phase1/net-merge/test1");
    }

    @Test
    public void testNetMergeTest2() throws Exception {
        analyzeDirectory("phase1/net-merge/test2");
    }

    @Test
    public void testNetMergeError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage("b is expected to be of type ObjectDeclExpression");
        interpretPhase1(pathToString("/phase1/net-merge/error1.kevs"));
    }

    @Test
    public void testNetMergeError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage("b is expected to be of type ObjectDeclExpression");
        interpretPhase1(pathToString("/phase1/net-merge/error2.kevs"));
    }

    @Test
    public void testNetRemoveTest1() throws Exception {
        analyzeDirectory("phase1/net-remove/test1");
    }

    @Test
    //@Ignore
    public void testRealWorld0() throws Exception {
        analyzeDirectory("phase1/real_world_0");
    }

    @Test
    public void testFor() throws Exception {
        analyzeDirectory("phase1/for");
    }

    @Test
    public void testFunctionReturn() throws Exception {
        analyzeDirectory("phase1/function_return");
    }

    @Test
    public void testForFunctionReturn() throws Exception {
        analyzeDirectory("phase1/for_function_return");
    }

    @Test
    public void testFunction() throws Exception {
        analyzeDirectory("phase1/function");
    }

    @Test
    public void testFunctionError1() throws Exception {
        exception.expect(WrongNumberOfArguments.class);
        exception.expectMessage("method a expected 0 arguments but got 1");
        interpretPhase1(pathToString("/phase1/function_error/function_err_1.kevs"));
    }

    @Test
    public void testFunctionError2() throws Exception {
        exception.expect(WrongNumberOfArguments.class);
        exception.expectMessage("method b expected 1 arguments but got 0");
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
    public void testAttach1() throws Exception {
        analyzeDirectory("phase1/attach_1");
    }

    @Test
    public void testDetachTest1() throws Exception {
        interpretPhase1(pathToString("/phase1/detach/test1.kevs"));
    }

    @Test
    public void testDetachTest2() throws Exception {
        interpretPhase1(pathToString("/phase1/detach/test2.kevs"));
    }

    @Test
    public void testBind() throws Exception {
        analyzeDirectory("phase1/bind/valid");
    }

    @Test
    public void testBindError1() throws Exception {
        exception.expect(PortPathNotFound.class);
        exception.expectMessage("portPath y not found");
        interpretPhase1(pathToString("/phase1/bind/error1.kevs"));
    }

    @Test
    public void testBindError2() throws Exception {
        exception.expect(PortPathNotFound.class);
        exception.expectMessage("portPath node1 not found");
        interpretPhase1(pathToString("/phase1/bind/error2.kevs"));
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
    public void testBindTest4() throws Exception {
        interpretPhase1(pathToString("/phase1/bind/test4.kevs"));
    }

    @Test
    public void testDetach() throws Exception {
        analyzeDirectory("phase1/detach/valid");
    }

    @Test
    public void testRemoveManyInstances() throws Exception {
        analyzeDirectory("phase1/remove/many_instances");
    }

    @Test
    public void testRemoveSingleInstance() throws Exception {
        analyzeDirectory("phase1/remove/single_instance");
    }


    @Test
    public void testEmptyScript() throws Exception {
        analyzeDirectory("phase1/empty_script");
    }
    @Test
    public void testFirstOrderFunction() throws Exception {
        analyzeDirectory("phase1/first_order_function");
    }

    @Test
    public void testSetError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage("conflict is expected to be of type StringExpression");
        interpretPhase1(pathToString("/phase1/set/error1.kevs"));
    }

    @Test
    public void testSetOk1() throws Exception {
        interpretPhase1(pathToString("/phase1/set/ok1.kevs"));
    }

    @Test
    public void testStartTest1() throws Exception {
        analyzeDirectory("phase1/start/test1");
    }

    @Test
    public void testStopTest1() throws Exception {
        analyzeDirectory("phase1/stop/test1");
    }

    @Test
    public void testUnbind() throws Exception {
        analyzeDirectory("phase1/unbind/valid");
    }

    @Test
    public void testUnbindError1() throws Exception {
        exception.expect(PortPathNotFound.class);
        exception.expectMessage("portPath node1 not found");
        interpretPhase1(pathToString("/phase1/unbind/error1.kevs"));
    }

    @Test
    public void testUnbindError2() throws Exception {
        exception.expect(PortPathNotFound.class);
        exception.expectMessage("portPath node1 not found");
        interpretPhase1(pathToString("/phase1/unbind/error2.kevs"));
    }

    @Test
    public void testUnbindTest1() throws Exception {
        interpretPhase1(pathToString("/phase1/unbind/test1.kevs"));
    }

    @Test
    public void testVariableScope() throws Exception {
        analyzeDirectory("/phase1/variable_scope");
    }

    private void analyzeDirectory(final String path) throws IOException {
        final String pathB;
        if(path.startsWith("/")) {
            pathB = path.substring(1);
        } else {
            pathB = path;
        }
        final String newStr = pathToString("/" + pathB + "/new.kevs");
        final String oldStr = pathToString("/" + pathB + "/old.kevs");
        assertEquals(oldStr.trim(), interpretPhase1(newStr).trim());
    }

    private String pathToString(String name1) throws IOException {
        final InputStream newKev = getClass().getResourceAsStream(name1);
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
