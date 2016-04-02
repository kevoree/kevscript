package org.kevoree.kevscript.language.expressions.finalexp;

/**
 *
 *
 */
public class InstanceExpression implements FinalExpression {

    public final String instanceName;
    public final TypeExpression typeExpr;

    public InstanceExpression(final String instanceName, final TypeExpression typeExpr) {
        this.instanceName = instanceName;
        this.typeExpr = typeExpr;
    }

    @Override
    public String toText() {
        return instanceName;
    }

}
