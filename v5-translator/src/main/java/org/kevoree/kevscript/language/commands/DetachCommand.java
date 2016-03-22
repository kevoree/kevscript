package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.InstanceElement;

/**
 * Created by mleduc on 17/03/16.
 */
public class DetachCommand extends AbstractCommand {
    public final InstanceElement group;
    public final InstanceElement node;

    public DetachCommand(final InstanceElement group, final InstanceElement node) {
        this.group = group;
        this.node = node;
    }
}
