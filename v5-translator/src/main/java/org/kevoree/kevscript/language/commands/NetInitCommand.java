package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.RootInstanceElement;
import org.kevoree.kevscript.language.commands.element.object.ObjectElement;

/**
 * Created by mleduc on 25/03/16.
 */
public class NetInitCommand extends AbstractCommand {
    public final RootInstanceElement instance;
    public final ObjectElement network;

    public NetInitCommand(final RootInstanceElement instance, final ObjectElement network) {
        this.instance = instance;
        this.network = network;
    }
}
