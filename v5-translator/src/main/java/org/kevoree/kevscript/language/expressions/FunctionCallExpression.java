package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 03/03/16.
 */
public class FunctionCallExpression implements FinalExpression {
    public final String returnValue;

    public FunctionCallExpression(String returnValue) {

        this.returnValue = returnValue;
    }

    @Override
    public String toText() {
        return returnValue;
    }
}
