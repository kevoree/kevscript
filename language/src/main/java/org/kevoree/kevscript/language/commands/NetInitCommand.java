package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class NetInitCommand implements Command {

    public final InstanceExpression instance;
    public final ObjectDeclExpression network;

    public NetInitCommand(final InstanceExpression instance, final ObjectDeclExpression network) {
        this.instance = instance;
        this.network = network;
    }

    @Override
    public String toString() {
        return "NetInitCommand{" +
                "instance=" + instance +
                ", keys=" + network +
                '}';
    }

    @Override
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitNetInitCommand(this);
    }
}
