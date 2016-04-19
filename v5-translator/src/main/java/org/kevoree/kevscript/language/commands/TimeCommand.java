package org.kevoree.kevscript.language.commands;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeCommand that = (TimeCommand) o;

        if (time != that.time) return false;
        return commands != null ? commands.equals(that.commands) : that.commands == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (time ^ (time >>> 32));
        result = 31 * result + (commands != null ? commands.hashCode() : 0);
        return result;
    }
}
