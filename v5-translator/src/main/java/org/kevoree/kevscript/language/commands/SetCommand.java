package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.DictionaryPathExpression;

/**
 *
 */
public class SetCommand implements ICommand {
    public DictionaryPathExpression dicPathExpr;
    public String value;

    public SetCommand(DictionaryPathExpression dicPathExpr, String value) {
        this.dicPathExpr = dicPathExpr;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SetCommand{" +
                "dicPathExpr=" + dicPathExpr +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetCommand that = (SetCommand) o;

        if (dicPathExpr != null ? !dicPathExpr.equals(that.dicPathExpr) : that.dicPathExpr != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = dicPathExpr != null ? dicPathExpr.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
