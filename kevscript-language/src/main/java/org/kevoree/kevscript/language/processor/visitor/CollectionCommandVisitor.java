package org.kevoree.kevscript.language.processor.visitor;

import java.util.Collection;

/**
 * Created by mleduc on 21/04/16.
 */
public class CollectionCommandVisitor<T extends Collection<U>, U> extends DefaultCommandVisitor<T> {


    @Override
    protected T mergeResults(T t1, T t2) {
        T ret;
        if(t1 == null) {
            ret = t2;
        } else {
            if(t2 == null) {
                ret = t1;
            } else {
                t1.addAll(t2);
                ret = t1;
            }
        }

        return ret;
    }
}
