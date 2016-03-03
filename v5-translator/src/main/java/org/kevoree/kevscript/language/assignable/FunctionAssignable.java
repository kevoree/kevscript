package org.kevoree.kevscript.language.assignable;

import org.apache.commons.lang3.NotImplementedException;
import org.kevoree.kevscript.language.context.Context;

/**
 * Created by mleduc on 03/03/16.
 */
public class FunctionAssignable extends Assignable {
    private final String bodyText;
    private final Assignable retValue;

    public FunctionAssignable(String bodyText) {
        this.bodyText = bodyText;
        this.retValue = null;
    }

    public FunctionAssignable(String bodyText, Assignable retValue) {
        this.bodyText = bodyText;
        this.retValue = retValue;
    }

    @Override
    public String toText() {
        return bodyText;
    }

    @Override
    public Assignable resolve(Context context) {
        throw new NotImplementedException("function resolution not working");
    }

    public Assignable getRetValue() {
        return retValue;
    }
}
