package org.kevoree.kevscript.interpreter;

import org.KevoreeModel;
import org.kevoree.Model;
import org.kevoree.kevscript.language.commands.Commands;
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

    public Model interpret(final String script, final KevoreeModel kModel) {
        final CommandInterpreter cmdInterpreter = new CommandInterpreter(kModel, this.resolver);
        Commands commands = KevScriptHelper.interpret(script);
         return cmdInterpreter.visitCommands(commands).model;
    }
}
