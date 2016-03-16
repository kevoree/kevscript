package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 04/03/16.
 */
public class InstanceExpression extends Expression {
    public final String instanceVarName;
    public final Expression instanceName;
    public final String instanceTypeDefName;
    public final Expression instanceTypeDefVersion;
    public final Expression instanceDeployUnits;

    public InstanceExpression(String instanceVarName, Expression instanceName, String instanceTypeDefName, Expression instanceTypeDefVersion, Expression instanceDeployUnits) {
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
    public boolean match(Expression identifier) {
        final boolean ret;
        if(identifier instanceof IdentifierExpression) {
            final IdentifierExpression identifier1 = (IdentifierExpression) identifier;
            if(identifier1.right != null) {
                ret = false;
            } else {
                ret = this.match(identifier1.left);
            }
        } else if (identifier instanceof StringExpression) {
            ret = ((StringExpression) identifier).text.equals(instanceVarName);
        }else {
            ret = false;
        }
        return ret;
    }
}
