package org.kevoree.kevscript.interpreter;

import org.KevoreeModel;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.processor.visitor.VisitCallback;
import org.kevoree.kevscript.language.visitors.helper.KevScriptHelper;
import org.kevoree.kevscript.resolver.RegistryResolver;

/**
 *
 */
public class KevScriptInterpreter {

    private RegistryResolver resolver;

    public KevScriptInterpreter(RegistryResolver resolver) {
        this.resolver = resolver;
    }

    public void interpret(final String script, final KevoreeModel kModel, final VisitCallback<ModelContext> callback) {
        final CommandInterpreter cmdInterpreter = new CommandInterpreter(kModel, this.resolver);
        Commands commands = KevScriptHelper.interpret(script);
        cmdInterpreter.visitCommands(commands, callback);
    }
}
