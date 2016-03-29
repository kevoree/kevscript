package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.RootInstanceElement;

import java.util.List;

/**
 * Created by mleduc on 29/03/16.
 */
public class MetaRemoveCommand extends AbstractCommand {
    public final RootInstanceElement instance;
    public final List<String> objectRefs;

    public MetaRemoveCommand(RootInstanceElement instance, List<String> objectRefs) {
        this.instance = instance;
        this.objectRefs = objectRefs;
    }
}
