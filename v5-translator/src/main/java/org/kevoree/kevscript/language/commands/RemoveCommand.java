package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

/**
 *
 * Created by mleduc on 17/03/16.
 */
public class RemoveCommand extends AbstractCommand {
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
}
