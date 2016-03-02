package org.kevoree.kevscript.language.context;

import org.apache.commons.lang3.NotImplementedException;
import org.kevoree.kevscript.language.assignable.Assignable;

import java.util.Map;
import java.util.Set;

/**
 * Created by mleduc on 02/03/16.
 */
public class LoopContext extends Context {
    @Override
    public Map<String, Assignable> getMapIdentifiers() {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public Set<String> getSetInstances() {
        throw new NotImplementedException("Not implemented");
    }
}
