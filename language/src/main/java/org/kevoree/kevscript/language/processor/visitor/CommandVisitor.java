package org.kevoree.kevscript.language.processor.visitor;

import org.kevoree.kevscript.language.commands.*;

/**
 *
 */
public interface CommandVisitor<T> {

    void visitCommands(final Commands cmds, final VisitCallback<T> callback);

    void visitAddCommand(final AddCommand cmd, final T context);

    void visitAttachCommand(final AttachCommand cmd, final T context);

    void visitBindCommand(final BindCommand cmd, final T context);

    void visitDetachCommand(final DetachCommand cmd, final T context);

    void visitInstanceCommand(final InstanceCommand cmd, final T context);

    void visitMetaInitCommand(final MetaInitCommand cmd, final T context);

    void visitMetaMergeCommand(final MetaMergeCommand cmd, final T context);

    void visitMetaRemoveCommand(final MetaRemoveCommand cmd, final T context);

    void visitMoveCommand(final MoveCommand cmd, final T context);

    void visitNetInitCommand(final NetInitCommand cmd, final T context);

    void visitNetMergeCommand(final NetMergeCommand cmd, final T context);

    void visitNetRemoveCommand(final NetRemoveCommand cmd, final T context);

    void visitRemoveCommand(final RemoveCommand cmd, final T context);

    void visitSetCommand(final SetCommand cmd, final T context);

    void visitStartCommand(final StartCommand cmd, final T context);

    void visitStopCommand(final StopCommand cmd, final T context);

    void visitTimeCommand(final TimeCommand cmd, final T context);

    void visitUnbindCommand(final UnbindCommand cmd, final T context);

    void visitWorldCommand(final WorldCommand cmd, final T context);
}
