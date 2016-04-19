package org.kevoree.kevscript.language;

import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.excpt.*;
import org.kevoree.kevscript.language.expressions.finalexp.*;
import org.kevoree.kevscript.language.utils.FileTestUtil;
import org.kevoree.kevscript.language.utils.HttpServer;

import java.util.ArrayList;
import java.util.List;

import static org.kevoree.kevscript.language.utils.FileTestUtil.MODEL_ROOT;

/**
 * Preconditions :
 * - grammatically valid script
 * <p>
 * Controls :
 * - local variable names collision
 * - does not throw error on missing instance names (this will be check on a next phase)
 */
public class Phase1Test {

    private final FileTestUtil fileTestUtil = new FileTestUtil();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAdd0() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("node0", new TypeExpression(null, "JavaNode", new VersionExpression(1), null)))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression("node0", null)))
                .addCommand(new InstanceCommand("node1", new TypeExpression(null, "JavaNode", null, null)))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression("node1", null)));
        fileTestUtil.analyzeDirectory(expected, "phase1/add_0");
    }

    @Test
    public void testInstance1Error1() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage(CoreMatchers.equalTo("node0 already declared in this scope"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/instance_1/error1.kevs"));
    }

    @Test
    public void testLetRec1() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("x:g", null), "c", null), "a"));
        fileTestUtil.analyzeDirectory(expected, "phase1/let/rec1");
    }

    @Test
    public void testLetError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("a is expected to be of type InstanceExpression [l: 3]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/let/rec_error/error1.kevs"));
    }

    @Test
    public void testLetError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("b is expected to be of type InstanceExpression [l: 3]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/let/rec_error/error2.kevs"));
    }

    @Test
    public void testLetArray() throws Exception {
        final DictionaryPathExpression dicPathExpr = new DictionaryPathExpression(new InstanceExpression("x:y", null), "d", null);
        final Commands expected = new Commands()
                .addCommand(new SetCommand(dicPathExpr, "a"))
                .addCommand(new SetCommand(dicPathExpr, "b"))
                .addCommand(new SetCommand(dicPathExpr, "c"));
        fileTestUtil.analyzeDirectory(expected, "phase1/let/array");
    }

    @Test
    public void testLetObjects() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("x:y", null), "d", null), "2"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("x:y", null), "e", null), "3"));
        fileTestUtil.analyzeDirectory(expected, "phase1/let/objects");
    }

    @Test
    public void testLetArrayObject() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("x:y", null), "d", null), "2"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("x:y", null), "e", null), "a"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("x:y", null), "f", null), "1"));
        fileTestUtil.analyzeDirectory(expected, "phase1/let/array_object");
    }


    @Test
    public void testMoveTest1() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new MoveCommand(new InstanceExpression("a", null), new InstanceExpression("b", null)))
                .addCommand(new MoveCommand(new InstanceExpression("a:b", null), new InstanceExpression("b:b", null)));
        fileTestUtil.analyzeDirectory(expected, "phase1/move/test1");
    }

    @Test
    public void testMoveTest2() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("test", new TypeExpression(null, "JavaNode", null, null)))
                .addCommand(new MoveCommand(new InstanceExpression("a", null), new InstanceExpression("b", null)))
                .addCommand(new MoveCommand(new InstanceExpression("a:b", null), new InstanceExpression("test:b", null)));
        fileTestUtil.analyzeDirectory(expected, "phase1/move/test2");
    }

    @Test
    public void testMoveTest3() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("test", new TypeExpression(null, "Ticker", null, null)))
                .addCommand(new MoveCommand(new InstanceExpression("a", null), new InstanceExpression("b:b", null)))
                .addCommand(new MoveCommand(new InstanceExpression("a", null), new InstanceExpression("test", null)));
        fileTestUtil.analyzeDirectory(expected, "phase1/move/test3");
    }

    @Test
    public void testNativeFunctionReturnArray() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "0.0"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "10.0"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "20.0"));
        fileTestUtil.analyzeDirectory(expected, "phase1/native_function/return_array");
    }

    @Test
    public void testNativeFunctionReturnObject() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "0.0"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "x", null), "100"));
        fileTestUtil.analyzeDirectory(expected, "phase1/native_function/return_object");
    }

    @Test
    public void testNativeFunctionTest1() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("a", null), "b", null), "81.0"));
        fileTestUtil.analyzeDirectory(expected, "phase1/native_function/test1");
    }

    @Test
    public void testNetInitTest1() throws Exception {
        final ObjectDeclExpression network = new ObjectDeclExpression();
        final ObjectDeclExpression wlan0Value = new ObjectDeclExpression();
        wlan0Value.put("ip", new StringExpression("192.168.1.1"));
        network.put("wlan0", wlan0Value);
        final Commands expected = new Commands()
                .addCommand(new NetInitCommand(new InstanceExpression("node0", null), network));
        fileTestUtil.analyzeDirectory(expected, "phase1/net-init/test1");
    }

    @Test
    public void testNetInitTest2() throws Exception {
        final ObjectDeclExpression network = new ObjectDeclExpression();
        final ObjectDeclExpression wlan0Value = new ObjectDeclExpression();
        wlan0Value.put("ip", new StringExpression("192.168.1.1"));
        network.put("wlan0", wlan0Value);
        final Commands expected = new Commands()
                .addCommand(new NetInitCommand(new InstanceExpression("node0", null), network));
        fileTestUtil.analyzeDirectory(expected, "phase1/net-init/test2");
    }

    @Test
    public void testNetInitError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("b is expected to be of type ObjectDeclExpression but is StringExpression [l: 2]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/net-init/error1.kevs"));
    }

    @Test
    public void testNetInitError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("b is expected to be of type ObjectDeclExpression but is InstanceExpression [l: 2]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/net-init/error2.kevs"));
    }

    @Test
    public void testNetMergeTest1() throws Exception {
        final ObjectDeclExpression network = new ObjectDeclExpression();
        final ObjectDeclExpression wlan0Value = new ObjectDeclExpression();
        wlan0Value.put("ip", new StringExpression("192.168.1.1"));
        network.put("wlan0", wlan0Value);
        final Commands expected = new Commands()
                .addCommand(new NetMergeCommand(new InstanceExpression("node0", null), network));
        fileTestUtil.analyzeDirectory(expected, "phase1/net-merge/test1");
    }


    @Test
    public void testMetaMergeTest1() throws Exception {
        final ObjectDeclExpression network = new ObjectDeclExpression();
        final ObjectDeclExpression wlan0Value = new ObjectDeclExpression();
        wlan0Value.put("ip", new StringExpression("192.168.1.1"));
        network.put("wlan0", wlan0Value);
        final Commands expected = new Commands()
                .addCommand(new MetaMergeCommand(new InstanceExpression("node0", null), network));
        fileTestUtil.analyzeDirectory(expected, "phase1/meta-merge/test1");
    }

    @Test
    public void testMetaMergeTest2() throws Exception {
        final ObjectDeclExpression network = new ObjectDeclExpression();
        final ObjectDeclExpression wlan0Value = new ObjectDeclExpression();
        wlan0Value.put("ip", new StringExpression("192.168.1.1"));
        network.put("wlan0", wlan0Value);
        final Commands expected = new Commands()
                .addCommand(new MetaMergeCommand(new InstanceExpression("node0", null), network));
        fileTestUtil.analyzeDirectory(expected, "phase1/meta-merge/test2");
    }

    @Test
    public void testNetMergeTest2() throws Exception {
        final ObjectDeclExpression network = new ObjectDeclExpression();
        final ObjectDeclExpression wlan0Value = new ObjectDeclExpression();
        wlan0Value.put("ip", new StringExpression("192.168.1.1"));
        network.put("wlan0", wlan0Value);
        final Commands expected = new Commands()
                .addCommand(new NetMergeCommand(new InstanceExpression("node0", null), network));
        fileTestUtil.analyzeDirectory(expected, "phase1/net-merge/test2");
    }

    @Test
    public void testNetMergeError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("b is expected to be of type ObjectDeclExpression but is StringExpression [l: 2]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/net-merge/error1.kevs"));
    }

    @Test
    public void testNetMergeError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("b is expected to be of type ObjectDeclExpression but is InstanceExpression [l: 2]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/net-merge/error2.kevs"));
    }

    @Test
    public void testmetaMergeError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("b is expected to be of type ObjectDeclExpression but is StringExpression [l: 2]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/meta-merge/error1.kevs"));
    }

    @Test
    public void testMetaMergeError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("b is expected to be of type ObjectDeclExpression but is InstanceExpression [l: 2]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/meta-merge/error2.kevs"));
    }

    @Test
    public void testNetRemoveTest1() throws Exception {
        final List<String> keys = new ArrayList<>();
        keys.add("a.b");
        final List<String> keys2 = new ArrayList<>();
        keys2.add("c");
        keys2.add("d.e.f");
        final Commands expected = new Commands()
                .addCommand(new NetRemoveCommand(new InstanceExpression("node0", null), keys))
                .addCommand(new InstanceCommand("node2", new TypeExpression(null, "DotnetNode", null, null)))
                .addCommand(new NetRemoveCommand(new InstanceExpression("node2", null), keys2));
        fileTestUtil.analyzeDirectory(expected, "phase1/net-remove/test1");
    }

    @Test
    public void testMetaRemoveTest1() throws Exception {
        final List<String> keys = new ArrayList<>();
        keys.add("a.b");
        final List<String> keys2 = new ArrayList<>();
        keys2.add("c");
        keys2.add("d.e.f");
        final Commands expected = new Commands()
                .addCommand(new MetaRemoveCommand(new InstanceExpression("node0", null), keys))
                .addCommand(new InstanceCommand("node2", new TypeExpression(null, "DotnetNode", null, null)))
                .addCommand(new MetaRemoveCommand(new InstanceExpression("node2", null), keys2));
        fileTestUtil.analyzeDirectory(expected, "phase1/meta-remove/test1");
    }

    @Test
    public void testFor() throws Exception {
        final InstanceExpression nodeInstance = new InstanceExpression("node", null);
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("node", new TypeExpression(null, "JavaNode", new VersionExpression(0), null)))
                .addCommand(new AddCommand(MODEL_ROOT, nodeInstance))
                .addCommand(new SetCommand(new DictionaryPathExpression(nodeInstance, "conf", null), "a"))
                .addCommand(new InstanceCommand("node", new TypeExpression(null, "JavaNode", new VersionExpression(1), null)))
                .addCommand(new AddCommand(MODEL_ROOT, nodeInstance))
                .addCommand(new SetCommand(new DictionaryPathExpression(nodeInstance, "conf", null), "b"))
                .addCommand(new InstanceCommand("node", new TypeExpression(null, "JavaNode", new VersionExpression(2), null)))
                .addCommand(new AddCommand(MODEL_ROOT, nodeInstance))
                .addCommand(new SetCommand(new DictionaryPathExpression(nodeInstance, "conf", null), "c"));
        fileTestUtil.analyzeDirectory(expected, "phase1/for");
    }

    @Test
    public void testFunctionReturn() throws Exception {
        final InstanceExpression instanceNode0 = new InstanceExpression("node0", null);
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("node0", new TypeExpression(null, "JavaNode", null, null)))
                .addCommand(new AddCommand(MODEL_ROOT, instanceNode0))
                .addCommand(new SetCommand(new DictionaryPathExpression(instanceNode0, "a", null), "b"));
        fileTestUtil.analyzeDirectory(expected, "phase1/function_return");
    }

    @Test
    public void testImportByFilesNonQualifiedTest1() throws Exception {
        final Commands expected = new Commands().addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "ok"));
        fileTestUtil.analyzeDirectory(expected, "phase1/import_by_files/non_qualified/test1");
    }

    @Test
    public void testImportByFilesNonQualifiedTest2() throws Exception {
        final Commands expected = new Commands().addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "ok"));
        fileTestUtil.analyzeDirectory(expected, "phase1/import_by_files/non_qualified/test2");
    }

    @Test
    public void testImportByFilesQualifiedTest1() throws Exception {
        final Commands expected = new Commands().addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "ok"));
        fileTestUtil.analyzeDirectory(expected, "phase1/import_by_files/qualified/test1");
    }

    @Test
    public void testImportByFilesQualifiedTest2() throws Exception {
        final Commands expected = new Commands().addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "ok"));
        fileTestUtil.analyzeDirectory(expected, "phase1/import_by_files/qualified/test2");
    }

    @Test
    public void testImportByFilesNonQualifiedError1() throws Exception {
        exception.expect(ResourceNotFoundException.class);
        exception.expectMessage(CoreMatchers.equalTo("doesnotexists.kevs not found"));
        final String basePath = "/phase1/import_by_files/";
        this.fileTestUtil.interpretPhase1(basePath, this.fileTestUtil.pathToString(basePath + "error1.kevs"));
    }

    @Test
    public void testImportByFilesNonQualifiedNameColision1() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage(CoreMatchers.equalTo("a already declared in this scope"));
        final String basePath = "/phase1/import_by_files/non_qualified/name_colision_1";
        this.fileTestUtil.interpretPhase1(getClass().getResource(basePath).getPath(), this.fileTestUtil.pathToString(basePath + "/new.kevs"));
    }

    @Test
    public void testImportByFilesNonQualifiedNameColision2() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage(CoreMatchers.equalTo("a already declared in this scope"));
        final String basePath = "/phase1/import_by_files/non_qualified/name_colision_2";
        this.fileTestUtil.interpretPhase1(getClass().getResource(basePath).getPath(), this.fileTestUtil.pathToString(basePath + "/new.kevs"));
    }

    @Test
    public void testImportByFilesQualifiedNameColision1() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage(CoreMatchers.equalTo("test already declared in this scope"));
        final String basePath = "/phase1/import_by_files/qualified/name_colision_1";
        this.fileTestUtil.interpretPhase1(getClass().getResource(basePath).getPath(), this.fileTestUtil.pathToString(basePath + "/new.kevs"));
    }

    @Test
    public void testImportByFilesQualifiedNameColision2() throws Exception {
        exception.expect(NameCollisionException.class);
        exception.expectMessage(CoreMatchers.equalTo("test already declared in this scope"));
        final String basePath = "/phase1/import_by_files/qualified/name_colision_2";
        this.fileTestUtil.interpretPhase1(getClass().getResource(basePath).getPath(), this.fileTestUtil.pathToString(basePath + "/new.kevs"));
    }

    @Test
    public void testImportByFilesNonQualifiedImportButDoesNotExists() throws Exception {
        exception.expect(ImportException.class);
        exception.expectMessage(CoreMatchers.equalTo("a not found in \"dep.kevs\""));
        final String basePathS = "/phase1/import_by_files/import_but_does_not_exists";
        final String basePath = getClass().getResource(basePathS).getPath();
        this.fileTestUtil.interpretPhase1(basePath, this.fileTestUtil.pathToString(basePathS + "/main.kevs"));
    }

    @Test
    public void testImportByFilesNonQualifiedImportButNotExported() throws Exception {
        exception.expect(ImportException.class);
        exception.expectMessage(CoreMatchers.equalTo("a not found in \"dep.kevs\""));
        final String basePathS = "/phase1/import_by_files/import_but_not_exported";
        final String basePath = getClass().getResource(basePathS).getPath();
        this.fileTestUtil.interpretPhase1(basePath, this.fileTestUtil.pathToString(basePathS + "/main.kevs"));
    }


    @Test
    @Ignore
    public void testImportByHttpNonQualifiedTest1() throws Exception {
        final HttpServer httpServer = new HttpServer(8083, "phase1/import_by_http/non_qualified/test1/http");
        httpServer.buildAndStartServer();
        Commands expected = null; // TODO
        fileTestUtil.analyzeDirectory(expected, "phase1/import_by_files/non_qualified/test1");
        httpServer.stop();
    }

    @Test
    @Ignore
    public void testImportByHttpNonQualifiedError1() throws Exception {
        final String basePath = "phase1/import_by_http/";
        final HttpServer httpServer = new HttpServer(8080, basePath);
        try {
            httpServer.buildAndStartServer();
            exception.expect(ResourceNotFoundException.class);
            exception.expectMessage(CoreMatchers.equalTo("http:localhost:8080/doesnotexists.kevs not found"));
            this.fileTestUtil.interpretPhase1(basePath, this.fileTestUtil.pathToString("/" + basePath + "error1.kevs"));
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

        fileTestUtil.analyzeDirectory(expected, "phase1/for_function_return");
    }

    @Test
    public void testFunction() throws Exception {
        final InstanceExpression instanceNode0 = new InstanceExpression("node0", null);
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("node0", new TypeExpression(null, "JavascriptNode", new VersionExpression(1), null)))
                .addCommand(new AddCommand(MODEL_ROOT, instanceNode0))
                .addCommand(new SetCommand(new DictionaryPathExpression(instanceNode0, "y", null), "ok1"))
                .addCommand(new SetCommand(new DictionaryPathExpression(instanceNode0, "z", null), "e1"))
                .addCommand(new InstanceCommand("node0", new TypeExpression(null, "JavascriptNode", new VersionExpression(2), null)))
                .addCommand(new AddCommand(MODEL_ROOT, instanceNode0))
                .addCommand(new SetCommand(new DictionaryPathExpression(instanceNode0, "y", null), "ok2"))
                .addCommand(new SetCommand(new DictionaryPathExpression(instanceNode0, "z", null), "e2"))
                .addCommand(new InstanceCommand("node0", new TypeExpression(null, "JavascriptNode", new VersionExpression(3), null)))
                .addCommand(new AddCommand(MODEL_ROOT, instanceNode0))
                .addCommand(new SetCommand(new DictionaryPathExpression(instanceNode0, "y", null), "ok3"))
                .addCommand(new SetCommand(new DictionaryPathExpression(instanceNode0, "z", null), "e3"));
        fileTestUtil.analyzeDirectory(expected, "phase1/function");
    }

    @Test
    public void testFunctionError1() throws Exception {
        exception.expect(WrongNumberOfArguments.class);
        exception.expectMessage(CoreMatchers.equalTo("method a expected 0 arguments but got 1"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/function_error/function_err_1.kevs"));
    }

    @Test
    public void testFunctionError2() throws Exception {
        exception.expect(WrongNumberOfArguments.class);
        exception.expectMessage(CoreMatchers.equalTo("method b expected 1 arguments but got 0"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/function_error/function_err_2.kevs"));
    }

    @Test
    public void testAttach0Test1() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/attach_0/test1.kevs"));
    }

    @Test
    public void testAttach0Test2() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/attach_0/test2.kevs"));
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
        fileTestUtil.analyzeDirectory(expected, "phase1/attach_1");
    }

    @Test
    public void testDetachTest1() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/detach/test1.kevs"));
    }

    @Test
    public void testDetachTest2() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/detach/test2.kevs"));
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
        fileTestUtil.analyzeDirectory(expected, "phase1/bind/valid");
    }

    @Test
    public void testBindError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("y is expected to be of type PortPathExpression but is NullExpression [l: 1]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/bind/error1.kevs"));
    }

    @Test
    public void testBindError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("node1 is expected to be of type PortPathExpression but is InstanceExpression [l: 3]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/bind/error2.kevs"));
    }

    @Test
    public void testBindTest1() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/bind/test1.kevs"));
    }

    @Test
    public void testBindTest2() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/bind/test2.kevs"));
    }

    @Test
    public void testBindTest3() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/bind/test3.kevs"));
    }

    @Test
    public void testBindTest4() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/bind/test4.kevs"));
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
        fileTestUtil.analyzeDirectory(expected, "phase1/detach/valid");
    }

    @Test
    public void testRemoveManyInstances() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("test", new TypeExpression(null, "WSGroup", new VersionExpression(2), null)))
                .addCommand(new RemoveCommand(new InstanceExpression("a:b", null)))
                .addCommand(new RemoveCommand(new InstanceExpression("b", null)))
                .addCommand(new RemoveCommand(new InstanceExpression("test", null)));
        fileTestUtil.analyzeDirectory(expected, "phase1/remove/many_instances");
    }

    @Test
    public void testRemoveSingleInstance() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new RemoveCommand(new InstanceExpression("a:b", null)))
                .addCommand(new RemoveCommand(new InstanceExpression("b", null)))
                .addCommand(new InstanceCommand("test", new TypeExpression(null, "WSGroup", null, null)))
                .addCommand(new RemoveCommand(new InstanceExpression("test", null)))
                .addCommand(new RemoveCommand(new InstanceExpression("a:b", null)))
                .addCommand(new RemoveCommand(new InstanceExpression("b", null)))
                .addCommand(new RemoveCommand(new InstanceExpression("test", null)));
        fileTestUtil.analyzeDirectory(expected, "phase1/remove/single_instance");
    }


    @Test
    public void testEmptyScript() throws Exception {
        fileTestUtil.analyzeDirectory(new Commands(), "phase1/empty_script");
    }

    @Test
    public void testFirstOrderFunction() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "d"));
        fileTestUtil.analyzeDirectory(expected, "phase1/first_order_function");
    }

    @Test
    public void testSetError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("conflict is expected to be of type StringExpression [l: 5]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/set/error1.kevs"));
    }

    @Test
    public void testSetOk1() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/set/ok1.kevs"));
    }

    @Test
    public void testStartTest1() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("test", new TypeExpression(null, "WSGroup", null, null)))
                .addCommand(new StartCommand(new InstanceExpression("a", null)))
                .addCommand(new StartCommand(new InstanceExpression("b:c", null)))
                .addCommand(new StartCommand(new InstanceExpression("test", null)));
        fileTestUtil.analyzeDirectory(expected, "phase1/start/test1");
    }

    @Test
    public void testStopTest1() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("test", new TypeExpression(null, "WSGroup", null, null)))
                .addCommand(new StopCommand(new InstanceExpression("a", null)))
                .addCommand(new StopCommand(new InstanceExpression("b:c", null)))
                .addCommand(new StopCommand(new InstanceExpression("test", null)));
        fileTestUtil.analyzeDirectory(expected, "phase1/stop/test1");
    }

    @Test
    public void testUnbind() throws Exception {
        final InstanceExpression node0 = new InstanceExpression("node0", null);
        final InstanceExpression node1 = new InstanceExpression("node1", null);
        final InstanceExpression chan0 = new InstanceExpression("chan0", null);
        final Commands expected = new Commands()
                .addCommand(new InstanceCommand("node0", new TypeExpression(null, "JavaNode", null, null)))
                .addCommand(new InstanceCommand("node1", new TypeExpression(null, "JSNode", null, null)))
                .addCommand(new InstanceCommand("chan0", new TypeExpression(null, "WSChan", null, null)))
                .addCommand(new AddCommand(MODEL_ROOT, node0))
                .addCommand(new AddCommand(MODEL_ROOT, node1))
                .addCommand(new AddCommand(MODEL_ROOT, chan0))
                .addCommand(new UnbindCommand(chan0, new PortPathExpression(node0, true, "port0")))
                .addCommand(new UnbindCommand(chan0, new PortPathExpression(node1, false, "port1")));
        fileTestUtil.analyzeDirectory(expected, "phase1/unbind/valid");
    }

    @Test
    public void testUnbindError1() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("node1 is expected to be of type PortPathExpression but is NullExpression [l: 3]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/unbind/error1.kevs"));
    }

    @Test
    public void testUnbindError2() throws Exception {
        exception.expect(WrongTypeException.class);
        exception.expectMessage(CoreMatchers.equalTo("node1 is expected to be of type PortPathExpression but is InstanceExpression [l: 3]"));
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/unbind/error2.kevs"));
    }

    @Test
    public void testUnbindTest1() throws Exception {
        this.fileTestUtil.interpretPhase1(this.fileTestUtil.pathToString("/phase1/unbind/test1.kevs"));
    }

    @Test
    public void testVariableScope() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "a"));
        fileTestUtil.analyzeDirectory(expected, "/phase1/variable_scope");
    }

    @Test
    public void testValueAfterFunctionReturn() throws Exception {
        final Commands expected = new Commands()
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "w", null), "a"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "x", null), "a"))
                .addCommand(new SetCommand(new DictionaryPathExpression(new InstanceExpression("u:v", null), "z", null), "b"));

        this.fileTestUtil.validateFile(expected, "/phase1", "value_after_function_return.kevs");
    }

    @Test
    public void testTimeTest1() throws Exception {
        final Commands expected = new Commands().addCommand(new TimeCommand(1, new Commands()));
        this.fileTestUtil.validateFile(expected, "/phase1/time", "test1.kevs");
    }

    @Test
    public void testTimeTest2() throws Exception {
        final Commands expected = new Commands().addCommand(new TimeCommand(1, new Commands()));
        this.fileTestUtil.validateFile(expected, "/phase1/time", "test2.kevs");
    }

    @Test
    public void testWorldTest1() throws Exception {
        final Commands expected = new Commands().addCommand(new WorldCommand(1, new Commands()));
        this.fileTestUtil.validateFile(expected, "/phase1/world", "test1.kevs");
    }

    @Test
    public void testWorldTest2() throws Exception {
        final Commands expected = new Commands().addCommand(new WorldCommand(1, new Commands()));
        this.fileTestUtil.validateFile(expected, "/phase1/world", "test2.kevs");
    }

    @Test
    public void testConcat() throws Exception {
        final InstanceExpression instance = new InstanceExpression("u:v", null);
        final DictionaryPathExpression dicPathExpr = new DictionaryPathExpression(instance, "w", null);
        final Commands expected = new Commands()
                .addCommand(new SetCommand(dicPathExpr, "ab"))
                .addCommand(new SetCommand(dicPathExpr, "ab"))
                .addCommand(new SetCommand(dicPathExpr, "ab"))
                .addCommand(new SetCommand(dicPathExpr, "ab"))
                .addCommand(new SetCommand(dicPathExpr, "ab"))
                .addCommand(new SetCommand(dicPathExpr, "a1"))
                .addCommand(new SetCommand(dicPathExpr, "1b"))
                .addCommand(new SetCommand(dicPathExpr, "11"));
        this.fileTestUtil.validateFile(expected, "/phase1", "concat.kevs");
    }
}
