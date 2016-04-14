package org.kevoree.kevscript.language.expressions.finalexp.function;

import org.kevoree.kevscript.language.expressions.finalexp.FinalExpression;

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
        return "";
    }

    public List<String> getParameters() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractFunctionExpression<?> that = (AbstractFunctionExpression<?>) o;

        if (params != null ? !params.equals(that.params) : that.params != null) return false;
        return functionBody != null ? functionBody.equals(that.functionBody) : that.functionBody == null;

    }

    @Override
    public int hashCode() {
        int result = params != null ? params.hashCode() : 0;
        result = 31 * result + (functionBody != null ? functionBody.hashCode() : 0);
        return result;
    }
}
