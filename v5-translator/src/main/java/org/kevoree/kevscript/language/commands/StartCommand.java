package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

/**
 *
 *
 */
public class StartCommand extends AbstractCommand {
    public final InstanceExpression instance;

    public StartCommand(InstanceExpression instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "start " + instance.toText();
    }
}
