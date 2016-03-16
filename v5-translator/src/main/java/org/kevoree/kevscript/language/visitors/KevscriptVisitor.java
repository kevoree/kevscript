package org.kevoree.kevscript.language.visitors;

import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.commands.AddCommand;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.InstanceExpression;
import org.kevoree.kevscript.language.expressions.StringExpression;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 01/03/16.
 */
public class KevscriptVisitor extends KevScriptBaseVisitor<Commands> {

    private final Context context;

    public KevscriptVisitor() {
        this.context = new RootContext();
    }

    @Override
    public Commands visitAdd(AddContext ctx) {
        final Commands ret;
        if(ctx.identifier() != null) {
            ret = new Commands().addCommand(new AddCommand(null, null, null));
        } else if (ctx.LS_BRACKET() != null) {
            ret = new Commands().addCommand(new AddCommand(null, null, null));
        } else {
            ret = new Commands().addCommand(new AddCommand(null, null, null));
        }
        return ret;
    }

    @Override
    public Commands visitInstance(InstanceContext ctx) {
        if(ctx.varName != null) {
            final String instanceVarName = ctx.varName.getText();
            final Expression instanceName;
            if(ctx.instanceName != null) {
                instanceName = new ExpressionVisitor(context).visit(ctx.instanceName);
            } else {
                instanceName = new StringExpression(instanceVarName);
            }
            final Expression instanceTypeDefName = new ExpressionVisitor(context).visit(ctx.type().typeName());
            final Expression instanceTypeDefVersion;
            if(ctx.type().version() != null) {
                instanceTypeDefVersion = new ExpressionVisitor(context).visit(ctx.type().version());
            } else {
                instanceTypeDefVersion = null;
            }
            final Expression instanceDeployUnit;
            if(ctx.type().duVersions() != null) {
                instanceDeployUnit = new ExpressionVisitor(context).visit(ctx.type().duVersions());
            } else {
                instanceDeployUnit = null;
            }
            this.context.getIdentifiers().add(new InstanceExpression(instanceVarName, instanceName, instanceTypeDefName, instanceTypeDefVersion, instanceDeployUnit));
        } else {

        }
        return super.visitInstance(ctx);
    }
}
