package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;

/**
 *
 *
 */
public class MetaRemoveCommand extends AbstractCommand {
    public final InstanceExpression instance;
    public final ObjectDeclExpression object;

    public MetaRemoveCommand(final InstanceExpression instance, final ObjectDeclExpression object) {
        this.instance = instance;
        this.object = object;
    }

    @Override
    public String toString() {
        return "MetaRemoveCommand{" +
                "instance=" + instance +
                ", object=" + object +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetaRemoveCommand that = (MetaRemoveCommand) o;

        if (instance != null ? !instance.equals(that.instance) : that.instance != null) return false;
        return object != null ? object.equals(that.object) : that.object == null;

    }

    @Override
    public int hashCode() {
        int result = instance != null ? instance.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }
}
