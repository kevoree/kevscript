package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

/**
 *
 *
 */
public class DetachCommand implements ICommand {
    public final InstanceExpression instance;

    public DetachCommand(final InstanceExpression instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "DetachCommand{" +
                "instance=" + instance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetachCommand that = (DetachCommand) o;

        return instance != null ? instance.equals(that.instance) : that.instance == null;

    }

    @Override
    public int hashCode() {
        return instance != null ? instance.hashCode() : 0;
    }
}
