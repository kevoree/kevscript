package org.kevoree.kevscript.language.expressions.function;

import org.kevoree.kevscript.language.expressions.FinalExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 29/03/16.
 */
public abstract class AbstractFunctionExpression<U> implements FinalExpression {

    private final List<String> params = new ArrayList<>();
    private U functionBody;

    public int getParametersSize() {
        return params.size();
    }

    public String getParameter(final int index) {
        return params.get(index);
    }

    public U getFunctionBody() {
        return functionBody;
    }

    public void addParam(String text) {
        this.params.add(text);
    }

    public void setFunctionBody(U functionBody) {
        this.functionBody = functionBody;
    }

    @Override
    public String toText() {
        return null;
    }

    public List<String> getParameters() {
        return params;
    }
}
