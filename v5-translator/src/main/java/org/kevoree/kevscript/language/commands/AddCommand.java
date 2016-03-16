package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.InstanceElement;

/**
 * Created by mleduc on 15/03/16.
 */
public class AddCommand extends AbstractCommand {

    public final InstanceElement parent;
    public final InstanceElement child;

    public AddCommand(final InstanceElement parent, final InstanceElement child) {
        this.parent = parent;
        this.child = child;
    }

    public AddCommand(final InstanceElement child) {
        this.parent = null;
        this.child = child;
    }
}
