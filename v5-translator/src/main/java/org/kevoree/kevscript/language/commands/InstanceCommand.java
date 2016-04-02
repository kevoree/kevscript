package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.TypeExpression;

/**
 *
 * Created by leiko on 4/1/16.
 */
public class InstanceCommand extends AbstractCommand {

    private String name;
    private TypeExpression typeExpr;

    public InstanceCommand(String name, TypeExpression typeExpr) {
        this.name = name;
        this.typeExpr = typeExpr;
    }

    @Override
    public String toString() {
        return "instance " + this.name + " " + typeExpr.toText();
    }
}
