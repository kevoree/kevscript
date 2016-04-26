package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class AttachCommand implements ICommand {
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
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitAttachCommand(this);
    }
}
