package org.kevoree.kevscript.language.commands.element;

/**
 * Created by mleduc on 21/03/16.
 */
public class PortElement {
    public final String name;
    public final InstanceElement node;
    public final InstanceElement component;
    public final Boolean isInput;

    public PortElement(final String name, final InstanceElement node, InstanceElement component, final Boolean isInput) {
        this.name = name;
        this.node = node;
        this.component = component;
        this.isInput = isInput;
    }
}
