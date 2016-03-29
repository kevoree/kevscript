package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.language.expressions.ArrayDeclExpression;
import org.kevoree.kevscript.language.expressions.FinalExpression;
import org.kevoree.kevscript.language.expressions.ObjectDeclExpression;

import java.util.Map;

/**
 * Created by mleduc on 29/03/16.
 */
public class RootContext extends Context {
    public void addExternalExpression(final String identifier, final FinalExpression expression) {

        if (expression instanceof ArrayDeclExpression) {
            final ArrayDeclExpression arr = (ArrayDeclExpression) expression;
            int i = 0;
            for (final FinalExpression nx : arr.expressionList) {
                addExternalExpression(identifier + "[" + (i++) + "]", nx);
            }
        } else if (expression instanceof ObjectDeclExpression) {
            final ObjectDeclExpression obj = (ObjectDeclExpression) expression;
            for (Map.Entry<String, FinalExpression> x : obj.values.entrySet()) {
                addExternalExpression(identifier + "." + x.getKey(), x.getValue());
            }
        }
        basicAddExpression('&' + identifier, expression);
    }
}
