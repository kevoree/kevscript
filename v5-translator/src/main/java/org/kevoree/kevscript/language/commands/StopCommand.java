package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

/**
 *
 *
 */
public class StopCommand extends AbstractCommand {
    public final InstanceExpression instance;

    public StopCommand(InstanceExpression instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "stop " + instance.toText();
    }
}
