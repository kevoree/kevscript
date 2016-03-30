package org.kevoree.kevscript.language.expressions.finalexp;

/**
 * Created by mleduc on 15/03/16.
 */
public class ArrayAccessExpression implements FinalExpression {


    private final long index;
    private final String id;

    public ArrayAccessExpression(final String id, final long index) {
        this.id = id;
        this.index = index;
    }

    @Override
    public String toText() {
        return id + "[" + index + "]";
    }
}

