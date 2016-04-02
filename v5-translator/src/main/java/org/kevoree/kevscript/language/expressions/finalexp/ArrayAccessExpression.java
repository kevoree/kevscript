package org.kevoree.kevscript.language.expressions.finalexp;

public class ArrayAccessExpression implements FinalExpression {
    private final long index;

    public ArrayAccessExpression(final long index) {
        this.index = index;
    }

    @Override
    public String toText() {
        return "[" + index + "]";
    }
}

