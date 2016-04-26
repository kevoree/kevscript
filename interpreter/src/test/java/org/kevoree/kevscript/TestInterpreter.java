package org.kevoree.kevscript;

import org.KevoreeModel;
import org.junit.Before;
import org.junit.Test;
import org.kevoree.Model;
import org.kevoree.kevscript.interpreter.KevScriptInterpreter;
import org.kevoree.kevscript.stub.StubRegistryResolver;
import org.kevoree.modeling.KCallback;
import org.kevoree.modeling.memory.manager.DataManagerBuilder;
import org.kevoree.modeling.scheduler.impl.DirectScheduler;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TestInterpreter {

    private KevScriptInterpreter kevs;
    private KevoreeModel kModel;

    @Before
    public void setUp() {
        this.kevs = new KevScriptInterpreter(new StubRegistryResolver());
        this.kModel = new KevoreeModel(DataManagerBuilder.create().withScheduler(new DirectScheduler()).build());
    }

    @Test
    public void testInstance() {
        final StringBuilder script = new StringBuilder();
        script.append("instance node0 = JavaNode\n");
        script.append("add node0\n");

        kModel.connect(new KCallback() {
            @Override
            public void on(Object o) {
                Model model = kevs.interpret(script.toString(), kModel);
                kModel.universe(0).time(0).json().save(model, new KCallback<String>() {
                    @Override
                    public void on(String modelStr) {
                        assertEquals(modelStr.trim(), "[\n" +
                                "{\"@class\":\"org.kevoree.Model\",\"@uuid\":1,\"nodes\":[3]},\n" +
                                "{\"@class\":\"org.kevoree.Node\",\"@uuid\":3,\"name\":\"node0\",\"op_Model_nodes\":[1],\"typeDefinition\":[2]},\n" +
                                "{\"@class\":\"org.kevoree.NodeType\",\"@uuid\":2,\"name\":\"JavaNode\",\"description\":\"No description\",\"version\":\"1\",\"op_Node_typeDefinition\":[3]}\n" +
                                "]");

                    }
                });
            }
        });
    }

    @Test
    public void testInstanceComp() {
        final StringBuilder script = new StringBuilder();
        script.append("instance node0 = JavaNode\n");
        script.append("instance comp = TickerComp/42\n");
        script.append("add node0\n");
        script.append("add node0 comp\n");

        kModel.connect(new KCallback() {
            @Override
            public void on(Object o) {
                Model model = kevs.interpret(script.toString(), kModel);
                kModel.universe(0).time(0).json().save(model, new KCallback<String>() {
                    @Override
                    public void on(String modelStr) {
;                        assertEquals(modelStr.trim(), "[\n" +
                                "{\"@class\":\"org.kevoree.Model\",\"@uuid\":1,\"nodes\":[3]},\n" +
                                "{\"@class\":\"org.kevoree.Node\",\"@uuid\":3,\"name\":\"node0\",\"op_Model_nodes\":[1],\"components\":[5],\"typeDefinition\":[2]},\n" +
                                "{\"@class\":\"org.kevoree.Component\",\"@uuid\":5,\"name\":\"comp\",\"typeDefinition\":[4],\"host\":[3]},\n" +
                                "{\"@class\":\"org.kevoree.NodeType\",\"@uuid\":2,\"name\":\"JavaNode\",\"description\":\"No description\",\"version\":\"1\",\"op_Node_typeDefinition\":[3]},\n" +
                                "{\"@class\":\"org.kevoree.ComponentType\",\"@uuid\":4,\"name\":\"TickerComp\",\"description\":\"No description\",\"version\":\"42\",\"op_Component_typeDefinition\":[5]}\n" +
                                "]");
                    }
                });
            }
        });
    }
}
