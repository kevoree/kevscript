package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.PortElement;
import org.kevoree.kevscript.language.commands.element.RootInstanceElement;

/**
 * Created by mleduc on 17/03/16.
 */
public class UnbindCommand extends AbstractCommand {
    public final RootInstanceElement chan;
    public final PortElement port;

    public UnbindCommand(final RootInstanceElement chan, final PortElement port) {
        this.chan = chan;
        this.port = port;
    }
}
