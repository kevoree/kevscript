package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.expressions.finalexp.PortPathExpression;

/**
 *
 *
 */
public class UnbindCommand extends AbstractCommand {
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
}
