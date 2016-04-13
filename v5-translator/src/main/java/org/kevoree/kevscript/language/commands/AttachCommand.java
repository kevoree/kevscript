package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

/**
 *
 *
 */
public class AttachCommand extends AbstractCommand {
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
}
