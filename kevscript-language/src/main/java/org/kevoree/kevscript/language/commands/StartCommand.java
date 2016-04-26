package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class StartCommand implements ICommand {
    public final InstanceExpression instance;

    public StartCommand(InstanceExpression instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "StartCommand{" +
                "instance=" + instance +
                '}';
    }

    @Override
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return null;
    }
}
