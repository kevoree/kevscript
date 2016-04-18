package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.utils.StringUtils;

/**
 * Created by mleduc on 18/04/16.
 */
public class TimeCommand extends Commands {
    public final long time;

    public TimeCommand(final long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TimeCommand{" +
                "time=" + time +
                StringUtils.join(this, "\n\t") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TimeCommand that = (TimeCommand) o;

        return time == that.time;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
}
