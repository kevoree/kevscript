package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class MetaInitCommand implements ICommand {
    public final InstanceExpression instance;
    public final ObjectDeclExpression object;

    public MetaInitCommand(final InstanceExpression instance, final ObjectDeclExpression object) {
        this.instance = instance;
        this.object = object;
    }

    @Override
    public String toString() {
        return "MetaInitCommand{" +
                "instance=" + instance +
                ", object=" + object +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetaInitCommand that = (MetaInitCommand) o;

        if (instance != null ? !instance.equals(that.instance) : that.instance != null) return false;
        return object != null ? object.equals(that.object) : that.object == null;

    }

    @Override
    public int hashCode() {
        int result = instance != null ? instance.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }

    @Override
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitMetaInitCommand(this);
    }
}
