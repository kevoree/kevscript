package org.kevoree.kevscript.language.processor.instance.version;

import org.kevoree.kevscript.language.commands.InstanceCommand;

/**
 * Created by mleduc on 21/04/16.
 */
public class InstanceContext {
    public final long world;
    public final long time;
    public final InstanceCommand instance;

    public InstanceContext(long world, long time, InstanceCommand instance) {
        this.world = world;
        this.time = time;
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "InstanceContext{" +
                "instance=" + instance +
                ", world=" + world +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceContext that = (InstanceContext) o;

        if (world != that.world) return false;
        if (time != that.time) return false;
        return instance != null ? instance.equals(that.instance) : that.instance == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (world ^ (world >>> 32));
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (instance != null ? instance.hashCode() : 0);
        return result;
    }
}
