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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttachCommand that = (AttachCommand) o;

        if (group != null ? !group.equals(that.group) : that.group != null) return false;
        return node != null ? node.equals(that.node) : that.node == null;

    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + (node != null ? node.hashCode() : 0);
        return result;
    }

    @Override
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitAttachCommand(this);
    }
}
