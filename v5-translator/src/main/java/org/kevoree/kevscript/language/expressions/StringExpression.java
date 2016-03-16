package org.kevoree.kevscript.language.expressions;

import org.kevoree.kevscript.language.context.Context;

/**
 * Created by mleduc on 02/03/16.
 */
public class StringExpression extends Expression {
    private final String text;

    public StringExpression(final String text) {
        this.text = text;
    }

    @Override
    public String toText() {
        return text;
    }

    @Override
    public Expression resolve(Context context) {
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
