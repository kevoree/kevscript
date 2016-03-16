package org.kevoree.kevscript.language;

import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.language.commands.AbstractCommand;
import org.kevoree.kevscript.language.commands.AddCommand;
import org.kevoree.kevscript.language.commands.Commands;

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
        } else {
            ret = "";
        }
        return ret;
    }

    private String proceedAddCommand(AddCommand command) {
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
