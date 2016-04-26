package org.kevoree.kevscript.language.utils;

import org.kevoree.kevscript.language.expressions.finalexp.ArrayDeclExpression;
import org.kevoree.kevscript.language.expressions.finalexp.FinalExpression;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;
import org.kevoree.kevscript.language.expressions.finalexp.StringExpression;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import javax.script.ScriptException;
import java.util.Map;

/**
 * Created by mleduc on 30/03/16.
 */
public class JsEngine {

    public FinalExpression evaluateFunction(final String expression) throws ScriptException, NoSuchMethodException {
        final Context cx = Context.enter();
        final Scriptable scope = cx.initStandardObjects();
        final Object result = cx.evaluateString(scope, expression, "<cmd>", 1, null);
        return toFinalExpression(result);
    }

    private FinalExpression toFinalExpression(final Object result) {
        final FinalExpression finalExpression;
        if (result instanceof NativeArray) {
            final ArrayDeclExpression arrayDeclExpression = new ArrayDeclExpression();
            final NativeArray nativeArray = (NativeArray) result;
            for (Object arrayElem : nativeArray) {
                arrayDeclExpression.add(this.toFinalExpression(arrayElem));
            }
            finalExpression = arrayDeclExpression;
        } else if (result instanceof NativeObject) {
            final ObjectDeclExpression objectDeclExpression = new ObjectDeclExpression();
            final NativeObject nativeObject = (NativeObject) result;
            for (final Map.Entry<Object, Object> entry : nativeObject.entrySet()) {
                objectDeclExpression.put(entry.getKey().toString(), this.toFinalExpression(entry.getValue()));
            }
            finalExpression = objectDeclExpression;
        } else {
            finalExpression = new StringExpression(String.valueOf(result));
        }
        return finalExpression;
    }
}
