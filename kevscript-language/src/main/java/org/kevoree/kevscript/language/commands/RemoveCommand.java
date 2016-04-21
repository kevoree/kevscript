package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 * Created by mleduc on 17/03/16.
 */
public class RemoveCommand implements ICommand {
    public final InstanceExpression instance;

    public RemoveCommand(final InstanceExpression instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "RemoveCommand{" +
                "instance=" + instance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemoveCommand that = (RemoveCommand) o;

        return instance != null ? instance.equals(that.instance) : that.instance == null;

    }

    @Override
    public int hashCode() {
        return instance != null ? instance.hashCode() : 0;
    }

    @Override
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitRemoveCommand(this);
    }
}
