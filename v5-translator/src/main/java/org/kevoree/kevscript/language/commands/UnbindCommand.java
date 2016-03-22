package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.commands.element.PortElement;

/**
 * Created by mleduc on 17/03/16.
 */
public class UnbindCommand extends AbstractCommand {
    public final InstanceElement chan;
    public final PortElement port;

    public UnbindCommand(final InstanceElement chan, final PortElement port) {
        this.chan = chan;
        this.port = port;
    }
}
