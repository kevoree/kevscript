package org.kevoree.kevscript.language.commands.element;

/**
 * Created by mleduc on 24/03/16.
 */
public class InstanceElement {
    public final RootInstanceElement parent;
    public final RootInstanceElement child;

    public InstanceElement(final RootInstanceElement parent, final RootInstanceElement child) {
        this.parent = parent;
        this.child = child;
    }

    public InstanceElement(final RootInstanceElement child) {
        this.parent = null;
        this.child = child;
    }
}
