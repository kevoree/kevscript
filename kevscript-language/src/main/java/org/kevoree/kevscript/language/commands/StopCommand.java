package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class StopCommand implements ICommand {
    public final InstanceExpression instance;

    public StopCommand(InstanceExpression instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "StopCommand{" +
                "instance=" + instance +
                '}';
    }

    @Override
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitStopCommand(this);
    }
}
