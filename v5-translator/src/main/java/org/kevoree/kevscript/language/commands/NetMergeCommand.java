package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.RootInstanceElement;
import org.kevoree.kevscript.language.commands.element.object.ObjectElement;

/**
 * Created by mleduc on 25/03/16.
 */
public class NetMergeCommand extends AbstractCommand {

    public final RootInstanceElement node;
    public final ObjectElement network;

    public NetMergeCommand(final RootInstanceElement node, final ObjectElement network) {
        this.node = node;
        this.network = network;
    }

}
