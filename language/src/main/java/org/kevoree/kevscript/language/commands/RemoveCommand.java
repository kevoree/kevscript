package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 */
public class RemoveCommand implements Command {
    public final InstanceExpression instance;

    public RemoveCommand(final InstanceExpression instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "RemoveCommand{" +
                "instance=" + instance +
                '}';
    }

    @Override
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitRemoveCommand(this, context);
    }
}
