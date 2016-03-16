package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;
import org.kevoree.kevscript.language.context.Context;

/**
 * Created by mleduc on 15/03/16.
 */
public class ConcatExpression extends Expression {
    private final Expression left;
    private final Expression right;

    public ConcatExpression(final Expression left, final Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toText() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Expression resolve(Context context) {
        throw new NotImplementedException("TODO");
    }
}
