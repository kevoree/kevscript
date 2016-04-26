package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

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
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitWorldCommand(this);
    }
}
