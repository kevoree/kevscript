package org.kevoree.kevscript.language.expressions.finalexp;

import org.kevoree.kevscript.language.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class ArrayDeclExpression implements FinalExpression {

    public List<FinalExpression> expressionList = new ArrayList<>();

    public void add(FinalExpression expression) {
        expressionList.add(expression);
    }

    @Override
    public String toText() {
        List<String> sb = new ArrayList<>();
        for (FinalExpression fe : expressionList) {
            sb.add(fe.toText());
        }
        return "[" + StringUtils.join(sb, ", ") + "]";
    }

    @Override
    public String toString() {
        return "ArrayDeclExpression{" +
                "expressionList=" + expressionList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayDeclExpression that = (ArrayDeclExpression) o;

        return expressionList != null ? expressionList.equals(that.expressionList) : that.expressionList == null;

    }

    @Override
    public int hashCode() {
        return expressionList != null ? expressionList.hashCode() : 0;
    }
}
