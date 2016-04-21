package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.processor.visitor.DefaultCommandVisitor;

/**
 *
 *
 */
public interface ICommand {

    <T> T accept(DefaultCommandVisitor<T> visitor);
}
