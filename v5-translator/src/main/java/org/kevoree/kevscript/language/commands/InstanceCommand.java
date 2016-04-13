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
        return "InstanceCommand{" +
                "name='" + name + '\'' +
                ", typeExpr=" + typeExpr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceCommand that = (InstanceCommand) o;

        if (!name.equals(that.name)) return false;
        return typeExpr.equals(that.typeExpr);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + typeExpr.hashCode();
        return result;
    }
}
