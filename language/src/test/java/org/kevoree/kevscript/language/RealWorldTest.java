package org.kevoree.kevscript.language;

import org.junit.Test;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.expressions.finalexp.*;
import org.kevoree.kevscript.language.utils.FileTestUtil;

import static org.kevoree.kevscript.language.utils.FileTestUtil.MODEL_ROOT;

/**
 *
 */
public class RealWorldTest {

    private final FileTestUtil fileTestUtil = new FileTestUtil();

    @Test
    public void testRealWorld0() throws Exception {
        fileTestUtil.analyzeDirectory(expectedRealWorld(), "phase1/real_world_0");
    }

    @Test
    public void testRealWorld1() throws Exception {
        fileTestUtil.analyzeDirectory(expectedRealWorld(), "phase1/real_world_1");
    }

    private Commands expectedRealWorld() {
        final InstanceExpression mainGroupInstance = new InstanceExpression("mainGroup");

        final Commands commands = new Commands()
                .addCommand(new InstanceCommand("mainGroup", new TypeExpression(null, "RemoteWSGroup", null, null)))
                .addCommand(new AddCommand(MODEL_ROOT, mainGroupInstance))
                .addCommand(new SetCommand(new DictionaryPathExpression(mainGroupInstance, "host", null), "ws.kevoree.org"))
                .addCommand(new SetCommand(new DictionaryPathExpression(mainGroupInstance, "path", null), "/edisons"))
                .addCommand(new SetCommand(new DictionaryPathExpression(mainGroupInstance, "answerPull", "edison2"), "false"))
                .addCommand(new SetCommand(new DictionaryPathExpression(mainGroupInstance, "answerPull", "edison3"), "false"))
                .addCommand(new SetCommand(new DictionaryPathExpression(mainGroupInstance, "answerPull", "edison4"), "false"))
                .addCommand(new SetCommand(new DictionaryPathExpression(mainGroupInstance, "answerPull", "edison5"), "false"));
        final Commands edison1 = initEdison(commands, mainGroupInstance, "edison1");
        final Commands edison2 = initEdison(edison1, mainGroupInstance, "edison2");
        final Commands edison3 = initEdison(edison2, mainGroupInstance, "edison3");
        final Commands edison4 = initEdison(edison3, mainGroupInstance, "edison4");
        final Commands edison5 = initEdison(edison4, mainGroupInstance, "edison5");
        final Commands forLoop1 = forLoop(edison5, "web1", mainGroupInstance, new InstanceExpression("edison1"));
        final Commands forLoop2 = forLoop(forLoop1, "web2", mainGroupInstance, new InstanceExpression("edison2"));
        final Commands forLoop3 = forLoop(forLoop2, "web3", mainGroupInstance, new InstanceExpression("edison3"));
        return forLoop3;
    }

    private Commands initEdison(final Commands abstractCommands, final InstanceExpression mainGroupInstance, final String nodeName) {
        final InstanceExpression lcdInstance = new InstanceExpression("lcd");
        final InstanceExpression ledInstance = new InstanceExpression("led");
        final InstanceExpression noiseInstance = new InstanceExpression("noise");
        final InstanceExpression lightInstance = new InstanceExpression("light");
        final InstanceExpression tempInstance = new InstanceExpression("temp");
        final InstanceExpression edison1Instance = new InstanceExpression(nodeName);
        return abstractCommands
                .addCommand(new InstanceCommand(nodeName, new TypeExpression(null, "JavascriptNode", new VersionExpression(533), null)))
                .addCommand(new InstanceCommand("lcd", new TypeExpression("eu_heads", "HeadsLCDDisplayComp", new VersionExpression(2), null)))
                .addCommand(new InstanceCommand("led", new TypeExpression("eu_heads", "HeadsDigitalWriteComp", new VersionExpression(2), null)))
                .addCommand(new InstanceCommand("noise", new TypeExpression("eu_heads", "HeadsAnalogSensorComp", new VersionExpression(2), null)))
                .addCommand(new InstanceCommand("light", new TypeExpression("eu_heads", "HeadsAnalogSensorComp", new VersionExpression(2), null)))
                .addCommand(new AddCommand(edison1Instance, lcdInstance))
                .addCommand(new AddCommand(edison1Instance, ledInstance))
                .addCommand(new AddCommand(edison1Instance, noiseInstance))
                .addCommand(new AddCommand(edison1Instance, lightInstance))
                .addCommand(new SetCommand(new DictionaryPathExpression(lcdInstance, "test", null), "false"))
                .addCommand(new SetCommand(new DictionaryPathExpression(ledInstance, "pin", null), "7"))
                .addCommand(new SetCommand(new DictionaryPathExpression(ledInstance, "test", null), "false"))
                .addCommand(new SetCommand(new DictionaryPathExpression(lightInstance, "pin", null), "2"))
                .addCommand(new SetCommand(new DictionaryPathExpression(lightInstance, "test", null), "false"))
                .addCommand(new SetCommand(new DictionaryPathExpression(noiseInstance, "test", null), "false"))
                .addCommand(new SetCommand(new DictionaryPathExpression(tempInstance, "pin", null), "1"))
                .addCommand(new SetCommand(new DictionaryPathExpression(tempInstance, "test", null), "false"))
                .addCommand(new AttachCommand(mainGroupInstance, edison1Instance));
    }

    private Commands forLoop(Commands commands, String nodeName, InstanceExpression mainGroupInstance, InstanceExpression edison1) {
        final InstanceExpression webInstance = new InstanceExpression(nodeName);
        final Commands initWebNode = commands
                .addCommand(new InstanceCommand(nodeName, new TypeExpression(null, "JavascriptNode", new VersionExpression(533), null)))
                .addCommand(new InstanceCommand("chart1", new TypeExpression(null, "Chart", new VersionExpression(2), null)))
                .addCommand(new InstanceCommand("chart2", new TypeExpression(null, "Chart", new VersionExpression(2), null)))
                .addCommand(new InstanceCommand("chart3", new TypeExpression(null, "Chart", new VersionExpression(2), null)))
                .addCommand(new AddCommand(MODEL_ROOT, webInstance))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression(nodeName + ":chart1")))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression(nodeName + ":chart2")))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression(nodeName + ":chart3")))
                .addCommand(new AttachCommand(mainGroupInstance, webInstance));
        final Commands ret = bindByChan(initWebNode, new PortPathExpression(new InstanceExpression(webInstance.instanceName + ":chart2"), true, "input"), new PortPathExpression(new InstanceExpression(edison1.instanceName + ":temp"), false, "out"), "chan", "edison1Temp");
        final Commands ret1 = bindByChan(ret, new PortPathExpression(new InstanceExpression(webInstance.instanceName + ":chart3"), true, "input"), new PortPathExpression(new InstanceExpression(edison1.instanceName + ":light"), false, "out"), "chan1", "edison1Light");
        final Commands ret2 = bindByChan(ret1, new PortPathExpression(new InstanceExpression(webInstance.instanceName + ":chart1"), true, "input"), new PortPathExpression(new InstanceExpression(edison1.instanceName + ":noise"), false, "out"), "chan2", "edison1Noise");
        return ret2;
    }

    private Commands bindByChan(final Commands initWebNode, final PortPathExpression port0, final PortPathExpression port1, final String chanName, final String uuid) {
        final InstanceExpression chanInstance = new InstanceExpression(chanName);
        return initWebNode
                .addCommand(new InstanceCommand(chanName, new TypeExpression(null, "RemoteWSChan", new VersionExpression(5), null)))
                .addCommand(new AddCommand(MODEL_ROOT, chanInstance))
                .addCommand(new SetCommand(new DictionaryPathExpression(chanInstance, "host", null), "ws.kevoree.org"))
                .addCommand(new SetCommand(new DictionaryPathExpression(chanInstance, "uuid", null), uuid))
                .addCommand(new BindCommand(chanInstance, port0))
                .addCommand(new BindCommand(chanInstance, port1));
    }
}
