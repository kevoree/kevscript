package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.InstanceElement;

/**
 * Created by mleduc on 24/03/16.
 */
public class RemoveCommand extends AbstractCommand {
    public final InstanceElement instance;

    public RemoveCommand(final InstanceElement instance) {
        this.instance = instance;
    }
}
