package org.kevoree.kevscript.interpreter;

import org.kevoree.Instance;
import org.kevoree.Model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ModelContext {

    public Model model;
    public Map<String, Instance> instances;

    public ModelContext(Model model) {
        this.model = model;
        this.instances = new HashMap<String, Instance>();
    }
}
