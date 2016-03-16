package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 15/03/16.
 */
public class IdentifierExpression extends Expression {

    public final Expression left;
    public final Expression right;

    public IdentifierExpression(final Expression left) {
        this.left = left;
        this.right = null;
    }

    public IdentifierExpression(final Expression left, final Expression visit) {
        this.left = left;
        this.right = visit;
    }

    @Override
    public String toText() {
        return null;
    }

    @Override
    public boolean match(Expression identifier) {
        return false;
    }


}
