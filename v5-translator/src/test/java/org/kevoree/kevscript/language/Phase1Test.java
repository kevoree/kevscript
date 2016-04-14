package org.kevoree.kevscript.language;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.excpt.*;
import org.kevoree.kevscript.language.expressions.finalexp.*;
import org.kevoree.kevscript.language.utils.HttpServer;
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

    private final static InstanceExpression MODEL_ROOT = new InstanceExpression("/", null);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAdd0() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("node0", new TypeExpression(null, "JavaNode", new VersionExpression(1), null)))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression("node0", null)))
                .addCommand(new InstanceCommand("node1", new TypeExpression(null, "JavaNode", null, null)))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression("node1", null)));
        analyzeDirectory(expected, "phase1/add_0");
    }

    @Test
    public void testInstance1Error1() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage("node0 already declared in this scope");
        interpretPhase1(pathToString("/phase1/instance_1/error1.kevs"));
    }

    @Test
    public void testLetRec1() throws Exception {
        // FIXME : unable to represent x
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("g", null), "c", null), "a"));
        analyzeDirectory(expected, "phase1/let/rec1");
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
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("y", null), "d", null), "b"));
        analyzeDirectory(expected, "phase1/let/array");
    }

    @Test
    public void testLetObjects() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("y", null), "d", null), "2"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("y", null), "e", null), "3"));
        analyzeDirectory(expected, "phase1/let/objects");
    }

    @Test
    public void testLetArrayObject() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("y", null), "d", null), "2"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("y", null), "e", null), "a"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("y", null), "f", null), "1"));
        analyzeDirectory(expected, "phase1/let/array_object");
    }


    @Test
    public void testMoveTest1() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/move/test1");
    }

    @Test
    public void testMoveTest2() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/move/test2");
    }

    @Test
    public void testMoveTest3() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/move/test3");
    }

    @Test
    public void testNativeFunctionReturnArray() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/native_function/return_array");
    }

    @Test
    public void testNativeFunctionReturnObject() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/native_function/return_object");
    }

    @Test
    public void testNativeFunctionTest1() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/native_function/test1");
    }

    @Test
    public void testNetInitTest1() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/net-init/test1");
    }

    @Test
    public void testNetInitTest2() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/net-init/test2");
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
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/net-merge/test1");
    }

    @Test
    public void testNetMergeTest2() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/net-merge/test2");
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
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/net-remove/test1");
    }

    @Test
    public void testRealWorld0() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/real_world_0");
    }

    @Test
    public void testRealWorld1() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/real_world_1");
    }

    @Test
    public void testFor() throws Exception {
        final InstanceExpression nodeInstance = new InstanceExpression("node", null);
        Commands expected = new Commands()
                .addCommand(new InstanceCommand("node", new TypeExpression(null, "JavaNode", new VersionExpression(0), null)))
                .addCommand(new AddCommand(MODEL_ROOT, nodeInstance))
                .addCommand(new SetCommand(new DictionaryPathExpression(nodeInstance, "conf", null), "a"))
                .addCommand(new InstanceCommand("node", new TypeExpression(null, "JavaNode", new VersionExpression(1), null)))
                .addCommand(new AddCommand(MODEL_ROOT, nodeInstance))
                .addCommand(new SetCommand(new DictionaryPathExpression(nodeInstance, "conf", null), "b"))
                .addCommand(new InstanceCommand("node", new TypeExpression(null, "JavaNode", new VersionExpression(2), null)))
                .addCommand(new AddCommand(MODEL_ROOT, nodeInstance))
                .addCommand(new SetCommand(new DictionaryPathExpression(nodeInstance, "conf", null), "c"));
        analyzeDirectory(expected, "phase1/for");
    }

    @Test
    public void testFunctionReturn() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/function_return");
    }

    @Test
    public void testImportByFilesNonQualifiedTest1() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/import_by_files/non_qualified/test1");
    }

    @Test
    public void testImportByFilesNonQualifiedTest2() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/import_by_files/non_qualified/test2");
    }

    @Test
    public void testImportByFilesQualifiedTest1() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/import_by_files/qualified/test1");
    }

    @Test
    public void testImportByFilesQualifiedTest2() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/import_by_files/qualified/test2");
    }

    @Test
    public void testImportByFilesNonQualifiedError1() throws Exception {
        exception.expect(ResourceNotFoundException.class);
        exception.expectMessage("doesnotexists.kevs not found");
        final String basePath = "/phase1/import_by_files/";
        interpretPhase1(basePath, pathToString(basePath + "error1.kevs"));
    }

    @Test
    public void testImportByFilesNonQualifiedNameColision1() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage("a already declared in this scope");
        final String basePath = "/phase1/import_by_files/non_qualified/name_colision_1";
        interpretPhase1(getClass().getResource(basePath).getPath(), pathToString(basePath + "/new.kevs"));
    }

    @Test
    public void testImportByFilesNonQualifiedNameColision2() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage("a already declared in this scope");
        final String basePath = "/phase1/import_by_files/non_qualified/name_colision_2";
        interpretPhase1(getClass().getResource(basePath).getPath(), pathToString(basePath + "/new.kevs"));
    }

    @Test
    public void testImportByFilesQualifiedNameColision1() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage("test already declared in this scope");
        final String basePath = "/phase1/import_by_files/qualified/name_colision_1";
        interpretPhase1(getClass().getResource(basePath).getPath(), pathToString(basePath + "/new.kevs"));
    }

    @Test
    public void testImportByFilesQualifiedNameColision2() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage("test already declared in this scope");
        final String basePath = "/phase1/import_by_files/qualified/name_colision_2";
        interpretPhase1(getClass().getResource(basePath).getPath(), pathToString(basePath + "/new.kevs"));
    }

    @Test
    public void testImportByFilesNonQualifiedImportButDoesNotExists() throws Exception {
        exception.expect(ImportException.class);
        exception.expectMessage("a not found in \"dep.kevs\"");
        final String basePathS = "/phase1/import_by_files/import_but_does_not_exists";
        final String basePath = getClass().getResource(basePathS).getPath();
        interpretPhase1(basePath, pathToString(basePathS + "/main.kevs"));
    }

    @Test
    public void testImportByFilesNonQualifiedImportButNotExported() throws Exception {
        exception.expect(ImportException.class);
        exception.expectMessage("a not found in \"dep.kevs\"");
        final String basePathS = "/phase1/import_by_files/import_but_not_exported";
        final String basePath = getClass().getResource(basePathS).getPath();
        interpretPhase1(basePath, pathToString(basePathS + "/main.kevs"));
    }


    @Test
    public void testImportByHttpNonQualifiedTest1() throws Exception {
        final HttpServer httpServer = new HttpServer(8083, "phase1/import_by_http/non_qualified/test1/http");
        httpServer.buildAndStartServer();
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/import_by_files/non_qualified/test1");
        httpServer.stop();
    }

    @Test
    public void testImportByHttpNonQualifiedError1() throws Exception {
        final String basePath = "phase1/import_by_http/";
        final HttpServer httpServer = new HttpServer(8080, basePath);
        try {
            httpServer.buildAndStartServer();
            exception.expect(ResourceNotFoundException.class);
            exception.expectMessage("http:localhost:8080/doesnotexists.kevs not found");
            interpretPhase1(basePath, pathToString("/" + basePath + "error1.kevs"));
        } finally {
            httpServer.stop();
        }
    }

    @Test
    public void testForFunctionReturn() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("node0", new TypeExpression(null, "JavaNode", null, null)))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression("node0", null)))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("node0", null), "dico", null), "1"))
                .addCommand(new InstanceCommand("node1", new TypeExpression(null, "JavaNode", null, null)))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression("node1", null)))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("node1", null), "dico", null), "1"))
                .addCommand(new InstanceCommand("node2", new TypeExpression(null, "JavaNode", null, null)))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression("node2", null)))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("node2", null), "dico", null), "1"));

        analyzeDirectory(expected, "phase1/for_function_return");
    }

    @Test
    public void testFunction() throws Exception {
        final Commands expected = new Commands(); // TODO
        analyzeDirectory(expected, "phase1/function");
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
        final TypeExpression typeJavaNode = new TypeExpression(null, "JavaNode", null, null);
        final TypeExpression typeJavaNodeV42 = new TypeExpression(null, "JavaNode", new VersionExpression(42), null);
        final TypeExpression typeWSGroup = new TypeExpression(null, "WSGroup", null, null);
        final TypeExpression typeWSGroup2 = new TypeExpression(null, "WSGroup", new VersionExpression(2), new ObjectDeclExpression());
        final InstanceExpression instanceNode0 = new InstanceExpression("node0", null);
        final InstanceExpression instanceNode1 = new InstanceExpression("node1", null);
        final InstanceExpression instanceNode3 = new InstanceExpression("node3", null);
        final InstanceExpression instanceGroup0 = new InstanceExpression("group0", null);
        final InstanceExpression instanceGroup1 = new InstanceExpression("group1", null);
        final InstanceExpression instanceGroup2 = new InstanceExpression("group2", null);
        Commands expected = new Commands()
                .addCommand(new InstanceCommand("node0", typeJavaNode))
                .addCommand(new InstanceCommand("node1", typeJavaNode))
                .addCommand(new AddCommand(MODEL_ROOT, instanceNode0))
                .addCommand(new AddCommand(MODEL_ROOT, instanceNode1))
                .addCommand(new InstanceCommand("node3", typeJavaNodeV42))
                .addCommand(new AddCommand(MODEL_ROOT, instanceNode3))
                .addCommand(new InstanceCommand("group0", typeWSGroup))
                .addCommand(new InstanceCommand("group1", typeWSGroup2))
                .addCommand(new InstanceCommand("group2", typeWSGroup2))
                .addCommand(new AddCommand(MODEL_ROOT, instanceGroup0))
                .addCommand(new AddCommand(MODEL_ROOT, instanceGroup1))
                .addCommand(new AddCommand(MODEL_ROOT, instanceGroup2))
                .addCommand(new AttachCommand(instanceGroup0, instanceNode0))
                .addCommand(new AttachCommand(instanceGroup1, instanceNode1))
                .addCommand(new AttachCommand(instanceGroup2, instanceNode3));
        analyzeDirectory(expected, "phase1/attach_1");
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
        final TypeExpression typeJavaNode = new TypeExpression(null, "JavaNode", null, null);
        final TypeExpression typeJsNode = new TypeExpression(null, "JsNode", null, null);
        final TypeExpression typeWSChan = new TypeExpression(null, "WSChan", null, null);
        final InstanceExpression chanInstance = new InstanceExpression("chan0", null);
        final InstanceExpression node0Instance = new InstanceExpression("node0", null);
        final InstanceExpression node1Instance = new InstanceExpression("node1", null);
        final InstanceExpression node0CompInstance = new InstanceExpression("node0:comp1", null);
        final InstanceExpression node1CompInstance = new InstanceExpression("node1:comp1", null);
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("node0", typeJavaNode))
                .addCommand(new InstanceCommand("node1", typeJsNode))
                .addCommand(new InstanceCommand("chan0", typeWSChan))
                .addCommand(new AddCommand(MODEL_ROOT, node0Instance))
                .addCommand(new AddCommand(MODEL_ROOT, node1Instance))
                .addCommand(new AddCommand(MODEL_ROOT, chanInstance))
                .addCommand(new BindCommand(chanInstance, new PortPathExpression(node0CompInstance, true, "input")))
                .addCommand(new BindCommand(chanInstance, new PortPathExpression(node1CompInstance, true, "input")));
        analyzeDirectory(expected, "phase1/bind/valid");
    }

    @Test
    public void testBindError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage("y is expected to be of type PortPathExpression but is NullExpression");
        interpretPhase1(pathToString("/phase1/bind/error1.kevs"));
    }

    @Test
    public void testBindError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage("node1 is expected to be of type PortPathExpression but is InstanceExpression");
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
        final InstanceExpression node0 = new InstanceExpression("node0", null);
        final InstanceExpression group0 = new InstanceExpression("group0", null);
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("node0", new TypeExpression(null, "JavaNode", null, null)))
                .addCommand(new InstanceCommand("group0", new TypeExpression(null, "WSGroup", null, null)))
                .addCommand(new AddCommand(MODEL_ROOT, node0))
                .addCommand(new AddCommand(MODEL_ROOT, group0))
                .addCommand(new DetachCommand(group0));
        analyzeDirectory(expected, "phase1/detach/valid");
    }

    @Test
    public void testRemoveManyInstances() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/remove/many_instances");
    }

    @Test
    public void testRemoveSingleInstance() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/remove/single_instance");
    }


    @Test
    public void testEmptyScript() throws Exception {
        analyzeDirectory(new Commands(), "phase1/empty_script");
    }

    @Test
    public void testFirstOrderFunction() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "d"));
        analyzeDirectory(expected, "phase1/first_order_function");
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
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/start/test1");
    }

    @Test
    public void testStopTest1() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/stop/test1");
    }

    @Test
    public void testUnbind() throws Exception {
        Commands expected = null; // TODO
        analyzeDirectory(expected, "phase1/unbind/valid");
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
        Commands expected = null; // TODO
        analyzeDirectory(expected, "/phase1/variable_scope");
    }

    private void analyzeDirectory(final Commands expected, final String path) throws IOException {
        final String pathB;
        if (path.startsWith("/")) {
            pathB = path.substring(1);
        } else {
            pathB = path;
        }

        final String basePathStr = "/" + pathB;
        final String basePath = getClass().getResource(basePathStr).getPath();
        final String newStr = pathToString(basePathStr + "/new.kevs");
        assertEquals(expected, interpretPhase1(basePath, newStr));
    }

    private String pathToString(String name1) throws IOException {
        final InputStream newKev = getClass().getResourceAsStream(name1);
        return IOUtils.toString(newKev);
    }

    private Commands interpretPhase1(String expression) {
        return this.interpretPhase1(null, expression);
    }

    private Commands interpretPhase1(final String basePath, String expression) {
        return new KevscriptInterpreter().interpret(expression, new KevscriptVisitor(new RootContext(basePath)));
    }
}
