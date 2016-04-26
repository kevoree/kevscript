package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 */
public class TimeCommand implements Command {
    public final long time;
    public final Commands commands;


    public TimeCommand(long time, Commands commands) {
        this.time = time;
        this.commands = commands;
    }

    @Override
    public String toString() {
        return "TimeCommand{" +
                "commands=" + commands +
                ", time=" + time +
                '}';
    }

    @Override
    public <T> void accept(CommandVisitor<T> visitor, T context) {
        visitor.visitTimeCommand(this, context);
    }
}
