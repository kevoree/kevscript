package org.kevoree.kevscript.language.commands;

import java.util.ArrayList;

/**
 * Created by mleduc on 15/03/16.
 */
public class Commands extends ArrayList<AbstractCommand> {
    public Commands addCommand(final AbstractCommand command) {
        this.add(command);
        return this;
    }
}
