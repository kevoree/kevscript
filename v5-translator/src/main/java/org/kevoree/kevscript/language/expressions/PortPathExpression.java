package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by mleduc on 15/03/16.
 */
public class PortPathExpression extends Expression {
    public final InstancePathExpression instancePath;
    public final boolean isInput;
    public final Expression portName;

    public PortPathExpression(final InstancePathExpression instancePath, final boolean isInput, final Expression portName) {
        this.instancePath = instancePath;
        this.isInput = isInput;
        this.portName = portName;
    }

    @Override
    public String toText() {
        throw new NotImplementedException("TODO");
    }

}

