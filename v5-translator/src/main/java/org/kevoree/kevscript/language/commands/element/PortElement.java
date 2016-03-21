package org.kevoree.kevscript.language.commands.element;

/**
 * Created by mleduc on 21/03/16.
 */
public class PortElement {
    public final String name;
    public final InstanceElement node;
    public final InstanceElement component;
    public final boolean isLeft;

    public PortElement(final String name, final InstanceElement node, InstanceElement component, final boolean isLeft) {
        this.name = name;
        this.node = node;
        this.component = component;
        this.isLeft = isLeft;
    }
}
