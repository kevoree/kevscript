package org.kevoree.kevscript;

import org.KevoreeModel;
import org.junit.Before;
import org.junit.Test;
import org.kevoree.kevscript.interpreter.KevScriptInterpreter;
import org.kevoree.kevscript.interpreter.ModelContext;
import org.kevoree.kevscript.language.processor.visitor.VisitCallback;
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
                kevs.interpret(script.toString(), kModel, new VisitCallback<ModelContext>() {
                    @Override
                    public void done(ModelContext context) {
                        System.out.println(context.model.toString());
                        kModel.universe(0).time(0).json().save(context.model, new KCallback<String>() {
                            @Override
                            public void on(String modelStr) {
                                assertEquals("[\n" +
                                        "{\"@class\":\"org.kevoree.Model\",\"@uuid\":2,\"name\":\"/\",\"nodes\":[4]},\n" +
                                        "{\"@class\":\"org.kevoree.Node\",\"@uuid\":4,\"name\":\"node0\",\"op_Model_nodes\":[2],\"typeDefinition\":[3]},\n" +
                                        "{\"@class\":\"org.kevoree.NodeType\",\"@uuid\":3,\"name\":\"JavaNode\",\"description\":\"No description\",\"version\":\"1\",\"op_Node_typeDefinition\":[4]}\n" +
                                        "]", modelStr.trim());

                            }
                        });
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
                kevs.interpret(script.toString(), kModel, new VisitCallback<ModelContext>() {
                    @Override
                    public void done(ModelContext context) {
                        kModel.universe(0).time(0).json().save(context.model, new KCallback<String>() {
                            @Override
                            public void on(String modelStr) {
                                assertEquals("[\n" +
                                        "{\"@class\":\"org.kevoree.Model\",\"@uuid\":2,\"name\":\"/\",\"nodes\":[4]},\n" +
                                        "{\"@class\":\"org.kevoree.Node\",\"@uuid\":4,\"name\":\"node0\",\"op_Model_nodes\":[2],\"components\":[6],\"typeDefinition\":[3]},\n" +
                                        "{\"@class\":\"org.kevoree.Component\",\"@uuid\":6,\"name\":\"comp\",\"typeDefinition\":[5],\"host\":[4]},\n" +
                                        "{\"@class\":\"org.kevoree.NodeType\",\"@uuid\":3,\"name\":\"JavaNode\",\"description\":\"No description\",\"version\":\"1\",\"op_Node_typeDefinition\":[4]},\n" +
                                        "{\"@class\":\"org.kevoree.ComponentType\",\"@uuid\":5,\"name\":\"TickerComp\",\"description\":\"No description\",\"version\":\"42\",\"op_Component_typeDefinition\":[6]}\n" +
                                        "]", modelStr.trim());
                            }
                        });
                    }
                });
            }
        });
    }

    @Test
    public void testAddCompInstanceInAlreadyPresentNode() {
        final StringBuilder script = new StringBuilder();
        script.append("instance comp = TickerComp/42\n");
        script.append("add node0 comp\n");

        kModel.connect(new KCallback() {
            @Override
            public void on(Object o) {
                kevs.interpret(script.toString(), kModel, new VisitCallback<ModelContext>() {
                    @Override
                    public void done(ModelContext context) {
                        kModel.universe(0).time(0).json().save(context.model, new KCallback<String>() {
                            @Override
                            public void on(String modelStr) {
                                System.out.println(modelStr);
                            }
                        });
                    }
                });
            }
        });
    }
}
