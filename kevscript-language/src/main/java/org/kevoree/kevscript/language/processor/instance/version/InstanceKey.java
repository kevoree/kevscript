package org.kevoree.kevscript.language.processor.instance.version;

/**
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceKey that = (InstanceKey) o;

        if (world != that.world) return false;
        if (time != that.time) return false;
        return instanceName != null ? instanceName.equals(that.instanceName) : that.instanceName == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (world ^ (world >>> 32));
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (instanceName != null ? instanceName.hashCode() : 0);
        return result;
    }
}
