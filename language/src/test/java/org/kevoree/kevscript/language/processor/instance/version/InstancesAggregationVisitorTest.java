package org.kevoree.kevscript.language.processor.instance.version;

import org.junit.Test;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.commands.InstanceCommand;
import org.kevoree.kevscript.language.commands.TimeCommand;
import org.kevoree.kevscript.language.commands.WorldCommand;
import org.kevoree.kevscript.language.expressions.finalexp.TypeExpression;
import org.unitils.reflectionassert.ReflectionAssert;

import java.util.ArrayList;

/**
 *
 */
public class InstancesAggregationVisitorTest {

//    private final InstancesAggregationVisitor instancesAggregationVisitor = new InstancesAggregationVisitor(0, 0);

    @Test
    public void basicTest1() throws Exception {
//        final Commands commands = new Commands().addCommand(new InstanceCommand("a", new TypeExpression(null, "JavaNode", null, null)));
//        final ArrayList<InstanceContext> res = instancesAggregationVisitor.visitCommands(commands);
//
//        final ArrayList<InstanceContext> expected = new ArrayList<>();
//        expected.add(new InstanceContext(0, 0, new InstanceCommand("a", new TypeExpression(null, "JavaNode", null, null))));
//        ReflectionAssert.assertReflectionEquals(expected, res);
    }

    @Test
    public void basicMergeTest1() throws Exception {
//        final Commands commands = new Commands()
//                .addCommand(new InstanceCommand("a", new TypeExpression(null, "JavaNode", null, null)))
//                .addCommand(new InstanceCommand("b", new TypeExpression(null, "JavaNode", null, null)));
//        final ArrayList<InstanceContext> res = instancesAggregationVisitor.visitCommands(commands);
//
//        final ArrayList<InstanceContext> expected = new ArrayList<>();
//        expected.add(new InstanceContext(0, 0, new InstanceCommand("a", new TypeExpression(null, "JavaNode", null, null))));
//        expected.add(new InstanceContext(0, 0, new InstanceCommand("b", new TypeExpression(null, "JavaNode", null, null))));
//        ReflectionAssert.assertReflectionEquals(expected, res);
    }

    @Test
    public void basicWorldTest1() throws Exception {
//        final Commands worldCommands = new Commands().addCommand(new InstanceCommand("b", new TypeExpression(null, "JavaNode", null, null)));
//        final Commands commands = new Commands()
//                .addCommand(new InstanceCommand("a", new TypeExpression(null, "JavaNode", null, null)))
//                .addCommand(new WorldCommand(1, worldCommands))
//                .addCommand(new InstanceCommand("c", new TypeExpression(null, "JavaNode", null, null)));
//        final ArrayList<InstanceContext> res = instancesAggregationVisitor.visitCommands(commands);
//
//        final ArrayList<InstanceContext> expected = new ArrayList<>();
//        expected.add(new InstanceContext(0, 0, new InstanceCommand("a", new TypeExpression(null, "JavaNode", null, null))));
//        expected.add(new InstanceContext(1, 0, new InstanceCommand("b", new TypeExpression(null, "JavaNode", null, null))));
//        expected.add(new InstanceContext(0, 0, new InstanceCommand("c", new TypeExpression(null, "JavaNode", null, null))));
//        ReflectionAssert.assertReflectionEquals(expected, res);
    }

    @Test
    public void basicTimeTest1() throws Exception {
//        final Commands timeCommands = new Commands().addCommand(new InstanceCommand("b", new TypeExpression(null, "JavaNode", null, null)));
//        final Commands commands = new Commands()
//                .addCommand(new InstanceCommand("a", new TypeExpression(null, "JavaNode", null, null)))
//                .addCommand(new TimeCommand(1, timeCommands))
//                .addCommand(new InstanceCommand("c", new TypeExpression(null, "JavaNode", null, null)));
//        final ArrayList<InstanceContext> res = instancesAggregationVisitor.visitCommands(commands);
//
//        final ArrayList<InstanceContext> expected = new ArrayList<>();
//        expected.add(new InstanceContext(0, 0, new InstanceCommand("a", new TypeExpression(null, "JavaNode", null, null))));
//        expected.add(new InstanceContext(0, 1, new InstanceCommand("b", new TypeExpression(null, "JavaNode", null, null))));
//        expected.add(new InstanceContext(0, 0, new InstanceCommand("c", new TypeExpression(null, "JavaNode", null, null))));
//        ReflectionAssert.assertReflectionEquals(expected, res);
    }
}
