package org.kevoree.kevscript.language.processor.instance.version;

import org.kevoree.kevscript.language.commands.InstanceCommand;

/**
 *
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
}
