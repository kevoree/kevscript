package org.kevoree.kevscript.language.expressions;

import org.kevoree.kevscript.language.context.Context;

/**
 * Created by mleduc on 04/03/16.
 */
public class InstanceExpression extends Expression {
    private final String instanceVarName;
    private final Expression instanceName;
    private final Expression instanceTypeDefName;
    private final Expression instanceTypeDefVersion;
    private final Expression instanceDeployUnits;

    public InstanceExpression(String instanceVarName, Expression instanceName, Expression instanceTypeDefName, Expression instanceTypeDefVersion, Expression instanceDeployUnits) {
        this.instanceVarName = instanceVarName;
        this.instanceName = instanceName;
        this.instanceTypeDefName =instanceTypeDefName;
        this.instanceTypeDefVersion = instanceTypeDefVersion;
        this.instanceDeployUnits = instanceDeployUnits;
    }

    @Override
    public String toText() {
        return null;
    }

    @Override
    public Expression resolve(Context context) {
        return null;
    }
}
