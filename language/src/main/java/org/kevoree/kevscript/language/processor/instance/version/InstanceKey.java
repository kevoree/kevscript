package org.kevoree.kevscript.language.processor.instance.version;

/**
 *
 * Created by mleduc on 20/04/16.
 */
public class InstanceKey {
    public final long world;
    public final long time;
    public final String instanceName;

    public InstanceKey(long world, long time, String instanceName) {
        this.world = world;
        this.time = time;
        this.instanceName = instanceName;
    }

    @Override
    public String toString() {
        return "InstanceKey{" +
                "instanceName='" + instanceName + '\'' +
                ", world=" + world +
                ", time=" + time +
                '}';
    }
}
