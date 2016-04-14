package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

import java.util.List;

/**
 *
 *
 */
public class NetRemoveCommand extends AbstractCommand {
    public final InstanceExpression instance;
    public final List<String> keys;

    public NetRemoveCommand(final InstanceExpression instance, final List<String> keys) {
        this.instance = instance;
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "NetRemoveCommand{" +
                "instance=" + instance +
                ", keys=" + keys +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetRemoveCommand that = (NetRemoveCommand) o;

        if (instance != null ? !instance.equals(that.instance) : that.instance != null) return false;
        return keys != null ? keys.equals(that.keys) : that.keys == null;

    }

    @Override
    public int hashCode() {
        int result = instance != null ? instance.hashCode() : 0;
        result = 31 * result + (keys != null ? keys.hashCode() : 0);
        return result;
    }
}
