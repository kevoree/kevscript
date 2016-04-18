package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.utils.StringUtils;

/**
 * Created by mleduc on 18/04/16.
 */
public class WorldCommand extends Commands {
    public final long world;

    public WorldCommand(final long world) {
        this.world = world;
    }

    @Override
    public String toString() {
        return "TimeCommand{" +
                "world=" + world +
                StringUtils.join(this, "\n\t") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        WorldCommand that = (WorldCommand) o;

        return world == that.world;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (world ^ (world >>> 32));
        return result;
    }
}
