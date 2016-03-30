package org.kevoree.kevscript.language.expressions.finalexp;

/**
 * Created by mleduc on 02/03/16.
 */
public class StringExpression implements FinalExpression {
    public final String text;
    private boolean exported;

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

    @Override
    public boolean isExported() {
        return this.exported;
    }

    @Override
    public void setExported(boolean exported) {
        this.exported = exported;
    }
}
