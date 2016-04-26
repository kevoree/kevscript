package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class DetachCommand implements Command {
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
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitDetachCommand(this);
    }
}
