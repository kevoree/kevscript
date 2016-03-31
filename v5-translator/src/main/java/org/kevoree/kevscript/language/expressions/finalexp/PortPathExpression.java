package org.kevoree.kevscript.language.expressions.finalexp;

import org.kevoree.kevscript.language.utils.NotImplementedException;

/**
 * Created by mleduc on 15/03/16.
 */
public class PortPathExpression implements FinalExpression {
    public final InstancePathExpression instancePath;
    public final boolean isInput;
    public final String portName;
    private boolean exported;

    public PortPathExpression(final InstancePathExpression instancePath, final boolean isInput, final String portName) {
        assert instancePath != null;
        this.instancePath = instancePath;
        this.isInput = isInput;
        this.portName = portName;
    }

    @Override
    public String toText() {
        throw new NotImplementedException("TODO");
    }


}

