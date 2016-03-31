package org.kevoree.kevscript.language.expressions.finalexp.function;

import org.kevoree.kevscript.language.context.Context;

import static org.kevoree.kevscript.KevScriptParser.FuncBodyContext;

/**
 * Created by mleduc on 22/03/16.
 */
public class FunctionExpression extends AbstractFunctionExpression<FuncBodyContext> {
    public final Context context;

    public FunctionExpression(Context context) {
        final Context context1 = new Context();
        context1.setContext(context.getInheritedContext());
        this.context = context1;
    }
}
