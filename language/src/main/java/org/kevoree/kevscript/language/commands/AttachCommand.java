package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 *
 */
public class AttachCommand implements Command {
    public final InstanceExpression group;
    public final InstanceExpression node;

    public AttachCommand(final InstanceExpression group, final InstanceExpression node) {
        this.group = group;
        this.node = node;
    }

    @Override
    public String toString() {
        return "AttachCommand{" +
                "group=" + group +
                ", node=" + node +
                '}';
    }

    @Override
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitAttachCommand(this, context);
    }
}
