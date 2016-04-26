package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 * Created by mleduc on 18/04/16.
 */
public class TimeCommand implements ICommand {
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
    public <T> T accept(DefaultCommandVisitor<T> visitor) {
        return visitor.visitTimeCommand(this);
    }
}
