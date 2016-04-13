package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;

/**
 *
 *
 */
public class NetInitCommand extends AbstractCommand {

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
                ", network=" + network +
                '}';
    }
}
