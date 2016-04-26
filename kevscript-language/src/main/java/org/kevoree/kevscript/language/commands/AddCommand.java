package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class AddCommand implements ICommand {

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
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitAddCommand(this);
    }
}
