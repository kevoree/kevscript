package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.PortPathExpression;

/**
 *
 *
 */
public class UnbindCommand implements ICommand {
    public final InstanceExpression chan;
    public final PortPathExpression port;

    public UnbindCommand(final InstanceExpression chan, final PortPathExpression port) {
        this.chan = chan;
        this.port = port;
    }

    @Override
    public String toString() {
        return "UnbindCommand{" +
                "chan=" + chan +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnbindCommand that = (UnbindCommand) o;

        if (chan != null ? !chan.equals(that.chan) : that.chan != null) return false;
        return port != null ? port.equals(that.port) : that.port == null;

    }

    @Override
    public int hashCode() {
        int result = chan != null ? chan.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }
}
