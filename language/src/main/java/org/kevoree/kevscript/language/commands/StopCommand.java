package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 *
 */
public class StopCommand implements Command {
    public final InstanceExpression instance;

    public StopCommand(InstanceExpression instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "StopCommand{" +
                "instance=" + instance +
                '}';
    }

    @Override
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitStopCommand(this, context);
    }
}
