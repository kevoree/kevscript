package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;

/**
 *
 *
 */
public class AddCommand extends AbstractCommand {

    public final InstanceExpression target;
    public final InstanceExpression source;

    public AddCommand(final InstanceExpression target, InstanceExpression source) {
        this.target = target;
        this.source = source;
    }

    /*@Override
    public String toString() {
        String ret = "add ";
        if (target != null) {
            ret += target.toText() + " ";
        }
        if (source != null) {
            ret += source.toText();
        }
        return ret;
    }*/

    @Override
    public String toString() {
        return "AddCommand{" +
                "source=" + source +
                ", target=" + target +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddCommand that = (AddCommand) o;

        if (target != null ? !target.equals(that.target) : that.target != null) return false;
        return source != null ? source.equals(that.source) : that.source == null;

    }

    @Override
    public int hashCode() {
        int result = target != null ? target.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }
}
