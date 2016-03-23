package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by mleduc on 15/03/16.
 */
public class InstancePathExpression implements FinalExpression {

    public final FinalExpression node;
    public final FinalExpression component;

    public InstancePathExpression(FinalExpression component) {
        this(null, component);
    }

    public InstancePathExpression(FinalExpression node, FinalExpression component) {
        assert node != null;
        assert component != null;
        this.node = node;
        this.component = component;
    }

    @Override
    public String toText() {
        throw new NotImplementedException("TODO");
    }
}
