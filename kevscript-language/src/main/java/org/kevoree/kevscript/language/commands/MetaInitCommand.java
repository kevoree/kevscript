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
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitMetaInitCommand(this);
    }
}
