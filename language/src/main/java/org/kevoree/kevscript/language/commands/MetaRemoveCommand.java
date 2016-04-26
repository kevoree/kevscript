package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

import java.util.List;

/**
 *
 *
 */
public class MetaRemoveCommand implements Command {
    public final InstanceExpression instance;
    public final List<String> keys;

    public MetaRemoveCommand(InstanceExpression instance, List<String> keys) {
        this.instance = instance;
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "MetaRemoveCommand{" +
                "instance=" + instance +
                ", keys=" + keys +
                '}';
    }

    @Override
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitMetaRemoveCommand(this);
    }
}
