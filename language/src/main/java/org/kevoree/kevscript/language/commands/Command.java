package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.processor.visitor.CommandVisitor;

/**
 *
 *
 */
public interface Command {

    <T> void accept(CommandVisitor<T> visitor, T context);
}
