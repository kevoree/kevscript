package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

import java.util.List;

/**
 *
 *
 */
public class NetRemoveCommand implements Command {
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
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitNetRemoveCommand(this, context);
    }
}
