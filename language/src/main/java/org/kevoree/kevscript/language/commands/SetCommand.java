package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.DictionaryPathExpression;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 */
public class SetCommand implements Command {
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
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitSetCommand(this, context);
    }
}
