package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 21/03/16.
 */
public class BasicIdentifierExpression extends Expression {
    private final String text;

    public BasicIdentifierExpression(String text) {
        this.text = text;
    }

    @Override
    public String toText() {
        return text;
    }
}
