package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.TypeExpression;

/**
 *
 * Created by leiko on 4/1/16.
 */
public class InstanceCommand extends AbstractCommand {

    public final  String name;
    public final  TypeExpression typeExpr;

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

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return typeExpr != null ? typeExpr.equals(that.typeExpr) : that.typeExpr == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (typeExpr != null ? typeExpr.hashCode() : 0);
        return result;
    }
}
