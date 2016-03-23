package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 02/03/16.
 */
public class StringExpression implements FinalExpression {
    public final String text;

    public StringExpression(final String text) {
        this.text = text.replaceAll("\"", "");
    }

    @Override
    public String toText() {
        return text;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
