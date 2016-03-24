package org.kevoree.kevscript.language.commands.element;

/**
 * Created by mleduc on 21/03/16.
 */
public class PortElement {
    public final String name;
    public final InstanceElement instance;
    public final Boolean isInput;

    public PortElement(final String name, final InstanceElement instance, final Boolean isInput) {
        this.name = name;
        this.instance = instance;
        this.isInput = isInput;
    }
}
