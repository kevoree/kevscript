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
}
