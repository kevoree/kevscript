package org.kevoree.kevscript.language.utils;

import org.kevoree.kevscript.language.expressions.finalexp.ArrayDeclExpression;
import org.kevoree.kevscript.language.expressions.finalexp.FinalExpression;
import org.kevoree.kevscript.language.expressions.finalexp.StringExpression;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import javax.script.ScriptException;

/**
 * Created by mleduc on 30/03/16.
 */
public class JsEngine {


    public FinalExpression evaluateFunction(final String expression) throws ScriptException, NoSuchMethodException {
        final Context cx = Context.enter();
        final Scriptable scope = cx.initStandardObjects();
        final Object result = cx.evaluateString(scope, expression, "<cmd>", 1, null);
        final FinalExpression finalExpression = toFinalExpression(result);
        return finalExpression;
    }

    private FinalExpression toFinalExpression(final Object result) {
        final FinalExpression finalExpression;
        if(result instanceof NativeArray) {
            final ArrayDeclExpression arrayDeclExpression = new ArrayDeclExpression();
            final NativeArray nativeArray = (NativeArray) result;
            for(Object arrayElem : nativeArray) {
                arrayDeclExpression.add(this.toFinalExpression(arrayElem));
            }
            finalExpression = arrayDeclExpression;
        } else {
            finalExpression = new StringExpression(String.valueOf(result));
        }
        return finalExpression;
    }
}
