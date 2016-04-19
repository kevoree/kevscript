package org.kevoree.kevscript.language.expressions.finalexp;

/**
 *
 */
public class NumericExpression implements PrimitiveExpression {
    public final int value;

    public NumericExpression(final int value) {
        this.value = value;
    }

    @Override
    public String toText() {
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return "NumericExpression{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumericExpression that = (NumericExpression) o;

        return value == that.value;

    }

    @Override
    public int hashCode() {
        return value;
    }
}
