package org.kevoree.kevscript.language.processor;

import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.commands.element.DictionaryElement;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.commands.element.PortElement;
import org.kevoree.kevscript.language.commands.element.RootInstanceElement;
import org.kevoree.kevscript.language.commands.element.object.AbstractObjectElement;
import org.kevoree.kevscript.language.commands.element.object.ArrayElement;
import org.kevoree.kevscript.language.commands.element.object.ObjectElement;
import org.kevoree.kevscript.language.commands.element.object.StringElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        } else if (command instanceof NetInitCommand) {
            ret = this.proceedNetInitCommand((NetInitCommand) command);
        } else if (command instanceof NetMergeCommand) {
            ret = this.proceedNetMergeCommand((NetMergeCommand) command);
        } else if (command instanceof NetRemoveCommand) {
            ret = this.proceedNetRemoveCommand((NetRemoveCommand) command);
        } else if (command instanceof MetaInitCommand) {
            ret = this.proceedMetaInitCommand((MetaInitCommand) command);
        } else if (command instanceof MetaMergeCommand) {
            ret = this.proceedMetaMergeCommand((MetaMergeCommand) command);
        } else if (command instanceof MetaRemoveCommand) {
            ret = this.proceedMetaRemoveCommand((MetaRemoveCommand) command);
        } else {
            ret = "";
        }
        return ret;
    }

    private String proceedMetaRemoveCommand(MetaRemoveCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("meta-remove ");
        sb.append(command.instance.instanceName);
        sb.append(" ");
        if (command.objectRefs.size() > 1) {
            sb.append("[");
            sb.append(StringUtils.join(command.objectRefs, ", "));
            sb.append("]");
        } else {
            sb.append(StringUtils.join(command.objectRefs, ""));
        }
        return sb.toString();
    }

    private String proceedMetaMergeCommand(MetaMergeCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("meta-merge ");
        sb.append(command.instance.instanceName);
        sb.append(' ');
        sb.append(proceedObjectElement(command.metas));
        return sb.toString();
    }

    private String proceedMetaInitCommand(MetaInitCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("net-init ");
        sb.append(command.instance.instanceName);
        sb.append(' ');
        sb.append(proceedObjectElement(command.metas));
        return sb.toString();
    }

    private String proceedNetRemoveCommand(final NetRemoveCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("net-remove ");
        sb.append(command.node.instanceName);
        sb.append(" ");
        if (command.objectRefs.size() > 1) {
            sb.append("[");
            sb.append(StringUtils.join(command.objectRefs, ", "));
            sb.append("]");
        } else {
            sb.append(StringUtils.join(command.objectRefs, ""));
        }
        return sb.toString();
    }

    private String proceedNetMergeCommand(NetMergeCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("net-merge ");
        sb.append(command.node.instanceName);
        sb.append(' ');
        sb.append(proceedObjectElement(command.network));
        return sb.toString();
    }

    private String proceedNetInitCommand(final NetInitCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("net-init ");
        sb.append(command.node.instanceName);
        sb.append(' ');
        sb.append(proceedObjectElement(command.network));
        return sb.toString();
    }

    private String proceedObjectElement(final ObjectElement objectElement) {
        final StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        final List<String> keyValues = new ArrayList<>();
        for (final Map.Entry<String, AbstractObjectElement> entry : objectElement.entrySet()) {
            final AbstractObjectElement abstractObjectElement = entry.getValue();
            final String value = proceedAbstractObjectElement(abstractObjectElement);
            keyValues.add(entry.getKey() + " : " + value);
        }
        sb.append(StringUtils.join(keyValues, ", "));
        sb.append(" }");
        return sb.toString();
    }

    private String proceedArrayElement(final ArrayElement arrayElement) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        final List<String> keyValues = new ArrayList<>();
        for (final AbstractObjectElement abstractObjectElement : arrayElement) {
            final String value = proceedAbstractObjectElement(abstractObjectElement);
            keyValues.add(value);
        }
        sb.append(StringUtils.join(keyValues, ", "));
        sb.append(" ]");
        return sb.toString();
    }

    private String proceedAbstractObjectElement(AbstractObjectElement abstractObjectElement) {
        final String value;
        if (abstractObjectElement instanceof ObjectElement) {
            value = proceedObjectElement((ObjectElement) abstractObjectElement);
        } else if (abstractObjectElement instanceof ArrayElement) {
            value = proceedArrayElement((ArrayElement) abstractObjectElement);
        } else {
            value = '"' + ((StringElement) abstractObjectElement).text + '"';
        }
        return value;
    }

    private String proceedStartCommand(StartCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("start ");
        sb.append(this.processInstanceElement(command.instance));
        return sb.toString();
    }

    private String proceedStopCommand(StopCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("stop ");
        sb.append(this.processInstanceElement(command.instance));
        return sb.toString();
    }

    private String proceedMoveCommand(MoveCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("move ");
        sb.append(processInstanceElement(command.targetInstance));
        sb.append(' ');
        sb.append(processInstanceElement(command.sourceInstance));
        return sb.toString();
    }

    private String proceedRemoveCommand(RemoveCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("remove ");
        final InstanceElement instance = command.instance;
        sb.append(processInstanceElement(instance));
        return sb.toString();
    }

    private String processInstanceElement(InstanceElement instance) {
        StringBuilder sb = new StringBuilder();
        final RootInstanceElement parent = instance.parent;
        if (parent != null) {
            sb.append(parent.instanceName);
            sb.append('.');
        }
        final RootInstanceElement child = instance.child;
        sb.append(child.instanceName);
        return sb.toString();
    }

    private String proceedSetCommand(SetCommand command) {
        final StringBuilder sb = new StringBuilder();
        sb.append("set ");
        final DictionaryElement dictionaryElement = command.dictionaryElement;
        final InstanceElement instance = dictionaryElement.instance;
        sb.append(processInstanceElement(instance));
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
        sb.append(processInstanceElement(command.port.instance));
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
        sb.append(this.processInstanceElement(instance));
        final RootInstanceElement child = instance.child;
        sb.append(" : ");
        sb.append(child.typeName);
        if (child.version != null) {
            sb.append('/');
            sb.append(StringUtils.join(StringUtils.rightPad(String.valueOf(child.version), 3, '0').toCharArray(), '.'));
        }
        return sb.toString();
    }
}
