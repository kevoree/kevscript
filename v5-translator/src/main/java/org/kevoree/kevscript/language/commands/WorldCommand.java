package org.kevoree.kevscript.language.commands;

/**
 * Created by mleduc on 18/04/16.
 */
public class WorldCommand implements ICommand {
    public final long world;
    public final Commands commands;

    public WorldCommand(long world, Commands commands) {
        this.world = world;
        this.commands = commands;
    }

    @Override
    public String toString() {
        return "WorldCommand{" +
                "commands=" + commands +
                ", world=" + world +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorldCommand that = (WorldCommand) o;

        if (world != that.world) return false;
        return commands != null ? commands.equals(that.commands) : that.commands == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (world ^ (world >>> 32));
        result = 31 * result + (commands != null ? commands.hashCode() : 0);
        return result;
    }
}
