package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by mleduc on 15/03/16.
 */
public class InstancePathExpression extends Expression {

    public final Expression node;
    public final Expression component;

    public InstancePathExpression(Expression component) {
        this(null, component);
    }

    public InstancePathExpression(Expression node, Expression component) {
        this.node = node;
        this.component = component;
    }

    @Override
    public String toText() {
        throw new NotImplementedException("TODO");
    }
}
