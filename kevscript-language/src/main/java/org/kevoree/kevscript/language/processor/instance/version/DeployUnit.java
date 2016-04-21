package org.kevoree.kevscript.language.processor.instance.version;

/**
 * Created by mleduc on 20/04/16.
 */
public class DeployUnit {
    public final String name;
    public final long version;
    public final String platform;

    public DeployUnit(String name, long version, String platform) {
        this.name = name;
        this.version = version;
        this.platform = platform;
    }


    @Override
    public String toString() {
        return "DeployUnit{" +
                "name='" + name + '\'' +
                ", version=" + version +
                ", platform='" + platform + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeployUnit that = (DeployUnit) o;

        if (version != that.version) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return platform != null ? platform.equals(that.platform) : that.platform == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (platform != null ? platform.hashCode() : 0);
        return result;
    }
}
