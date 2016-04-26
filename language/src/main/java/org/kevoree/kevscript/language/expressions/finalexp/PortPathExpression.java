package org.kevoree.kevscript.language.expressions.finalexp;

/**
 *
 *
 */
public class PortPathExpression implements FinalExpression {
    public final InstanceExpression instance;
    public final boolean isInput;
    public final String portName;

    public PortPathExpression(final InstanceExpression instance, final boolean isInput, final String portName) {
        assert instance != null;
        this.instance = instance;
        this.isInput = isInput;
        this.portName = portName;
    }

    @Override
    public String toText() {
        String arrow = isInput ? "<-" : "->";
        return instance.toText() + arrow + portName;
    }

    @Override
    public String toString() {
        return "PortPathExpression{" +
                "instance=" + instance +
                ", isInput=" + isInput +
                ", portName='" + portName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PortPathExpression that = (PortPathExpression) o;

        if (isInput != that.isInput) return false;
        if (instance != null ? !instance.equals(that.instance) : that.instance != null) return false;
        return portName != null ? portName.equals(that.portName) : that.portName == null;

    }

    @Override
    public int hashCode() {
        int result = instance != null ? instance.hashCode() : 0;
        result = 31 * result + (isInput ? 1 : 0);
        result = 31 * result + (portName != null ? portName.hashCode() : 0);
        return result;
    }
}

