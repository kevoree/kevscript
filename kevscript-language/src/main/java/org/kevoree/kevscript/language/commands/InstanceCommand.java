package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.TypeExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 * Created by leiko on 4/1/16.
 */
public class InstanceCommand implements ICommand {

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
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitInstanceCommand(this);
    }
}
