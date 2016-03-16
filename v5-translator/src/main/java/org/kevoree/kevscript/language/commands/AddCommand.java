package org.kevoree.kevscript.language.commands;

/**
 * Created by mleduc on 15/03/16.
 */
public class AddCommand extends AbstractCommand {

    private final String instanceName;
    private final String typeDefinition;
    private final String version;

    public AddCommand(String instanceName, String typeDefinition, String version) {
        this.instanceName = instanceName;
        this.typeDefinition = typeDefinition;
        this.version = version;
    }

    public AddCommand(String instanceName, String typeDefinition) {
        this.instanceName = instanceName;
        this.typeDefinition = typeDefinition;
        this.version = null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("add");
        sb.append(' ');
        sb.append(instanceName);
        sb.append(' ');
        sb.append(':');
        sb.append(' ');
        sb.append(typeDefinition);
        if(version != null) {
            sb.append('/');
            sb.append(version);
        }
        return sb.toString();
    }
}
