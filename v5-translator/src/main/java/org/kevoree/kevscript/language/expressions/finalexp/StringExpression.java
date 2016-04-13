package org.kevoree.kevscript.language.expressions.finalexp;

/**
 *
 */
public class StringExpression implements FinalExpression {
    public final String text;

    public StringExpression(final String text) {
        this.text = text;
    }

    @Override
    public String toText() {
        return text;
    }

    @Override
    public String toString() {
        return "StringExpression{" +
                "text='" + text + '\'' +
                '}';
    }
}
