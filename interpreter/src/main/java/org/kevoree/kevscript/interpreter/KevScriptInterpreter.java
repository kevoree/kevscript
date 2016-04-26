package org.kevoree.kevscript.interpreter;

import org.KevoreeModel;
import org.kevoree.Model;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.visitors.helper.KevScriptHelper;
import org.kevoree.kevscript.resolver.RegistryResolver;
import org.kevoree.modeling.KCallback;
import org.kevoree.modeling.memory.manager.DataManagerBuilder;
import org.kevoree.modeling.scheduler.impl.DirectScheduler;

/**
 *
 */
public class KevScriptInterpreter {

    private RegistryResolver resolver;

    public KevScriptInterpreter(RegistryResolver resolver) {
        this.resolver = resolver;
    }

    public void interpret(final String script, final KCallback<Model> callback) {
        final KevoreeModel kModel = new KevoreeModel(
                DataManagerBuilder.create().withScheduler(new DirectScheduler()).build());
        final CommandInterpreter cmdInterpreter = new CommandInterpreter(kModel, this.resolver);


        kModel.connect(new KCallback() {
            @Override
            public void on(Object o) {
                Commands commands = KevScriptHelper.interpret(script);
                callback.on(cmdInterpreter.visitCommands(commands).model);
            }
        });
    }
}
