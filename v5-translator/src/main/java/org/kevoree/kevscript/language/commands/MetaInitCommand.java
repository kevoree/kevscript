package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.RootInstanceElement;
import org.kevoree.kevscript.language.commands.element.object.ObjectElement;

/**
 * Created by mleduc on 29/03/16.
 */
public class MetaInitCommand extends AbstractCommand {
    public final RootInstanceElement instance;
    public final ObjectElement metas;

    public MetaInitCommand(RootInstanceElement instance, ObjectElement metas) {
        this.instance = instance;
        this.metas = metas;
    }
}
