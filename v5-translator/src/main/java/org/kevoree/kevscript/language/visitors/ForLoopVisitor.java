package org.kevoree.kevscript.language.visitors;

import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.context.Context;

/**
 * Created by mleduc on 02/03/16.
 */
public class ForLoopVisitor extends KevScriptBaseVisitor<String> {

    final Context context;
    public ForLoopVisitor(Context context) {
        this.context = context;
    }


}
