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
}
