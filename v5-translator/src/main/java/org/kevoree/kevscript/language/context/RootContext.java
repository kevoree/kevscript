package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.language.assignable.Assignable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mleduc on 02/03/16.
 */
public class RootContext extends Context {
    private final Map<String, Assignable> mapIdentifiers = new HashMap<>();
    private final Set<String> setInstances = new HashSet<>();

    public Map<String, Assignable> getMapIdentifiers() {
        return mapIdentifiers;
    }

    public Set<String> getSetInstances() {
        return setInstances;
    }
}
