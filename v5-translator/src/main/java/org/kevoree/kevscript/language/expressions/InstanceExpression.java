package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 04/03/16.
 */
public class InstanceExpression extends Expression {
    public final Expression instanceName;
    public final String instanceTypeDefName;
    public final Expression instanceTypeDefVersion;
    public final Expression instanceDeployUnits;

    public InstanceExpression(final Expression instanceName, final String instanceTypeDefName, final Expression instanceTypeDefVersion, final Expression instanceDeployUnits) {
        this.instanceName = instanceName;
        this.instanceTypeDefName = instanceTypeDefName;
        this.instanceTypeDefVersion = instanceTypeDefVersion;
        this.instanceDeployUnits = instanceDeployUnits;
    }

    @Override
    public String toText() {
        return instanceName.toText();
    }

/*    @Override
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
            ret = true;
        }else {
            ret = false;
        }
        return ret;
    }*/
}
