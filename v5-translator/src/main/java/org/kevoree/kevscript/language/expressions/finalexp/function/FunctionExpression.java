package org.kevoree.kevscript.language.expressions.finalexp.function;

import org.kevoree.kevscript.language.context.Context;

import static org.kevoree.kevscript.KevScriptParser.FuncBodyContext;

/**
 *
 *
 */
public class FunctionExpression extends AbstractFunctionExpression<FuncBodyContext> {
    public final Context context;

    public FunctionExpression(Context context) {
        this.context = context;
    }
}
