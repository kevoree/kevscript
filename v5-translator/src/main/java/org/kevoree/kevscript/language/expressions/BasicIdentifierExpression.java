package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 21/03/16.
 */
public class BasicIdentifierExpression implements NonFinalExpression {
    private final String text;

    public BasicIdentifierExpression(String text) {
        this.text = text;
    }

    @Override
    public String toPath() {
        return text;
    }
}
