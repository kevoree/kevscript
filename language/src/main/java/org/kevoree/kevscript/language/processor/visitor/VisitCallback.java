package org.kevoree.kevscript.language.processor.visitor;

/**
 *
 */
public interface VisitCallback<T> {

    void done(T context);
}
