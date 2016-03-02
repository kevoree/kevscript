package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.language.assignable.Assignable;

import java.util.Map;
import java.util.Set;

/**
 * Created by mleduc on 02/03/16.
 */
public abstract class Context {
    public abstract Map<String, Assignable> getMapIdentifiers();

    public abstract Set<String> getSetInstances();
}
