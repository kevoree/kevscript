package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.language.expressions.finalexp.ArrayDeclExpression;
import org.kevoree.kevscript.language.expressions.finalexp.FinalExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;

import java.util.Map;

/**
 * Created by mleduc on 29/03/16.
 */
public class RootContext extends Context {
    private final String basePath;

    public RootContext(final String basePath) {
        super();
        this.basePath = basePath;
    }

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
        basicAddExpression('&' + identifier, expression, false);
    }

    @Override
    public String getBasePath() {
        return this.basePath;
    }
}
