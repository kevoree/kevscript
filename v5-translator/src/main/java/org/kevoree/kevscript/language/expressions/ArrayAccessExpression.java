package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by mleduc on 15/03/16.
 */
public class ArrayAccessExpression implements NonFinalExpression {


    private final long index;
    private final String id;

    public ArrayAccessExpression(final String id, final long index) {
        this.id = id; // probably useless ?
        this.index = index;
    }

    @Override
    public String toPath() {
        return this.id + "["+index+"]";

    }

}

