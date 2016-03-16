package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 02/03/16.
 */
public abstract class Expression {
    /**
     *
     * @return a text representation of the expression
     */
    public abstract String toText();


    public abstract boolean match(Expression identifier);
}
