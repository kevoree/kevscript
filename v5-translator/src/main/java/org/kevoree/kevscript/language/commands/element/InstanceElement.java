package org.kevoree.kevscript.language.commands.element;

/**
 * Created by mleduc on 16/03/16.
 */
public class InstanceElement {
    public final String instanceName;
    public final String typeName;
    public final Long version;

    public InstanceElement(final String instanceName) {
        this.instanceName = instanceName;
        this.typeName = null;
        this.version = null;
    }

    public InstanceElement(final String instanceName, final String typeName) {
        this.instanceName = instanceName;
        this.typeName = typeName;
        this.version = null;
    }

    public InstanceElement(final String instanceName, final String typeName, final Long version) {
        this.instanceName = instanceName;
        this.typeName = typeName;
        this.version = version;
    }
}
