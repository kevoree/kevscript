package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;

/**
 *
 *
 */
public class MetaMergeCommand extends AbstractCommand {
    public final InstanceExpression instance;
    public final ObjectDeclExpression object;

    public MetaMergeCommand(final InstanceExpression instance, final ObjectDeclExpression object) {
        this.instance = instance;
        this.object = object;
    }

    @Override
    public String toString() {
        return "MetaMergeCommand{" +
                "instance=" + instance +
                ", object=" + object +
                '}';
    }
}
