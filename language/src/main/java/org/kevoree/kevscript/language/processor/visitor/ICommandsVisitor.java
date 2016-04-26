package org.kevoree.kevscript.language.processor.visitor;

import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.expressions.finalexp.*;

/**
 * Created by mleduc on 20/04/16.
 */
public interface ICommandsVisitor<T> {
    T visitAddCommand(AddCommand addCommand);

    T visitAttachCommand(AttachCommand attachCommand);

    T visitBindCommand(BindCommand bindCommand);

    T visitCommands(Commands commands);

    T visitDetachCommand(DetachCommand detachCommand);

    T visitInstanceCommand(InstanceCommand instanceCommand);

    T visitMetaInitCommand(MetaInitCommand metaInitCommand);

    T visitMetaMergeCommand(MetaMergeCommand metaMergeCommand);

    T visitMetaRemoveCommand(MetaRemoveCommand metaRemoveCommand);

    T visitMoveCommand(MoveCommand moveCommand);

    T visitNetInitCommand(NetInitCommand netInitCommand);

    T visitnetMergeCommand(NetMergeCommand netMergeCommand);

    T visitNetRemoveCommand(NetRemoveCommand netRemoveCommand);

    T visitRemoveCommand(RemoveCommand removeCommand);

    T visitSetCommand(SetCommand setCommand);

    T visitStartCommand(StartCommand startCommand);

    T visitStopCommand(StopCommand stopCommand);

    T visitTimeCommand(TimeCommand timeCommand);

    T visitUnbindCommand(UnbindCommand unbindCommand);

    T visitWorldCommand(WorldCommand worldCommand);

    T visitInstanceExpression(InstanceExpression instanceExpression);

    T visitTypeExpression(TypeExpression typeExpression);

    T visitVersionExpression(VersionExpression versionExpression);

    T visitObjectDeclExpression(ObjectDeclExpression objectDeclExpression);

    T visitPortPathExpression(PortPathExpression portPathExpression);

    T visitDictionaryPathExpression(DictionaryPathExpression dicPathExpr);
    // TODO add missing datastructure elements.
}
