package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.PortPathExpression;
import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public class BindCommand implements Command {
    public final InstanceExpression chan;
    public final PortPathExpression port;

    public BindCommand(final InstanceExpression chan, final PortPathExpression port) {
        this.chan = chan;
        this.port = port;
    }

    @Override
    public String toString() {
        return "BindCommand{" +
                "chan=" + chan +
                ", port=" + port +
                '}';
    }

    @Override
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitBindCommand(this);
    }
}
