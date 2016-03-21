package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by mleduc on 15/03/16.
 */
public class ArrayAccessExpression extends Expression {


    private final long index;

    public ArrayAccessExpression(final String id, final long index) {
        this.name = id;
        this.index = index;
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

