package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 *
 */
public class NetMergeCommand implements Command {

    public final InstanceExpression instance;
    public final ObjectDeclExpression network;

    public NetMergeCommand(final InstanceExpression instance, final ObjectDeclExpression network) {
        this.instance = instance;
        this.network = network;
    }

    @Override
    public String toString() {
        return "NetMergeCommand{" +
                "instance=" + instance +
                ", keys=" + network +
                '}';
    }

    @Override
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitNetMergeCommand(this, context);
    }
}
