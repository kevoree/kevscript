package org.kevoree.kevscript.language;

import org.kevoree.kevscript.KevScriptBaseListener;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 29/02/16.
 */
public class KevscriptListener extends KevScriptBaseListener {
    @Override
    public void enterAdd(AddContext ctx) {
        super.enterAdd(ctx);
        //System.out.println("add " + ctx.list_add_members.getText() +":"+ctx.typeDef.getText());
    }
}
