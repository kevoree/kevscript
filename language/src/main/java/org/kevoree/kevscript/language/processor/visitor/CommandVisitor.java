package org.kevoree.kevscript.language.processor.visitor;

import org.kevoree.kevscript.language.commands.*;

/**
 *
 */
public interface CommandVisitor<T> {

    T visitCommands(Commands cmds);

    void visitAddCommand(AddCommand cmd, T context);

    void visitAttachCommand(AttachCommand cmd, T context);

    void visitBindCommand(BindCommand cmd, T context);

    void visitDetachCommand(DetachCommand cmd, T context);

    void visitInstanceCommand(InstanceCommand cmd, T context);

    void visitMetaInitCommand(MetaInitCommand cmd, T context);

    void visitMetaMergeCommand(MetaMergeCommand cmd, T context);

    void visitMetaRemoveCommand(MetaRemoveCommand cmd, T context);

    void visitMoveCommand(MoveCommand cmd, T context);

    void visitNetInitCommand(NetInitCommand cmd, T context);

    void visitNetMergeCommand(NetMergeCommand cmd, T context);

    void visitNetRemoveCommand(NetRemoveCommand cmd, T context);

    void visitRemoveCommand(RemoveCommand cmd, T context);

    void visitSetCommand(SetCommand cmd, T context);

    void visitStartCommand(StartCommand cmd, T context);

    void visitStopCommand(StopCommand cmd, T context);

    void visitTimeCommand(TimeCommand cmd, T context);

    void visitUnbindCommand(UnbindCommand cmd, T context);

    void visitWorldCommand(WorldCommand cmd, T context);
}
