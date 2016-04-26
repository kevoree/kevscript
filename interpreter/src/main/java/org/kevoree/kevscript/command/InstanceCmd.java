package org.kevoree.kevscript.command;

import org.KevoreeView;
import org.kevoree.*;
import org.kevoree.kevscript.interpreter.ModelContext;
import org.kevoree.kevscript.language.commands.InstanceCommand;
import org.kevoree.kevscript.resolver.RegistryResolver;

/**
 *
 */
public class InstanceCmd {

    public static void process(InstanceCommand cmd, ModelContext context, RegistryResolver resolver, KevoreeView view) {
        TypeDefinition tdef = resolver.resolve(view, cmd.typeExpr);
        Instance instance = null;
        if (tdef instanceof NodeType) {
            instance = view.createNode();
        } else if (tdef instanceof ChannelType) {
            instance = view.createChannel();
        } else if (tdef instanceof ComponentType) {
            instance = view.createComponent();
        } else if (tdef instanceof ModelConnectorType) {
            instance = view.createModelConnector();
        }

        instance.setName(cmd.name);
        instance.addTypeDefinition(tdef);
        context.instances.put(cmd.name, instance);
    }
}
