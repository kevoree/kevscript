package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

/**
 *
 *
 */
public class MoveCommand extends AbstractCommand {
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
}
