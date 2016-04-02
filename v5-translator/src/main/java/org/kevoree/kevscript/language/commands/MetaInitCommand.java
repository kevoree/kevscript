package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;

/**
 *
 *
 */
public class MetaInitCommand extends AbstractCommand {
    public final InstanceExpression instance;
    public final ObjectDeclExpression object;

    public MetaInitCommand(final InstanceExpression instance, final ObjectDeclExpression object) {
        this.instance = instance;
        this.object = object;
    }

    @Override
    public String toString() {
        return "meta-init " + instance.toText() + " " + object.toText();
    }
}
