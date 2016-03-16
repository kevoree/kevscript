package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;
import org.kevoree.kevscript.language.context.Context;

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
    public Expression resolve(Context context) {
        throw new NotImplementedException("function resolution not working");
    }


    public void add(final Expression expression) {
        expressions.add(expression);
    }
}
