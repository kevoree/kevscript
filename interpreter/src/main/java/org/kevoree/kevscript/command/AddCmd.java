package org.kevoree.kevscript.command;

import org.kevoree.*;
import org.kevoree.kevscript.exception.AddCommandException;
import org.kevoree.kevscript.exception.UnknownInstanceException;
import org.kevoree.kevscript.interpreter.ModelContext;
import org.kevoree.kevscript.language.commands.AddCommand;
import org.kevoree.modeling.KCallback;

/**
 *
 */
public class AddCmd {

    public static void process(final AddCommand cmd, final ModelContext context) {
        if (context.instances.containsKey(cmd.source.instanceName)) {
            final Instance source = context.instances.get(cmd.source.instanceName);
            if (cmd.target.toText().equals("/")) {
                // add cmd.source to model root
                source.getTypeDefinition(new KCallback<TypeDefinition[]>() {
                    @Override
                    public void on(TypeDefinition[] typeDefinitions) {
                        if (typeDefinitions[0] instanceof NodeType) {
                            context.model.addNodes((Node) source);
                        } else if (typeDefinitions[0] instanceof ChannelType) {
                            context.model.addChannels((Channel) source);
                        } else {
                            throw new AddCommandException(cmd.source.toText(), cmd.target.toText(), cmd.source.toText()
                                    + " must be a NodeType or a ChannelType to be added to model root");
                        }
                    }
                });

            } else {
                // add cmd.source to cmd.target
                final Instance target = context.instances.get(cmd.target.instanceName);
                if (target == null) {
                    context.model.select("nodes[name=" + cmd.target.instanceName + "]", new KCallback<Object[]>() {
                        @Override
                        public void on(Object[] objects) {
                            if (objects.length == 1) {
                                if (objects[0] != null) {
                                    processTarget(source, (Instance) objects[0]);
                                }
                            } else {
                                throw new UnknownInstanceException(cmd.target.instanceName);
                            }
                        }
                    });
                } else {
                    processTarget(source, target);
                }
            }
        } else {
            // TODO enhance exception handling to know the line and column ?
            throw new UnknownInstanceException(cmd.source.instanceName);
        }
    }

    private static void processTarget(final Instance source, final Instance target) {
        target.getTypeDefinition(new KCallback<TypeDefinition[]>() {
            @Override
            public void on(TypeDefinition[] typeDefinitions) {
                if (typeDefinitions[0] instanceof NodeType) {
                    // then source can be a Component or a ModelConnector
                    source.getTypeDefinition(new KCallback<TypeDefinition[]>() {
                        @Override
                        public void on(TypeDefinition[] typeDefinitions) {
                            if (typeDefinitions[0] instanceof ComponentType) {
                                ((Node) target).addComponents((Component) source);
                            } else if (typeDefinitions[0] instanceof ModelConnectorType) {
                                ((Node) target).addModelConnector((ModelConnector) source);
                            } else {
                                throw new AddCommandException(source.getName(), target.getName(),
                                        source.getName() +
                                                " must be a NodeType or a ChannelType to be added to model root");
                            }
                        }
                    });
                } else {
                    throw new AddCommandException();
                }
            }
        });
    }
}
