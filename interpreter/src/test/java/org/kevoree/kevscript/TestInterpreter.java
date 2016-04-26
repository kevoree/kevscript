package org.kevoree.kevscript;

import org.junit.Test;
import org.kevoree.Model;
import org.kevoree.kevscript.interpreter.KevScriptInterpreter;
import org.kevoree.kevscript.stub.StubRegistryResolver;
import org.kevoree.modeling.KCallback;

/**
 *
 */
public class TestInterpreter {

    @Test
    public void testInstance() {
        KevScriptInterpreter kevs = new KevScriptInterpreter(new StubRegistryResolver());
        StringBuilder script = new StringBuilder();
        script.append("instance node0 = JavaNode\n");
        script.append("add node0\n");
        kevs.interpret(script.toString(), new KCallback<Model>() {
            @Override
            public void on(Model model) {
                // TODO
                System.out.println(model.toJSON());
            }
        });
    }
}
