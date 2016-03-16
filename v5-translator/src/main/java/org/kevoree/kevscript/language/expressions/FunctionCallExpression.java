package org.kevoree.kevscript.language.expressions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 03/03/16.
 */
public class FunctionCallExpression extends Expression {

    private final boolean isSpecial;
    private final String name;
    private final List<Expression> expressions = new ArrayList<>();

    public FunctionCallExpression(final boolean b, final String name) {
        this.isSpecial = b;
        this.name = name;
    }

    @Override
    public String toText() {
        return null;
    }

    @Override
    public boolean match(Expression identifier) {
        return false;
    }

    public void add(final Expression expression) {
        expressions.add(expression);
    }
}
