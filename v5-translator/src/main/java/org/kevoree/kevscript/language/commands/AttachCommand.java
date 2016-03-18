package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.InstanceElement;

/**
 * Created by mleduc on 17/03/16.
 */
public class AttachCommand extends AbstractCommand {
    private final InstanceElement connector;
    private final InstanceElement node;

    public AttachCommand(final InstanceElement connector, final InstanceElement node) {
        this.connector = connector;
        this.node = node;
    }
}
