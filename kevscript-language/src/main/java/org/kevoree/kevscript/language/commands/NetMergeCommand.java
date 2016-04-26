package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class NetMergeCommand implements ICommand {

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
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitnetMergeCommand(this);
    }
}
