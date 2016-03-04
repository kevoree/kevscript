package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.assignable.Assignable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mleduc on 02/03/16.
 */
public class RootContext extends Context {
    private final Map<String, Assignable> mapIdentifiers;
    private final Map<String, KevScriptParser.Function_operationContext> functions;

    public RootContext() {
        mapIdentifiers = new HashMap<>();
        functions = new HashMap<>();
    }

    public RootContext(Context rootContext) {
        this();
        mapIdentifiers.putAll(rootContext.getMapIdentifiers());
        functions.putAll(rootContext.getSetFunctions());
    }

    @Override
    public Map<String, Assignable> getMapIdentifiers() {
        return mapIdentifiers;
    }

    @Override
    public Map<String, KevScriptParser.Function_operationContext> getSetFunctions() {
        return this.functions;
    }
}
