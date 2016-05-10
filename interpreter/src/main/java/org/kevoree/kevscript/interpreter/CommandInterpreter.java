package org.kevoree.kevscript.interpreter;

import org.KevoreeModel;
import org.kevoree.*;
import org.kevoree.kevscript.command.AddCmd;
import org.kevoree.kevscript.command.InstanceCmd;
import org.kevoree.kevscript.exception.AddCommandException;
import org.kevoree.kevscript.exception.UnknownInstanceException;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;
import org.kevoree.kevscript.language.processor.visitor.VisitCallback;
import org.kevoree.kevscript.resolver.RegistryResolver;
import org.kevoree.meta.MetaModel;
import org.kevoree.modeling.KCallback;
import org.kevoree.modeling.KObject;

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
    public void visitCommands(final Commands cmds, final VisitCallback<ModelContext> callback) {
        final String query = "[name=/]";
        this.kModel.find(MetaModel.getInstance(), currentUniverse, currentTime, query, new KCallback<KObject>() {
            @Override
            public void on(KObject o) {
                Model model;
                if (o == null) {
                    System.out.println("create a new one");
                    model = kModel.universe(currentUniverse).time(currentTime).createModel();
                    model.setName("/");
                } else {
                    System.out.println("re-use the previous one");
                    model = (Model) o;
                }
                ModelContext context = new ModelContext(model);
                for (Command cmd : cmds) {
                    cmd.accept(CommandInterpreter.this, context);
                }
                callback.done(context);
            }
        });
    }

    @Override
    public void visitInstanceCommand(InstanceCommand cmd, ModelContext context) {
        InstanceCmd.process(cmd, context, resolver, kModel.universe(currentUniverse).time(currentTime));
    }

    @Override
    public void visitAddCommand(final AddCommand cmd, final ModelContext context) {
        AddCmd.process(cmd, context);
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
