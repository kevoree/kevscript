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

    private final ScriptEngine engine;
    private final static JsEngine instance = new JsEngine();

    private JsEngine() {
        final ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("nashorn");
    }

    public static JsEngine getInstance() {
        return JsEngine.instance;
    }

    public String evaluateFunction(final String expression, final String functionName, final List<?> arg) throws ScriptException, NoSuchMethodException {
        final ScriptEngine scriptEngine = engine.getFactory().getScriptEngine();
        scriptEngine.eval(expression);
        return String.valueOf(((Invocable) scriptEngine).invokeFunction(functionName, arg.toArray()));
    }
}
