package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.TypeExpression;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 */
public class InstanceCommand implements Command {

    public final String name;
    public final TypeExpression typeExpr;

    public InstanceCommand(String name, TypeExpression typeExpr) {
        this.name = name;
        this.typeExpr = typeExpr;
    }

    @Override
    public String toString() {
        return "InstanceCommand{" +
                "name='" + name + '\'' +
                ", typeExpr=" + typeExpr +
                '}';
    }

    @Override
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitInstanceCommand(this, context);
    }
}
