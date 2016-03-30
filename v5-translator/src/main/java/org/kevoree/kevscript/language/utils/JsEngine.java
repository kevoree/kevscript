package org.kevoree.kevscript.language.utils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

/**
 * Created by mleduc on 30/03/16.
 */
public class JsEngine {
    public String evaluateFunction(final String expression, final String functionName, final List<?> arg) throws ScriptException, NoSuchMethodException {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager.getEngineByName("nashorn");
        engine.eval(expression);
        return String.valueOf(((Invocable) engine).invokeFunction(functionName, arg.toArray()));
    }
}
