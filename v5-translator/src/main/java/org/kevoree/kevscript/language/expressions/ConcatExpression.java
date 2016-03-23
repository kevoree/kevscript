package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by mleduc on 15/03/16.
 */
public class ConcatExpression implements NonFinalExpression {
    private final FinalExpression left;
    private final FinalExpression right;

    public ConcatExpression(final FinalExpression left, final FinalExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toPath() {
        throw new IllegalStateException("Concat can't be used as a selector");
    }

}
