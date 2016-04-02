package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.DictionaryPathExpression;

/**
 *
 */
public class SetCommand extends AbstractCommand {
    public DictionaryPathExpression dicPathExpr;
    public String value;

    public SetCommand(DictionaryPathExpression dicPathExpr, String value) {
        this.dicPathExpr = dicPathExpr;
        this.value = value;
    }

    @Override
    public String toString() {
        return "set " + dicPathExpr.toText() + " = " + value;
    }
}
