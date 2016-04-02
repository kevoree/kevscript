package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

/**
 *
 *
 */
public class AddCommand extends AbstractCommand {

    public final InstanceExpression target;
    public final InstanceExpression source;

    public AddCommand(final InstanceExpression target, InstanceExpression source) {
        this.target = target;
        this.source = source;
    }

    @Override
    public String toString() {
        String ret = "add ";
        if (target != null) {
            ret += target.toText() + " ";
        }
        if (source != null) {
            ret += source.toText();
        }
        return ret;
    }
}
