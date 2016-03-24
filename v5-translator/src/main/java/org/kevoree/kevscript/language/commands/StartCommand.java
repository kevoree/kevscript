package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.AbstractCommand;
import org.kevoree.kevscript.language.commands.element.InstanceElement;

/**
 * Created by mleduc on 24/03/16.
 */
public class StartCommand extends AbstractCommand {
    public final InstanceElement instance;

    public StartCommand(InstanceElement instance) {
        this.instance = instance;
    }
}
