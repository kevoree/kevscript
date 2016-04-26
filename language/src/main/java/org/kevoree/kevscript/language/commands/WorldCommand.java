package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 */
public class WorldCommand implements Command {
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
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitWorldCommand(this, context);
    }
}
