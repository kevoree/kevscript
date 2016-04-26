package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class MoveCommand implements ICommand {
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
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitMoveCommand(this);
    }
}
