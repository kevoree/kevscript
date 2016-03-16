package org.kevoree.kevscript.language.expressions;

import org.kevoree.kevscript.language.context.Context;

/**
 * Created by mleduc on 02/03/16.
 */
public abstract class Expression {
    public abstract String toText();
    public abstract Expression resolve(Context context);



}
