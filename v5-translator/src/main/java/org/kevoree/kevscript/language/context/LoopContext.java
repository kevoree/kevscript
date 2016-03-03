package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.language.assignable.Assignable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mleduc on 02/03/16.
 */
public class LoopContext extends Context {
    private final Map<String, Assignable> identifiers = new HashMap<>();
    private final Set<String> instances = new HashSet<>();
    private Set<String> functions = new HashSet<>();

    public LoopContext(Context rootContext) {
        identifiers.putAll(rootContext.getMapIdentifiers());
        instances.addAll(rootContext.getSetInstances());
        functions.addAll(rootContext.getSetFunctions());
    }

    @Override
    public Map<String, Assignable> getMapIdentifiers() {
        return this.identifiers;
    }

    @Override
    public Set<String> getSetInstances() {
        return this.instances;
    }

    @Override
    public Set<String> getSetFunctions() {
        return this.functions;
    }
}
