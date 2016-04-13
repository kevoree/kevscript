package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

/**
 *
 *
 */
public class DetachCommand extends AbstractCommand {
    public final InstanceExpression instance;

    public DetachCommand(final InstanceExpression instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "DetachCommand{" +
                "instance=" + instance +
                '}';
    }
}
