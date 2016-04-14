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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FunctionExpression that = (FunctionExpression) o;

        return context != null ? context.equals(that.context) : that.context == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (context != null ? context.hashCode() : 0);
        return result;
    }
}
