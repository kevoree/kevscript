package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 02/03/16.
 */
public abstract class Expression {

    protected String name;

    /**
     *
     * @return a text representation of the expression
     */
    public abstract String toText();

    public final String getName() {
        return this.name;
    }

    public abstract boolean match(Expression identifier);

    public final void setName(final String name) {
        this.name = name;
    }
}
