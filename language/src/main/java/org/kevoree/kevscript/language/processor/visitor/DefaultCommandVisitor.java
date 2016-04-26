package org.kevoree.kevscript.language.processor.visitor;

import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.expressions.finalexp.*;

/**
 *
 * Created by mleduc on 20/04/16.
 */
public abstract class DefaultCommandVisitor<T> implements ICommandsVisitor<T> {
    @Override
    public T visitAddCommand(AddCommand addCommand) {
        T r1 = this.visitInstanceExpression(addCommand.source);
        T r2 = this.visitInstanceExpression(addCommand.target);
        return mergeResults(r1, r2);
    }

    protected T mergeResults(T t1, T t2) {
        return t2;
    }

    @Override
    public T visitAttachCommand(AttachCommand attachCommand) {
        final T r1 = this.visitInstanceExpression(attachCommand.group);
        final T r2 = this.visitInstanceExpression(attachCommand.node);
        return mergeResults(r1, r2);
    }

    @Override
    public T visitBindCommand(BindCommand bindCommand) {
        T r1 = this.visitInstanceExpression(bindCommand.chan);
        final T r2 = this.visitPortPathExpression(bindCommand.port);
        return mergeResults(r1, r2);
    }

    @Override
    public T visitCommands(Commands commands) {
        T ret = null;
        for (Command command : commands) {
            final T accept = command.accept(this);
            ret = mergeResults(ret, accept);
        }
        return ret;
    }


    @Override
    public T visitDetachCommand(DetachCommand detachCommand) {
        return this.visitInstanceExpression(detachCommand.instance);
    }

    @Override
    public T visitInstanceCommand(InstanceCommand instanceCommand) {
        return this.visitTypeExpression(instanceCommand.typeExpr);
    }

    @Override
    public T visitMetaInitCommand(MetaInitCommand metaInitCommand) {
        T r1 = this.visitInstanceExpression(metaInitCommand.instance);
        T r2 = this.visitObjectDeclExpression(metaInitCommand.object);
        return mergeResults(r1, r2);
    }

    @Override
    public T visitMetaMergeCommand(MetaMergeCommand metaMergeCommand) {
        T r1 = this.visitInstanceExpression(metaMergeCommand.instance);
        T r2 = this.visitObjectDeclExpression(metaMergeCommand.object);
        return mergeResults(r1, r2);
    }

    @Override
    public T visitMetaRemoveCommand(MetaRemoveCommand metaRemoveCommand) {
        return this.visitInstanceExpression(metaRemoveCommand.instance);
    }

    @Override
    public T visitMoveCommand(MoveCommand moveCommand) {
        T r1 = this.visitInstanceExpression(moveCommand.target);
        T r2 = this.visitInstanceExpression(moveCommand.source);
        return mergeResults(r1, r2);
    }

    @Override
    public T visitNetInitCommand(NetInitCommand netInitCommand) {
        T r1 = this.visitInstanceExpression(netInitCommand.instance);
        T r2 = this.visitObjectDeclExpression(netInitCommand.network);
        return mergeResults(r1, r2);
    }

    @Override
    public T visitnetMergeCommand(NetMergeCommand netMergeCommand) {
        T r1 = this.visitInstanceExpression(netMergeCommand.instance);
        T r2 = this.visitObjectDeclExpression(netMergeCommand.network);
        return mergeResults(r1, r2);
    }

    @Override
    public T visitNetRemoveCommand(NetRemoveCommand netRemoveCommand) {
        return this.visitInstanceExpression(netRemoveCommand.instance);
    }

    @Override
    public T visitRemoveCommand(RemoveCommand removeCommand) {
        return this.visitInstanceExpression(removeCommand.instance);
    }

    @Override
    public T visitSetCommand(SetCommand setCommand) {
        return this.visitDictionaryPathExpression(setCommand.dicPathExpr);
    }

    @Override
    public T visitDictionaryPathExpression(DictionaryPathExpression dicPathExpr) {
        return this.visitInstanceExpression(dicPathExpr.instance);
    }

    @Override
    public T visitStartCommand(StartCommand startCommand) {
        return this.visitInstanceExpression(startCommand.instance);
    }

    @Override
    public T visitStopCommand(StopCommand stopCommand) {
        return this.visitInstanceExpression(stopCommand.instance);
    }

    @Override
    public T visitTimeCommand(TimeCommand timeCommand) {
        return this.visitCommands(timeCommand.commands);
    }

    @Override
    public T visitUnbindCommand(UnbindCommand unbindCommand) {
        T r1 = this.visitInstanceExpression(unbindCommand.chan);
        T r2 = this.visitPortPathExpression(unbindCommand.port);
        return mergeResults(r1, r2);
    }

    @Override
    public T visitWorldCommand(WorldCommand worldCommand) {
        return this.visitCommands(worldCommand.commands);
    }

    @Override
    public T visitInstanceExpression(InstanceExpression instanceExpression) {
        return null;
    }

    @Override
    public T visitTypeExpression(TypeExpression typeExpression) {
        T r1 = this.visitVersionExpression(typeExpression.versionExpr);
        T r2 = this.visitObjectDeclExpression(typeExpression.duVersionsExpr);
        return mergeResults(r1, r2);
    }

    @Override
    public T visitVersionExpression(VersionExpression versionExpression) {
        return null;
    }

    @Override
    public T visitObjectDeclExpression(ObjectDeclExpression objectDeclExpression) {
        return null;
    }

    @Override
    public T visitPortPathExpression(PortPathExpression portPathExpression) {
        return this.visitInstanceExpression(portPathExpression.instance);
    }
}
