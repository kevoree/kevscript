package org.kevoree.kevscript.language.commands.element;

/**
 * Created by mleduc on 22/03/16.
 */
public class DictionaryElement {
    public final String name;
    public final InstanceElement node;
    public final InstanceElement component;

    public DictionaryElement(final String name, final InstanceElement node, InstanceElement component) {
        this.name = name;
        this.node = node;
        this.component = component;
    }
}
