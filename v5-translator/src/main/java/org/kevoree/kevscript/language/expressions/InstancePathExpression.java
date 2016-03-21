package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 15/03/16.
 */
public class InstancePathExpression extends Expression {

    private List<Expression> expressions = new ArrayList<>();

    @Override
    public String toText() {
        throw new NotImplementedException("TODO");
    }


    @Override
    public boolean match(Expression identifier) {
        return false;
    }


    public void add(Expression expression) {
        this.expressions.add(expression);
    }
}
