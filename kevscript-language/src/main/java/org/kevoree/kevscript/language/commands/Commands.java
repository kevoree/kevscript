package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by mleduc on 15/03/16.
 */
public class Commands extends ArrayList<ICommand> {

    public Commands addCommand(final ICommand command) {
        this.add(command);
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Commands{\n\t");
        sb.append(StringUtils.join(this, "\n\t"));
        sb.append("\n}");
        return sb.toString();
    }
}
