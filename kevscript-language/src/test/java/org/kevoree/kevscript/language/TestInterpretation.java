package org.kevoree.kevscript.language;

import org.junit.Test;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.expressions.finalexp.*;
import org.kevoree.kevscript.language.visitors.KevscriptVisitor;
import org.unitils.reflectionassert.ReflectionAssert;

public class TestInterpretation {

    public void test(String input, Commands expected) {
        KevscriptInterpreter interpreter = new KevscriptInterpreter();
        Commands cmds = interpreter.interpret(input, new KevscriptVisitor(null));
        ReflectionAssert.assertReflectionEquals(expected, cmds);
    }

    @Test
    public void testInstance1() {
        StringBuilder script = new StringBuilder();
        script.append("instance node0 = JavascriptNode\n");

        Commands cmds = new Commands();
        TypeExpression typeExpr = new TypeExpression(null, "JavascriptNode", null, null);
        cmds.addCommand(new InstanceCommand("node0", typeExpr));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testInstance2() {
        StringBuilder script = new StringBuilder();
        script.append("instance node0, node1 = JavascriptNode\n");

        Commands cmds = new Commands();
        TypeExpression typeExpr = new TypeExpression(null, "JavascriptNode", null, null);
        cmds.addCommand(new InstanceCommand("node0", typeExpr));
        cmds.addCommand(new InstanceCommand("node1", typeExpr));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testInstance3() {
        StringBuilder script = new StringBuilder();
        script.append("instance node0 = 'otherName' JavascriptNode\n");

        Commands cmds = new Commands();
        TypeExpression typeExpr = new TypeExpression(null, "JavascriptNode", null, null);
        cmds.addCommand(new InstanceCommand("otherName", typeExpr));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testInstance4() {
        final StringBuilder script = new StringBuilder();
        script.append("let array = ['one', 'two']\n");
        script.append("instance node0 = array[1] JavascriptNode\n");

        final Commands cmds = new Commands();
        final TypeExpression typeExpr = new TypeExpression(null, "JavascriptNode", null, null);
        cmds.addCommand(new InstanceCommand("two", typeExpr));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testInstance5() {
        StringBuilder script = new StringBuilder();
        script.append("function get() {\n");
        script.append("  return ['one', 'two']");
        script.append("}\n");
        script.append("instance a = get()[1] Ticker\n");

        Commands cmds = new Commands();
        TypeExpression typeExpr = new TypeExpression(null, "Ticker", null, null);
        cmds.addCommand(new InstanceCommand("two", typeExpr));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testAddInstance1() {
        StringBuilder script = new StringBuilder();
        script.append("instance node0 = 'otherName' JavascriptNode\n");
        script.append("add node0\n");

        Commands cmds = new Commands();
        TypeExpression typeExpr = new TypeExpression(null, "JavascriptNode", null, null);
        cmds.addCommand(new InstanceCommand("otherName", typeExpr));
        cmds.addCommand(new AddCommand(new InstanceExpression("/"), new InstanceExpression("otherName")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testAddInstance2() {
        StringBuilder script = new StringBuilder();
        script.append("instance node0 = 'otherName' JavascriptNode\n");
        script.append("add otherName\n");

        Commands cmds = new Commands();
        TypeExpression typeExpr = new TypeExpression(null, "JavascriptNode", null, null);
        cmds.addCommand(new InstanceCommand("otherName", typeExpr));
        cmds.addCommand(new AddCommand(new InstanceExpression("/"), new InstanceExpression("otherName")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testAddInstance3() {
        StringBuilder script = new StringBuilder();
        script.append("add node0 comp0\n");

        Commands cmds = new Commands();
        cmds.addCommand(new AddCommand(new InstanceExpression("node0"), new InstanceExpression("comp0")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testAddInstance4() {
        StringBuilder script = new StringBuilder();
        script.append("add node0 [comp0, node1:comp1]\n");

        Commands cmds = new Commands();
        cmds.addCommand(new AddCommand(new InstanceExpression("node0"), new InstanceExpression("comp0")));
        cmds.addCommand(new AddCommand(new InstanceExpression("node0"), new InstanceExpression("node1:comp1")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testSet1() {
        StringBuilder script = new StringBuilder();
        script.append("set node0#param = 'value'\n");

        Commands cmds = new Commands();
        InstanceExpression instance = new InstanceExpression("node0");
        DictionaryPathExpression paramExpr = new DictionaryPathExpression(instance, "param", null);
        cmds.addCommand(new SetCommand(paramExpr, "value"));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testSet2() {
        StringBuilder script = new StringBuilder();
        script.append("set node0:comp0#param = 'value'\n");

        Commands cmds = new Commands();
        InstanceExpression instance = new InstanceExpression("node0:comp0");
        DictionaryPathExpression paramExpr = new DictionaryPathExpression(instance, "param", null);
        cmds.addCommand(new SetCommand(paramExpr, "value"));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testSet3() {
        StringBuilder script = new StringBuilder();
        script.append("instance comp = Ticker\n");
        script.append("set comp#param = 'value'\n");

        Commands cmds = new Commands();
        InstanceExpression instance = new InstanceExpression("comp");
        DictionaryPathExpression paramExpr = new DictionaryPathExpression(instance, "param", null);
        TypeExpression typeExpr = new TypeExpression(null, "Ticker", null, null);
        cmds.addCommand(new InstanceCommand("comp", typeExpr));
        cmds.addCommand(new SetCommand(paramExpr, "value"));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testFunction1() {
        StringBuilder script = new StringBuilder();
        script.append("function myFunc() {\n");
        script.append("  return 'foo'\n");
        script.append("}\n");
        script.append("let a = myFunc()\n");
        script.append("instance node = a JavaNode");

        Commands cmds = new Commands();
        TypeExpression typeExpr = new TypeExpression(null, "JavaNode", null, null);
        cmds.addCommand(new InstanceCommand("foo", typeExpr));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testMove1() {
        StringBuilder script = new StringBuilder();
        script.append("move node0 node1:comp0\n");

        Commands cmds = new Commands();
        cmds.addCommand(new MoveCommand(new InstanceExpression("node0"),
                new InstanceExpression("node1:comp0")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testMove2() {
        StringBuilder script = new StringBuilder();
        script.append("move node0 [comp0, node1:comp1]\n");

        Commands cmds = new Commands();
        InstanceExpression target = new InstanceExpression("node0");
        cmds.addCommand(new MoveCommand(target, new InstanceExpression("comp0")));
        cmds.addCommand(new MoveCommand(target, new InstanceExpression("node1:comp1")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testDetach1() {
        StringBuilder script = new StringBuilder();
        script.append("detach node0\n");

        Commands cmds = new Commands();
        cmds.addCommand(new DetachCommand(new InstanceExpression("node0")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testDetach2() {
        StringBuilder script = new StringBuilder();
        script.append("detach [node0, node1]\n");

        Commands cmds = new Commands();
        cmds.addCommand(new DetachCommand(new InstanceExpression("node0")));
        cmds.addCommand(new DetachCommand(new InstanceExpression("node1")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testStart1() {
        StringBuilder script = new StringBuilder();
        script.append("start [node0, node1]\n");

        Commands cmds = new Commands();
        cmds.addCommand(new StartCommand(new InstanceExpression("node0")));
        cmds.addCommand(new StartCommand(new InstanceExpression("node1")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testStop1() {
        StringBuilder script = new StringBuilder();
        script.append("stop [node0, node1]\n");

        Commands cmds = new Commands();
        cmds.addCommand(new StopCommand(new InstanceExpression("node0")));
        cmds.addCommand(new StopCommand(new InstanceExpression("node1")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testUnbind1() {
        StringBuilder script = new StringBuilder();
        script.append("unbind chan node0:comp<-input\n");

        Commands cmds = new Commands();
        cmds.addCommand(new UnbindCommand(new InstanceExpression("chan"),
                new PortPathExpression(new InstanceExpression("node0:comp"), true, "input")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testUnbind2() {
        StringBuilder script = new StringBuilder();
        script.append("function unbindPort(port) {\n");
        script.append("  unbind chan port\n");
        script.append("}\n");
        script.append("unbindPort(node0:comp<-input)\n");

        Commands cmds = new Commands();
        cmds.addCommand(new UnbindCommand(new InstanceExpression("chan"),
                new PortPathExpression(new InstanceExpression("node0:comp"), true, "input")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testBind1() {
        StringBuilder script = new StringBuilder();
        script.append("bind chan node0:comp->output\n");

        Commands cmds = new Commands();
        cmds.addCommand(new BindCommand(new InstanceExpression("chan"),
                new PortPathExpression(new InstanceExpression("node0:comp"), false, "output")));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testMetaInit1() {
        StringBuilder script = new StringBuilder();
        script.append("meta-init node0 {}\n");

        Commands cmds = new Commands();
        ObjectDeclExpression objDecl = new ObjectDeclExpression();
        cmds.addCommand(new MetaInitCommand(new InstanceExpression("node0"),
                objDecl));

        this.test(script.toString(), cmds);
    }

    @Test
    public void testMetaInit2() {
        StringBuilder script = new StringBuilder();
        script.append("meta-init node0 { foo: 'bar' }\n");

        Commands cmds = new Commands();
        ObjectDeclExpression objDecl = new ObjectDeclExpression();
        objDecl.put("foo", new StringExpression("bar"));
        cmds.addCommand(new MetaInitCommand(new InstanceExpression("node0"),
                objDecl));

        this.test(script.toString(), cmds);
    }
}