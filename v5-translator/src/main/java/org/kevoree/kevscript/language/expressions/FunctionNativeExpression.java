package org.kevoree.kevscript.language.expressions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 22/03/16.
 */
public class FunctionNativeExpression implements FinalExpression {

    private final List<String> params = new ArrayList<>();
    private String functionBody;

    public int getParamtersSize() {
        return params.size();
    }

    public String getParameter(final int index) {
        return params.get(index);
    }

    public String getFunctionBody() {
        return functionBody;
    }

    @Override
    public String toText() {
        return null;
    }

    public void addParam(String text) {
        this.params.add(text);
    }

    public void setFunctionBody(String functionBody) {
        this.functionBody = functionBody;
    }
}
