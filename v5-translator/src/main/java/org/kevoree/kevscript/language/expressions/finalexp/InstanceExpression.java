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

    @Override
    public String toString() {
        return "InstanceExpression{" +
                "instanceName='" + instanceName + '\'' +
                ", typeExpr=" + typeExpr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceExpression that = (InstanceExpression) o;

        if (instanceName != null ? !instanceName.equals(that.instanceName) : that.instanceName != null) return false;
        return typeExpr != null ? typeExpr.equals(that.typeExpr) : that.typeExpr == null;

    }

    @Override
    public int hashCode() {
        int result = instanceName != null ? instanceName.hashCode() : 0;
        result = 31 * result + (typeExpr != null ? typeExpr.hashCode() : 0);
        return result;
    }
}
