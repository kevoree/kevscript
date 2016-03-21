package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by mleduc on 15/03/16.
 */
public class PortPathExpression extends Expression {
    private final Expression leftExpression;
    private final boolean isLeft;
    private final Expression portName;

    public PortPathExpression(final Expression leftExpression, final boolean isLeft, final Expression portName) {
        this.leftExpression = leftExpression;
        this.isLeft = isLeft;
        this.portName = portName;
    }

    @Override
    public String toText() {
        throw new NotImplementedException("TODO");
    }


    @Override
    public boolean match(Expression identifier) {
        return false;
    }

}

