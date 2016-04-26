package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 *
 */
public class MoveCommand implements Command {
    public final InstanceExpression target;
    public final InstanceExpression source;

    public MoveCommand(InstanceExpression target, InstanceExpression source) {
        this.target = target;
        this.source = source;
    }

    @Override
    public String toString() {
        return "MoveCommand{" +
                "source=" + source +
                ", target=" + target +
                '}';
    }

    @Override
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitMoveCommand(this, context);
    }
}
