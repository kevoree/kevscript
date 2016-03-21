package org.kevoree.kevscript.language.visitors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.commands.AddCommand;
import org.kevoree.kevscript.language.commands.AttachCommand;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.InstanceNameNotFound;
import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.InstanceExpression;
import org.kevoree.kevscript.language.expressions.StringExpression;
import org.kevoree.kevscript.language.expressions.VersionExpression;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 01/03/16.
 */
public class KevscriptVisitor extends KevScriptBaseVisitor<Commands> {

    private final Context context;

    public KevscriptVisitor() {
        this.context = new Context();
    }

    @Override
    public Commands visitScript(final ScriptContext ctx) {
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
    public Commands visitAdd(final AddContext ctx) {
        final Commands ret = new Commands();
        if(ctx.identifier(0) != null) {
            final InstanceExpression parent = context.lookup(new ExpressionVisitor(context).visitIdentifier(ctx.identifier(0)), InstanceExpression.class);
            if(ctx.identifier(1) != null) {
                final InstanceExpression child = context.lookup(new ExpressionVisitor(context).visitIdentifier(ctx.identifier(1)), InstanceExpression.class);
                final StringExpression parentInstanceName = context.lookupString(parent.instanceName);
                final StringExpression childInstanceName = context.lookupString(child.instanceName);
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
                final Long versionValue = convertVersionToLong(parent.instanceTypeDefVersion);
                final InstanceElement root = new InstanceElement(instanceName.text, parent.instanceTypeDefName, versionValue);
                ret.add(new AddCommand(root));
            }

        } else if (ctx.LS_BRACKET() != null) {
            for(IdentifierContext identifier : ctx.identifierList().identifier()) {
                final InstanceExpression parent = context.lookup(new ExpressionVisitor(context).visitIdentifier(identifier), InstanceExpression.class);
                final StringExpression instanceName = context.lookupString(parent.instanceName);
                final Long versionValue = convertVersionToLong(parent.instanceTypeDefVersion);
                final InstanceElement root = new InstanceElement(instanceName.text, parent.instanceTypeDefName, versionValue);
                ret.add(new AddCommand(root));
            }
        } else {

        }
        return ret;
    }

    private Long convertVersionToLong(final Expression expression) {
        final Long versionValue;
        if(expression != null) {
            final VersionExpression version = context.lookupVersion(expression);
            versionValue = version.version;
        } else {
            versionValue = null;
        }
        return versionValue;
    }


    @Override
    public Commands visitInstance(final InstanceContext ctx) {
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
            this.context.addExpression(new InstanceExpression(instanceVarName, instanceName, instanceTypeDefName, instanceTypeDefVersion, instanceDeployUnit));
        } else {
            for(Basic_identifierContext varName : ctx.varIdentifierList().basic_identifier()) {
                final String instanceVarName = varName.getText();
                final Expression instanceName = new StringExpression(varName.getText());
                this.context.addExpression(new InstanceExpression(instanceVarName, instanceName, instanceTypeDefName, instanceTypeDefVersion, instanceDeployUnit));
            }
        }
        return super.visitInstance(ctx);
    }

    @Override
    public Commands visitAttach(final AttachContext ctx) {
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);

        // connector conversion from expression to command element
        final Expression connectorExpression = expressionVisitor.visit(ctx.identifier());
        final InstanceExpression connectorInstance = context.lookup(connectorExpression, InstanceExpression.class);
        if(connectorInstance == null) {
            throw new InstanceNameNotFound(connectorExpression);
        }
        final StringExpression connectorName = context.lookupString(connectorInstance.instanceName);
        final Long connectorVersion = convertVersionToLong(connectorInstance.instanceTypeDefVersion);
        final InstanceElement connector = new InstanceElement(connectorName.text, connectorInstance.instanceTypeDefName, connectorVersion);

        // node conversion from expression to command element
        final Commands ret = new Commands();
        for(final IdentifierContext node : ctx.nodesId.identifier()) {
            final Expression nodeExpression = expressionVisitor.visit(node);
            final InstanceExpression nodeInstance = context.lookup(nodeExpression, InstanceExpression.class);
            if (nodeInstance == null) {
                throw new InstanceNameNotFound(nodeExpression);
            }
            final StringExpression nodeName = context.lookupString(nodeInstance.instanceName);
            final Long nodeVersion = convertVersionToLong(nodeInstance.instanceTypeDefVersion);
            final InstanceElement nodeInstanceElement = new InstanceElement(nodeName.text, nodeInstance.instanceTypeDefName, nodeVersion);

            // command instanciation
            final AttachCommand command = new AttachCommand(connector, nodeInstanceElement);
            ret.addCommand(command);
        }
        return ret;
    }

    @Override
    public Commands visitLetDecl(final LetDeclContext ctx) {
        final Expression res = new ExpressionVisitor(context).visit(ctx.val);
        res.setName(ctx.basic_identifier().getText());
        this.context.addExpression(res);
        return super.visitLetDecl(ctx);
    }
}
