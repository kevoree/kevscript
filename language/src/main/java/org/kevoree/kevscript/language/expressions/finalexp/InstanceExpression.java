package org.kevoree.kevscript.language.expressions.finalexp;

/**
 *
 *
 */
public class InstanceExpression implements FinalExpression {

    public final String instanceName;

    public InstanceExpression(final String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public String toText() {
        return instanceName;
    }

    @Override
    public String toString() {
        return "InstanceExpression{" +
                "instanceName='" + instanceName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceExpression that = (InstanceExpression) o;

        return instanceName != null ? instanceName.equals(that.instanceName) : that.instanceName == null;

    }

    @Override
    public int hashCode() {
        return instanceName != null ? instanceName.hashCode() : 0;
    }
}
