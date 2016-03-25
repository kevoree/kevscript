package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.RootInstanceElement;

import java.util.List;

/**
 * Created by mleduc on 25/03/16.
 */
public class NetRemoveCommand extends AbstractCommand {
    public final RootInstanceElement node  ;
    public final List<String> objectRefs;

    public NetRemoveCommand(final RootInstanceElement node, final List<String> objectRefs) {
        this.node = node;
        this.objectRefs = objectRefs;
    }
}
