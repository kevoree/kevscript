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
    private final Set<String> setInstances;
    private final Map<String, KevScriptParser.Function_operationContext> functions;

    public RootContext() {
        mapIdentifiers = new HashMap<>();
        setInstances = new HashSet<>();
        functions = new HashMap<>();
    }

    public RootContext(Context rootContext) {
        this();
        mapIdentifiers.putAll(rootContext.getMapIdentifiers());
        setInstances.addAll(rootContext.getSetInstances());
        functions.putAll(rootContext.getSetFunctions());
    }

    @Override
    public Map<String, Assignable> getMapIdentifiers() {
        return mapIdentifiers;
    }

    @Override
    public Set<String> getSetInstances() {
        return setInstances;
    }

    @Override
    public Map<String, KevScriptParser.Function_operationContext> getSetFunctions() {
        return this.functions;
    }
}
