package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 *
 */
public class AddCommand implements Command {

    public final InstanceExpression target;
    public final InstanceExpression source;

    public AddCommand(final InstanceExpression target, InstanceExpression source) {
        this.target = target;
        this.source = source;
    }

    @Override
    public String toString() {
        return "AddCommand{" +
                "source=" + source +
                ", target=" + target +
                '}';
    }

    @Override
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitAddCommand(this, context);
    }
}
