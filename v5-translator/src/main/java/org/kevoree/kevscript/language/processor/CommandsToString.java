package org.kevoree.kevscript.language.processor;

import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.commands.element.DictionaryElement;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.commands.element.PortElement;
import org.kevoree.kevscript.language.commands.element.RootInstanceElement;

/**
 * Created by mleduc on 16/03/16.
 */
public class CommandsToString {
    public String proceed(Commands visit) {
        final StringBuilder sb = new StringBuilder();
        for (AbstractCommand a : visit) {
            sb.append(this.proceed(a));
            sb.append('\n');
        }
        return sb.toString();
    }

    private String proceed(final AbstractCommand command) {
        final String ret;
        if (command instanceof AddCommand) {
            ret = this.proceedAddCommand((AddCommand) command);
        } else if (command instanceof AttachCommand) {
            ret = this.proceedAttachCommand((AttachCommand) command);
        } else if (command instanceof BindCommand) {
            ret = this.proceedBindCommand((BindCommand) command);
        } else if (command instanceof DetachCommand) {
            ret = this.proceedDetachCommand((DetachCommand) command);
        } else if (command instanceof UnbindCommand) {
            ret = this.proceedUnbindCommand((UnbindCommand) command);
        } else if (command instanceof SetCommand) {
            ret = this.proceedSetCommand((SetCommand) command);
        } else if (command instanceof RemoveCommand) {
            ret = this.proceedRemoveCommand((RemoveCommand) command);
        } else if (command instanceof MoveCommand) {
            ret = this.proceedMoveCommand((MoveCommand) command);
        } else if (command instanceof StartCommand) {
            ret = this.proceedStartCommand((StartCommand) command);
        } else if (command instanceof StopCommand) {
            ret = this.proceedStopCommand((StopCommand) command);
        } else {
            ret = "";
        }
        return ret;
    }

    private String proceedStartCommand(StartCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("start ");
        this.processInstanceElement(sb, command.instance);
        return sb.toString();
    }

    private String proceedStopCommand(StopCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("stop ");
        this.processInstanceElement(sb, command.instance);
        return sb.toString();
    }

    private String proceedMoveCommand(MoveCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("move ");
        processInstanceElement(sb, command.targetInstance);
        sb.append(' ');
        processInstanceElement(sb, command.sourceInstance);
        return sb.toString();
    }

    private String proceedRemoveCommand(RemoveCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("remove ");
        final InstanceElement instance = command.instance;
        processInstanceElement(sb, instance);
        return sb.toString();
    }

    private void processInstanceElement(StringBuilder sb, InstanceElement instance) {
        final RootInstanceElement parent = instance.parent;
        if (parent != null) {
            sb.append(parent.instanceName);
            sb.append('.');
        }
        final RootInstanceElement child = instance.child;
        sb.append(child.instanceName);
    }

    private String proceedSetCommand(SetCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("set ");
        final DictionaryElement dictionaryElement = command.dictionaryElement;
        final InstanceElement instance = dictionaryElement.instance;
        processInstanceElement(sb, instance);
        sb.append('.');
        sb.append(dictionaryElement.dicoName);

        if (dictionaryElement.frag != null) {
            sb.append('/');
            sb.append(dictionaryElement.frag);
        }

        sb.append(" = ");
        sb.append('"');
        sb.append(command.value);
        sb.append('"');
        return sb.toString();
    }

    private String proceedUnbindCommand(UnbindCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("unbind ");
        final RootInstanceElement chan = command.chan;
        sb.append(chan.instanceName);
        sb.append(" ");
        final PortElement port = command.port;
        final InstanceElement instance = port.instance;
        if (instance.parent != null) {
            sb.append(instance.parent.instanceName);
            sb.append('.');
        }
        sb.append(instance.child.instanceName);
        sb.append('.');
        sb.append(port.name);
        return sb.toString();
    }

    private String proceedDetachCommand(DetachCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("detach ");
        final RootInstanceElement node = command.node;
        sb.append(node.instanceName);
        sb.append(' ');
        final RootInstanceElement group = command.group;
        sb.append(group.instanceName);
        return sb.toString();
    }

    private String proceedBindCommand(BindCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("bind ");
        sb.append(command.chan.instanceName);
        sb.append(" ");
        processInstanceElement(sb, command.port.instance);
        sb.append('.');
        sb.append(command.port.name);
        return sb.toString();
    }

    private String proceedAttachCommand(final AttachCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("attach ");
        final RootInstanceElement node = command.node;
        sb.append(node.instanceName);
        sb.append(' ');
        final RootInstanceElement group = command.group;
        sb.append(group.instanceName);
        return sb.toString();
    }

    private String proceedAddCommand(final AddCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("add ");
        final InstanceElement instance = command.instance;
        final RootInstanceElement parent = instance.parent;
        if (parent == null) {
            final RootInstanceElement child = instance.child;
            sb.append(child.instanceName);
            sb.append(" : ");
            sb.append(child.typeName);
            if (child.version != null) {
                sb.append('/');
                sb.append(StringUtils.join(StringUtils.rightPad(String.valueOf(child.version), 3, '0').toCharArray(), '.'));
            }
        } else {

        }
        return sb.toString();
    }
}
