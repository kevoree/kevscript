package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;

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
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetMergeCommand that = (NetMergeCommand) o;

        if (instance != null ? !instance.equals(that.instance) : that.instance != null) return false;
        if (network != null ? !network.equals(that.network) : that.network != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = instance != null ? instance.hashCode() : 0;
        result = 31 * result + (network != null ? network.hashCode() : 0);
        return result;
    }
}
