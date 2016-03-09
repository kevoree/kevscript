package org.kevoree.kevscript.language.visitors;

import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.assignable.*;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.excpt.CustomException;

import java.util.Iterator;

import static org.kevoree.kevscript.KevScriptParser.*;
import static org.kevoree.kevscript.language.assignable.IdentifierAssignable.*;

/**
 * Created by mleduc on 02/03/16.
 */
public class AssignableVisitor extends KevScriptBaseVisitor<Assignable> {

    private final Context context;

    public AssignableVisitor(Context context) {
        this.context = context;
    }

    /*@Override
    public Assignable visitAssignable(AssignableContext ctx) {
        final Assignable ret = null;
//        if(ctx.string() != null) {
//            ret = visit(ctx.string());
//        } else if(ctx.object() != null) {
//            ret = visit(ctx.object());
//        } else if(ctx.long_identifier() != null) {
//            ret = visit(ctx.long_identifier());
//        } else if(ctx.func_call() != null) {
//            ret = visit(ctx.func_call());
//        } else if(ctx.dereference() != null) {
//            ret = visit(ctx.dereference());
//        } else {
//            throw new IllegalStateException("Assignable type not implemented yet : " + ctx.getText());
//        }
        return ret;
    }*/

    @Override
    public Assignable visitString(StringContext ctx) {
        final String text = ctx.getText();
        return new StringAssignable(text.substring(1, text.length()-1));
    }

//    @Override
//    public Assignable visitObject(ObjectContext ctx) {
//        ObjectAssignable object = new ObjectAssignable();
//        for(KeyAndValueContext it: ctx.values) {
//            object.put(it.key.getText(), visit(it.assignable()));
//        }
//        return object;
//    }

//    @Override
//    public Assignable visitLong_identifier(Long_identifierContext ctx) {
//        final IdentifierAssignable ret = new IdentifierAssignable();
//        for(Long_identifier_chunkContext a : ctx.identifiers) {
//            final Chunk chunk;
//            if(a.dereference() != null)  {
//                final String identifier = a.dereference().assignable().getText();
//                chunk = at(identifier);
//            } else {
//                final String identifier = a.short_identifier().getText();
//                chunk = identifier(identifier);
//            }
//            ret.add(chunk);
//        }
//        return ret;
//    }

    /**
     * This implementation of function_call is call when
     * @param functionCall
     * @return
     */
//    @Override
//    public FunctionAssignable visitFunction_call(Function_callContext functionCall) {
//
//        final String functionName = functionCall.ID().getText();
//        if (!this.context.getSetFunctions().containsKey(functionName)) {
//            throw new CustomException("unknow function " + functionName);
//        }
//        final Function_operationContext functionContext = this.context.getSetFunctions().get(functionName);
//        final Function_bodyContext functionBody = functionContext.function_body();
//        final RootContext context = new RootContext(this.context);
//        if (functionContext.parameters != null) {
//            if ((functionCall.parameters != null || functionContext.parameters != null) &&
//                    functionCall.parameters.assignable().size() != functionContext.parameters.assignable().size()) {
//                throw new CustomException("Wrong number of parameters");
//            }
//            final Iterator<AssignableContext> iteratorParams = functionCall.parameters.assignable().iterator();
//            for (AssignableContext paramName : functionContext.parameters.assignable()) {
//                final AssignableContext paramValue = iteratorParams.next();
//                final String paramNameStr = paramName.getText();
//                final Assignable assignableBeforeResolve = new AssignableVisitor(context).visit(paramValue);
//                final Assignable paramAssignable = assignableBeforeResolve.resolve(this.context);
//                if(paramAssignable == null) {
//                    throw new CustomException("Unknow variable " + paramValue.getText());
//                } else {
//                    context.getMapIdentifiers().put(paramNameStr, paramAssignable);
//                }
//            }
//
//        }
//        final String bodyText = new KevscriptVisitor(context).visit(functionBody);
//        final FunctionAssignable functionAssignable;
//        if(functionContext.RETURN() != null) {
//            final Assignable funcationVariableAssignable = this.visit(functionContext.assignable());
//            final Assignable returnVariableAssignableResolved = funcationVariableAssignable.resolve(context);
//            functionAssignable = new FunctionAssignable(bodyText, returnVariableAssignableResolved);
//        } else {
//            functionAssignable = new FunctionAssignable(bodyText);
//        }
//        return functionAssignable;
//    }
}
