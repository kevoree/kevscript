package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.InstanceElement;

/**
 * Created by mleduc on 15/03/16.
 */
public class AddCommand extends AbstractCommand {

    public final InstanceElement instance;

    public AddCommand(final InstanceElement instance) {
        this.instance = instance;
    }
}
