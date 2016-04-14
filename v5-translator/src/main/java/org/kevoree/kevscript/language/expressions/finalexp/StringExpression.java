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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringExpression that = (StringExpression) o;

        return text != null ? text.equals(that.text) : that.text == null;

    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
