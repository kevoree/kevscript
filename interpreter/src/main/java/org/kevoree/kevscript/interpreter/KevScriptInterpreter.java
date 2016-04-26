package org.kevoree.kevscript.interpreter;

import org.KevoreeModel;
import org.KevoreeView;
import org.kevoree.Model;
import org.kevoree.kevscript.language.commands.Command;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.visitors.helper.KevScriptHelper;
import org.kevoree.modeling.KCallback;
import org.kevoree.modeling.memory.manager.DataManagerBuilder;

/**
 *
 */
public class KevScriptInterpreter {

    public void interpret(final String script, final KCallback<Model> callback) {
        final KevoreeModel kModel = new KevoreeModel(DataManagerBuilder.buildDefault());
        kModel.connect(new KCallback() {
            @Override
            public void on(Object o) {
                System.out.println("yo ?");
                KevoreeView kView = kModel.universe(0).time(0);
                Model model = kView.createModel();
                Commands commands = KevScriptHelper.interpret(script);
                for (Command cmd : commands) {
                    // TODO we are supposed to execute command here with the model in the context
                    System.out.println(cmd.toString());
                }
                callback.on(model);
            }
        });
    }

//    public void parse()
}
