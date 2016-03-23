package org.kevoree.kevscript.language.expressions;

import java.util.ArrayList;
import java.util.List;

import static org.kevoree.kevscript.KevScriptParser.FuncBodyContext;

/**
 * Created by mleduc on 22/03/16.
 */
public class FunctionExpression implements FinalExpression {

    private final List<String> params = new ArrayList<>();
    private FuncBodyContext functionBody;

    public int getParamtersSize() {
        return params.size();
    }

    public String getParameter(final int index) {
        return params.get(index);
    }

    public FuncBodyContext getFunctionBody() {
        return functionBody;
    }

    @Override
    public String toText() {
        return null;
    }

    public void addParam(String text) {
        this.params.add(text);
    }

    public void setFunctionBody(FuncBodyContext functionBody) {
        this.functionBody = functionBody;
    }
}
