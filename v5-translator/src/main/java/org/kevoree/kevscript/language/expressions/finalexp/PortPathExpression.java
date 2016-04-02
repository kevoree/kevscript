package org.kevoree.kevscript.language.expressions.finalexp;

import org.kevoree.kevscript.language.utils.NotImplementedException;

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
}

