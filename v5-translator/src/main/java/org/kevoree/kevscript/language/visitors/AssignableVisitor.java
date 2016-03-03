package org.kevoree.kevscript.language.visitors;

import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.assignable.Assignable;
import org.kevoree.kevscript.language.assignable.LongIdentifierAssignable;
import org.kevoree.kevscript.language.assignable.ObjectAssignable;
import org.kevoree.kevscript.language.assignable.StringAssignable;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 02/03/16.
 */
public class AssignableVisitor extends KevScriptBaseVisitor<Assignable> {

    @Override
    public Assignable visitAssignable(AssignableContext ctx) {
        final Assignable ret;
        if(ctx.string() != null) {
            ret = visit(ctx.string());
        } else if(ctx.object() != null) {
            ret = visit(ctx.object());
        } else if(ctx.long_identifier() != null) {
            ret = visit(ctx.long_identifier());
        } else {
            throw new IllegalStateException("Assignable type not implemented yed : " + ctx.getText());
        }
        return ret;
    }

    @Override
    public Assignable visitDq_string(Dq_stringContext ctx) {
        return new StringAssignable(ctx.getChild(1).getText());
    }

    @Override
    public Assignable visitSq_string(Sq_stringContext ctx) {
        return new StringAssignable(ctx.getChild(1).getText());
    }

    @Override
    public Assignable visitObject(ObjectContext ctx) {
        ObjectAssignable object = new ObjectAssignable();
        for(KeyAndValueContext it: ctx.keyAndValue()) {
            object.put(it.long_identifier().getText(), visit(it.assignable()));
        }
        return object;
    }

    @Override
    public Assignable visitLong_identifier(Long_identifierContext ctx) {
        final LongIdentifierAssignable ret = new LongIdentifierAssignable();
        for(Long_identifier_chunkContext a : ctx.identifiers) {
            if(a.AT() != null)  {
                ret.add(LongIdentifierAssignable.at(a.assignable().getText()));
            } else {
                ret.add(LongIdentifierAssignable.identifier(a.short_identifier().getText()));
            }
        }
        return ret;
    }
}
