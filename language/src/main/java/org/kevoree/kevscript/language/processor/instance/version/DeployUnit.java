package org.kevoree.kevscript.language.processor.instance.version;

/**
 *
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
}
