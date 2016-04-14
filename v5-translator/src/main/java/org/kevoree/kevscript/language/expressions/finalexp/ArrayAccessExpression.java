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

    @Override
    public String toString() {
        return "ArrayAccessExpression{" +
                "index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayAccessExpression that = (ArrayAccessExpression) o;

        return index == that.index;

    }

    @Override
    public int hashCode() {
        return (int) (index ^ (index >>> 32));
    }
}

