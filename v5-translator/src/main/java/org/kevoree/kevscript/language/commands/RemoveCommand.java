package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.InstanceElement;

/**
 * Created by mleduc on 24/03/16.
 */
public class RemoveCommand extends AbstractCommand {
    public final InstanceElement parent;
    public final InstanceElement child;

    public RemoveCommand(final InstanceElement parent, final InstanceElement child) {
        this.parent = parent;
        this.child = child;
    }

    public RemoveCommand(final InstanceElement child) {
        this.parent = null;
        this.child = child;
    }
}
