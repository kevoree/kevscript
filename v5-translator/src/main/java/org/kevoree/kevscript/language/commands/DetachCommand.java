package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.commands.element.RootInstanceElement;

/**
 * Created by mleduc on 17/03/16.
 */
public class DetachCommand extends AbstractCommand {
    public final RootInstanceElement group;
    public final RootInstanceElement node;

    public DetachCommand(final RootInstanceElement group, final RootInstanceElement node) {
        this.group = group;
        this.node = node;
    }
}
