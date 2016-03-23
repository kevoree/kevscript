package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 04/03/16.
 */
public class InstanceExpression implements FinalExpression {
    public final FinalExpression instanceName;
    public final String instanceTypeDefName;
    public final VersionExpression instanceTypeDefVersion;
    public final Expression instanceDeployUnits;

    public InstanceExpression(final FinalExpression instanceName, final String instanceTypeDefName, final VersionExpression instanceTypeDefVersion, final Expression instanceDeployUnits) {
        this.instanceName = instanceName;
        this.instanceTypeDefName = instanceTypeDefName;
        this.instanceTypeDefVersion = instanceTypeDefVersion;
        this.instanceDeployUnits = instanceDeployUnits;
    }

    @Override
    public String toText() {
        return instanceName.toText();
    }
}
