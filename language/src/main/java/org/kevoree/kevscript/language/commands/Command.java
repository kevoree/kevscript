package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public interface Command {

    <T> T accept(DefaultCommandVisitor<T> visitor);
}
