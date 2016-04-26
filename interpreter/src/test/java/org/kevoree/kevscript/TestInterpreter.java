package org.kevoree.kevscript;

import org.junit.Test;
import org.kevoree.Model;
import org.kevoree.kevscript.interpreter.KevScriptInterpreter;
import org.kevoree.modeling.KCallback;

/**
 *
 */
public class TestInterpreter {

    @Test
    public void testInstance() {
        KevScriptInterpreter kevs = new KevScriptInterpreter();
        kevs.interpret("instance node0 = JavaNode", new KCallback<Model>() {
            @Override
            public void on(Model model) {
                // TODO
                System.out.println(model.toJSON());
            }
        });
    }
}
