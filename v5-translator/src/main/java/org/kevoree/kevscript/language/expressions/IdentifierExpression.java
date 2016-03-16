package org.kevoree.kevscript.language.expressions;

import org.kevoree.kevscript.language.context.Context;

/**
 * Created by mleduc on 15/03/16.
 */
public class IdentifierExpression extends Expression {

    private final Expression left;
    private final Expression right;

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
    public Expression resolve(Context context) {
        return null;
    }
}
