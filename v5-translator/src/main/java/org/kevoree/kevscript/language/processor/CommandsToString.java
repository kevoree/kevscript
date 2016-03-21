package org.kevoree.kevscript.language.processor;

import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.language.commands.*;

/**
 * Created by mleduc on 16/03/16.
 */
public class CommandsToString {
    public String proceed(Commands visit) {
        final StringBuilder sb = new StringBuilder();
        for(AbstractCommand a : visit) {
            sb.append(this.proceed(a));
            sb.append('\n');
        }
        return sb.toString();
    }

    private String proceed(final AbstractCommand command) {
        final String ret;
        if(command instanceof AddCommand) {
            ret = this.proceedAddCommand((AddCommand) command);
        } else if(command instanceof AttachCommand) {
            ret = this.proceedAttachCommand((AttachCommand) command);
        } else if(command instanceof BindCommand) {
            ret = this.proceedBindCommand((BindCommand) command);
        }
        else {
            ret = "";
        }
        return ret;
    }

    private String proceedBindCommand(BindCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("bind ");
        sb.append(command.chan.instanceName);
        sb.append(" ");
        if(command.port.node != null) {
            sb.append(command.port.node.instanceName);
            sb.append('.');
        }
        sb.append(command.port.component.instanceName);
        sb.append('.');
        sb.append(command.port.name);
        return sb.toString();
    }

    private String proceedAttachCommand(final AttachCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("attach ");
        sb.append(command.node.instanceName);
        sb.append(' ');
        sb.append(command.group.instanceName);
        return sb.toString();
    }

    private String proceedAddCommand(final AddCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("add ");
        if(command.parent == null) {
            sb.append(command.child.instanceName);
            sb.append(" : ");
            sb.append(command.child.typeName);
            if(command.child.version != null) {
                sb.append('/');
                sb.append(StringUtils.join(StringUtils.rightPad(String.valueOf(command.child.version), 3, '0').toCharArray(), '.'));
            }
        } else {

        }
        return sb.toString();
    }
}
