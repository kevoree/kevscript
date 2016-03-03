package org.kevoree.kevscript.language.assignable;

import org.kevoree.kevscript.language.context.Context;

/**
 * Created by mleduc on 02/03/16.
 */
public abstract class Assignable {
    public abstract String toText();
    public abstract Assignable resolve(Context context);
}
