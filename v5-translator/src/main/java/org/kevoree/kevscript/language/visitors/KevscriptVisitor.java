package org.kevoree.kevscript.language.visitors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.commands.AddCommand;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.excpt.InstanceNameNotFound;
import org.kevoree.kevscript.language.excpt.VersionNotFound;
import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.InstanceExpression;
import org.kevoree.kevscript.language.expressions.StringExpression;
import org.kevoree.kevscript.language.expressions.VersionExpression;

import javax.management.InstanceNotFoundException;

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
    public Commands visitScript(ScriptContext ctx) {
        final Commands c = new Commands();
        for(ParseTree child: ctx.children) {
            final Commands visit = this.visit(child);
            if(visit != null) {
                c.addAll(visit);
            }
        }
        return c;
    }

    @Override
    public Commands visitAdd(AddContext ctx) {
        final Commands ret = new Commands();
        if(ctx.identifier(0) != null) {
            final InstanceExpression parent = context.lookupInstanceIdentifier(new ExpressionVisitor(context).visitIdentifier(ctx.identifier(0)));
            if(ctx.identifier(1) != null) {
                final InstanceExpression child = context.lookupInstanceIdentifier(new ExpressionVisitor(context).visitIdentifier(ctx.identifier(1)));
                final StringExpression parentInstanceName = context.lookupString(parent.instanceName);
                if(parentInstanceName == null) {
                    throw new InstanceNameNotFound(parent.instanceName);
                }

                final StringExpression childInstanceName = context.lookupString(child.instanceName);
                if(childInstanceName == null) {
                    throw new InstanceNameNotFound(child.instanceName);
                }
                final InstanceElement parentElement = new InstanceElement(parentInstanceName.text, parent.instanceTypeDefName);
                final InstanceElement childElement = new InstanceElement(childInstanceName.text, child.instanceTypeDefName);
                ret.add(new AddCommand(parentElement, childElement));
            } else if(ctx.identifierList() != null && ctx.identifierList().identifiers != null) {
                for (final IdentifierContext instance : ctx.identifierList().identifiers) {
                    //context.loo
                    // TODO complete me
                }
            } else {
                final StringExpression instanceName = context.lookupString(parent.instanceName);
                if(instanceName == null) {
                    throw new InstanceNameNotFound(parent.instanceName);
                }

                final Long versionValue;
                if(parent.instanceTypeDefVersion != null) {
                    final VersionExpression version = context.lookupVersion(parent.instanceTypeDefVersion);

                    if (version == null) {
                        throw new VersionNotFound(parent.instanceName);
                    }
                    versionValue = version.version;
                } else {
                    versionValue = null;
                }

                final InstanceElement root = new InstanceElement(instanceName.text, parent.instanceTypeDefName, versionValue);
                ret.add(new AddCommand(root));
            }

        } else if (ctx.LS_BRACKET() != null) {

        } else {

        }
        return ret;
    }



    @Override
    public Commands visitInstance(InstanceContext ctx) {

        // TODO : check for name collision
        final String instanceTypeDefName = ctx.type().typeName().getText();
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
        if(ctx.varName != null) {
            final String instanceVarName = ctx.varName.getText();
            final Expression instanceName;
            if(ctx.instanceName != null) {
                instanceName = new ExpressionVisitor(context).visit(ctx.instanceName);
            } else {
                instanceName = new StringExpression(instanceVarName);
            }
            this.context.getIdentifiers().add(new InstanceExpression(instanceVarName, instanceName, instanceTypeDefName, instanceTypeDefVersion, instanceDeployUnit));
        } else {
            for(VarIdentifierListContext varName : ctx.varNames) {
                final String instanceVarName = varName.getText();
                final Expression instanceName = new StringExpression(varName.getText());
                this.context.getIdentifiers().add(new InstanceExpression(instanceVarName, instanceName, instanceTypeDefName, instanceTypeDefVersion, instanceDeployUnit));
            }
        }
        return super.visitInstance(ctx);
    }
}
