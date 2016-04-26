package org.kevoree.kevscript.interpreter;

import org.KevoreeModel;
import org.KevoreeView;
import org.kevoree.*;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;
import org.kevoree.kevscript.resolver.RegistryResolver;

/**
 *
 */
public class CommandInterpreter implements CommandVisitor<ModelContext> {

    private KevoreeModel kModel;
    private RegistryResolver resolver;
    private long currentUniverse = 0;
    private long currentTime = 0;

    public CommandInterpreter(KevoreeModel kModel, RegistryResolver resolver) {
        this.kModel = kModel;
        this.resolver = resolver;
    }

    @Override
    public ModelContext visitCommands(Commands cmds) {
        ModelContext context = new ModelContext(this.kModel.universe(currentUniverse).time(currentTime).createModel());
        for (Command cmd : cmds) {
            cmd.accept(this, context);
        }
        return context;
    }

    @Override
    public void visitInstanceCommand(InstanceCommand cmd, ModelContext context) {
        final KevoreeView kView = this.kModel.universe(currentUniverse).time(currentTime);
        TypeDefinition tdef = this.resolver.resolve(kView, cmd.typeExpr);
        Instance instance = null;
        if (tdef instanceof NodeType) {
            instance = kView.createNode();
        } else if (tdef instanceof ChannelType) {
            instance = kView.createChannel();
        } else if (tdef instanceof ComponentType) {
            instance = kView.createComponent();
        } else if (tdef instanceof ModelConnectorType) {
            instance = kView.createModelConnector();
        }

        if (instance != null) {
            instance.setName(cmd.name);
            context.instances.put(cmd.name, instance);
        }
    }

    @Override
    public void visitAddCommand(AddCommand cmd, ModelContext context) {
        
    }

    @Override
    public void visitAttachCommand(AttachCommand cmd, ModelContext context) {

    }

    @Override
    public void visitBindCommand(BindCommand cmd, ModelContext context) {

    }

    @Override
    public void visitDetachCommand(DetachCommand cmd, ModelContext context) {

    }

    @Override
    public void visitMetaInitCommand(MetaInitCommand cmd, ModelContext context) {

    }

    @Override
    public void visitMetaMergeCommand(MetaMergeCommand cmd, ModelContext context) {

    }

    @Override
    public void visitMetaRemoveCommand(MetaRemoveCommand cmd, ModelContext context) {

    }

    @Override
    public void visitMoveCommand(MoveCommand cmd, ModelContext context) {

    }

    @Override
    public void visitNetInitCommand(NetInitCommand cmd, ModelContext context) {

    }

    @Override
    public void visitNetMergeCommand(NetMergeCommand cmd, ModelContext context) {

    }

    @Override
    public void visitNetRemoveCommand(NetRemoveCommand cmd, ModelContext context) {

    }

    @Override
    public void visitRemoveCommand(RemoveCommand cmd, ModelContext context) {

    }

    @Override
    public void visitSetCommand(SetCommand cmd, ModelContext context) {

    }

    @Override
    public void visitStartCommand(StartCommand cmd, ModelContext context) {

    }

    @Override
    public void visitStopCommand(StopCommand cmd, ModelContext context) {

    }

    @Override
    public void visitTimeCommand(TimeCommand cmd, ModelContext context) {

    }

    @Override
    public void visitUnbindCommand(UnbindCommand cmd, ModelContext context) {

    }

    @Override
    public void visitWorldCommand(WorldCommand cmd, ModelContext context) {

    }
}
