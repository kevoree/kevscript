package org.kevoree.kevscript.language.visitors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.commands.element.DictionaryElement;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.commands.element.PortElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.InstanceNameNotFound;
import org.kevoree.kevscript.language.excpt.PortPathNotFound;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.*;
import org.kevoree.kevscript.language.visitors.helper.KevscriptHelper;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 01/03/16.
 */
public class KevscriptVisitor extends KevScriptBaseVisitor<Commands> {

    private final Context context;
    private final KevscriptHelper helper;

    public KevscriptVisitor() {
        this.context = new Context();
        this.helper = new KevscriptHelper();
    }

    public KevscriptVisitor(final Context context) {
        this.context = new Context(context);
        this.helper = new KevscriptHelper();
    }

    @Override
    public Commands visitScript(final ScriptContext ctx) {
        final Commands c = new Commands();
        for (ParseTree child : ctx.children) {
            final Commands visit = this.visit(child);
            if (visit != null) {
                c.addAll(visit);
            }
        }
        return c;
    }

    @Override
    public Commands visitAdd(final AddContext ctx) {
        final Commands ret = new Commands();
        if (ctx.identifier(0) != null) {
            /*final FinalExpression instanceIdentifier = new ExpressionVisitor(context).visitIdentifier();
            final InstanceExpression parent = context.lookup(instanceIdentifier, InstanceExpression.class);*/
            final InstanceElement parent = this.helper.getInstanceFromContext(context, ctx.identifier(0));
            if (ctx.identifier(1) != null) {
                final InstanceExpression child = context.lookup(new ExpressionVisitor(context).visitIdentifier(ctx.identifier(1)), InstanceExpression.class);
                //final StringExpression parentInstanceName = context.lookupByStrKey(parent.instanceName, StringExpression.class);
                //final StringExpression childInstanceName = context.lookupBy(, StringExpression.class);
                //final InstanceElement parentElement = new InstanceElement(parentInstanceName.toText(), parent.instanceTypeDefName);
                final InstanceElement childElement = new InstanceElement(child.instanceName, child.instanceTypeDefName);
                ret.add(new AddCommand(parent, childElement));
            } else if (ctx.identifierList() != null && ctx.identifierList().identifiers != null) {
                for (final IdentifierContext instance : ctx.identifierList().identifiers) {
                    // TODO complete me
                }
            } else {
                //final Long versionValue = helper.convertVersionToLong(this.context, parent.version);
                ret.add(new AddCommand(parent));
            }

        } else if (ctx.LS_BRACKET() != null) {
            for (IdentifierContext identifier : ctx.identifierList().identifier()) {
                final InstanceExpression parent = context.lookup(new ExpressionVisitor(context).visitIdentifier(identifier), InstanceExpression.class);
                final String instanceName = parent.instanceName;
                final Long versionValue = helper.convertVersionToLong(this.context, parent.instanceTypeDefVersion);
                final InstanceElement root = new InstanceElement(instanceName, parent.instanceTypeDefName, versionValue);
                ret.add(new AddCommand(root));
            }
        } else {

        }
        return ret;
    }

    @Override
    public Commands visitInstance(final InstanceContext ctx) {
        final String instanceTypeDefName = ctx.type().typeName().getText();
        final VersionExpression instanceTypeDefVersion;
        if (ctx.type().version() != null) {
            instanceTypeDefVersion = new ExpressionVisitor(context).visitVersion(ctx.type().version());
        } else {
            instanceTypeDefVersion = null;
        }
        final Expression instanceDeployUnit;
        if (ctx.type().duVersions() != null) {
            instanceDeployUnit = new ExpressionVisitor(context).visit(ctx.type().duVersions());
        } else {
            instanceDeployUnit = null;
        }
        if (ctx.varName != null) {
            final String instanceVarName = ctx.varName.getText();
            final FinalExpression instanceName;
            if (ctx.instanceName != null) {
                final Expression tmp = new ExpressionVisitor(context).visit(ctx.instanceName);
                final FinalExpression instanceName2 = this.context.lookup(tmp, FinalExpression.class);
                if(instanceName2 != null) {
                    if(instanceName2 instanceof StringExpression) { // || instanceName2 instanceof InstanceExpression
                        instanceName = instanceName2;
                    } else {
                        throw new WrongTypeException(instanceName2.toText(), FinalExpression.class);
                    }
                } else {
                    instanceName = null;
                }
            } else {
                instanceName = new StringExpression(instanceVarName);
            }
            final InstanceExpression addedInstance = new InstanceExpression(instanceName.toText(), instanceTypeDefName, instanceTypeDefVersion, instanceDeployUnit);
            this.context.addExpression(instanceVarName, addedInstance);
        } else {
            for (Basic_identifierContext varName : ctx.varIdentifierList().basic_identifier()) {
                final String instanceVarName = varName.getText();
                final FinalExpression instanceName = new StringExpression(varName.getText());
                this.context.addExpression(instanceVarName, new InstanceExpression(instanceName.toText(), instanceTypeDefName, instanceTypeDefVersion, instanceDeployUnit));
            }
        }
        return new Commands();
    }

    @Override
    public Commands visitAttach(final AttachContext ctx) {
        final InstanceElement group = this.helper.getInstanceFromContext(context, ctx.identifier());

        // node conversion from expression to command element
        final Commands ret = new Commands();
        for (final IdentifierContext node : ctx.nodesId.identifier()) {
            final InstanceElement nodeInstanceElement = this.helper.getInstanceFromContext(context, node);

            // command instanciation
            ret.addCommand(new AttachCommand(group, nodeInstanceElement));
        }
        return ret;
    }

    @Override
    public Commands visitDetach(DetachContext ctx) {
        final InstanceElement group = this.helper.getInstanceFromContext(context, ctx.identifier());

        // node conversion from expression to command element
        final Commands ret = new Commands();
        for (final IdentifierContext node : ctx.nodesId.identifier()) {
            final InstanceElement nodeInstanceElement = this.helper.getInstanceFromContext(context, node);

            // command instanciation
            ret.addCommand(new DetachCommand(group, nodeInstanceElement));
        }
        return ret;
    }


    @Override
    public Commands visitLetDecl(final LetDeclContext ctx) {
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        final FinalExpression res = expressionVisitor.visit(ctx.val);
        this.context.addExpression(ctx.basic_identifier().getText(), res);
        return expressionVisitor.aggregatedFunctionsCommands;
    }

    @Override
    public Commands visitBind(BindContext ctx) {
        final InstanceElement chan = this.helper.getInstanceFromContext(context, ctx.chan);

        final Commands ret = new Commands();
        for (final PortPathContext portPath : ctx.nodes.instances) {
            ret.addAll(visitBindNode(chan, portPath));
        }

        return ret;
    }

    private Commands visitBindNode(final InstanceElement chan, final PortPathContext portPath) {
        final Commands ret = new Commands();
        final InstancePathContext instancePathContext = portPath.instancePath();
        if (instancePathContext == null) {
            // instance path is empty so identifier must be a reference to an instance path declared previously.
            final Expression result = new ExpressionVisitor(context).visit(portPath.identifier());
            try {
                if (result instanceof PortPathExpression) {
                    final PortPathExpression instance = (PortPathExpression) result;
                    final String name = instance.portName;
                    if (instance.instancePath.node == null) {
                        final InstanceExpression componentExpression = context.lookup(instance.instancePath.component, InstanceExpression.class);
                        final String instanceName = componentExpression.instanceName;
                        final String instanceTypeDefName = componentExpression.instanceTypeDefName;
                        final Long version = helper.convertVersionToLong(this.context, componentExpression.instanceTypeDefVersion);
                        final InstanceElement component = new InstanceElement(instanceName, instanceTypeDefName, version);
                        final Boolean isInput = instance.isInput;
                        final PortElement portElement = new PortElement(name, null, component, isInput);
                        ret.addCommand(new BindCommand(chan, portElement));
                    } else {
                        final InstanceElement node = convertPortPathToNodeElement(instance.instancePath);
                        final InstanceElement component = convertPortPathToComponentElement(instance.instancePath);
                        final Boolean isInput = instance.isInput;
                        final PortElement portElement = new PortElement(name, node, component, isInput);
                        ret.addCommand(new BindCommand(chan, portElement));
                    }
                } else {
                    throw new PortPathNotFound(portPath.identifier().getText());
                }
            } catch (InstanceNameNotFound e) {
                throw new PortPathNotFound(result.toString());
            }
        } else {
            if (instancePathContext.identifier().size() == 1) {
                // direct reference to a component, must be in the current scope
                final InstanceElement componentInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(0));

                final Expression portNameIdentifier = new ExpressionVisitor(context).visit(portPath.identifier());
                final StringExpression portNameStr = this.context.lookup(portNameIdentifier, StringExpression.class);
                final PortElement port = new PortElement(portNameStr.toText(), null, componentInstance, portPath.LEFT_LIGHT_ARROW() != null);
                ret.addCommand(new BindCommand(chan, port));
            } else {
                // reference to a component via its node, might be found in the context or later in the CDN
                final InstanceElement nodeInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(0));
                final InstanceElement componentInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(1));
                final IdentifierContext identifier = portPath.identifier();
                final String portName = getPortPathFromIdentifier(identifier);
                final PortElement port = new PortElement(portName, nodeInstance, componentInstance, portPath.LEFT_LIGHT_ARROW() != null);
                ret.addCommand(new BindCommand(chan, port));
            }
        }
        return ret;
    }

    private InstanceElement convertPortPathToNodeElement(final InstancePathExpression instancePath) {
        final InstanceExpression nodeExpression = context.lookup(instancePath.node, InstanceExpression.class, false);
        final InstanceElement node;
        if (nodeExpression != null) {
            node = new InstanceElement(nodeExpression.instanceName, nodeExpression.instanceTypeDefName, helper.convertVersionToLong(this.context, nodeExpression.instanceTypeDefVersion));
        } else if(instancePath.node != null){
            node = new InstanceElement(this.context.lookup(instancePath.node, FinalExpression.class).toText());
        } else {
            node = null;
        }
        return node;
    }

    private String getPortPathFromIdentifier(IdentifierContext identifier) {
        final Expression portNameIdentifier = new ExpressionVisitor(context).visit(identifier);
        final String portNameFromIdentifier = this.helper.getPortNameFromIdentifier(context, portNameIdentifier);
        final String ret;
        if(portNameFromIdentifier == null) {
            ret = identifier.getText();
        } else {
            ret = portNameFromIdentifier;
        }
        return ret;
    }

    @Override
    public Commands visitUnbind(UnbindContext ctx) {
        final InstanceElement chan = this.helper.getInstanceFromContext(context, ctx.chan);

        final Commands ret = new Commands();
        for (final PortPathContext portPath : ctx.nodes.instances) {
            final InstancePathContext instancePathContext = portPath.instancePath();
            if (instancePathContext == null) {
                // instance path is empty so identifier must be a reference to an instance path declared previously.
                final Expression result = new ExpressionVisitor(context).visit(portPath.identifier());
                try {
                    final FinalExpression instanceB = this.context.lookup(result, FinalExpression.class);
                    if (instanceB instanceof PortPathExpression) {
                        final PortPathExpression instance = (PortPathExpression) instanceB;
                        final String name = instance.portName;
                        if (instance.instancePath.node == null) {
                            final InstanceExpression componentExpression = context.lookup(instance.instancePath.component, InstanceExpression.class);
                            final InstanceElement component = new InstanceElement(componentExpression.instanceName, componentExpression.instanceTypeDefName, helper.convertVersionToLong(this.context, componentExpression.instanceTypeDefVersion));
                            final Boolean isInput = instance.isInput;
                            final PortElement portElement = new PortElement(name, null, component, isInput);
                            ret.addCommand(new UnbindCommand(chan, portElement));
                        } else {
                            final InstanceElement node = convertPortPathToNodeElement(instance.instancePath);
                            final InstanceElement component = convertPortPathToComponentElement(instance.instancePath);
                            final Boolean isInput = instance.isInput;
                            final PortElement portElement = new PortElement(name, node, component, isInput);
                            ret.addCommand(new UnbindCommand(chan, portElement));
                        }
                    } else {
                        throw new PortPathNotFound(portPath.identifier().getText());
                    }
                } catch (InstanceNameNotFound e) {
                    throw new PortPathNotFound(result.toString());
                }
            } else {
                if (instancePathContext.identifier().size() == 1) {
                    // direct reference to a component, must be in the current scope
                    final InstanceElement componentInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(0));
                    final String portName = getPortPathFromIdentifier(portPath.identifier());
                    final PortElement port = new PortElement(portName, null, componentInstance, portPath.LEFT_LIGHT_ARROW() != null);
                    ret.addCommand(new UnbindCommand(chan, port));
                } else {
                    // reference to a component via its node, might be found in the context or later in the CDN
                    final InstanceElement nodeInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(0));
                    final InstanceElement componentInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(1));
                    final String portName = getPortPathFromIdentifier(portPath.identifier());
                    final PortElement port = new PortElement(portName, nodeInstance, componentInstance, portPath.LEFT_LIGHT_ARROW() != null);
                    ret.addCommand(new UnbindCommand(chan, port));
                }
            }
        }

        return ret;
    }

    private InstanceElement convertPortPathToComponentElement(final InstancePathExpression instancePath) {
        final InstanceExpression componentExpression = context.lookup(instancePath.component, InstanceExpression.class, false);
        final InstanceElement component;
        if (componentExpression != null) {
            component = new InstanceElement(componentExpression.instanceName, componentExpression.instanceTypeDefName, helper.convertVersionToLong(this.context, componentExpression.instanceTypeDefVersion));
        } else {
            component = new InstanceElement(this.context.lookup(instancePath.node, FinalExpression.class).toText());
        }
        return component;
    }

    @Override
    public Commands visitFuncDecl(final FuncDeclContext ctx) {

        if(ctx.NATIVE() == null) {
            final FunctionExpression functionExpression = new FunctionExpression();
            if(ctx.parameters != null) {
                for (final Basic_identifierContext param : ctx.parameters.basic_identifier()) {
                    functionExpression.addParam(param.getText());
                }
            }
            functionExpression.setFunctionBody(ctx.funcBody());
            this.context.addExpression(ctx.functionName.getText(), functionExpression);
        } else {
            // TODO native function
        }

        return new Commands();
    }

    @Override
    public Commands visitFuncCall(FuncCallContext ctx) {
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(this.context);
        final FunctionCallExpression res = expressionVisitor.visitFuncCall(ctx);
        return expressionVisitor.aggregatedFunctionsCommands;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public Commands visitSet(SetContext ctx) {
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        final DictionaryPathExpression instanceDicoRef = expressionVisitor.visitDictionaryPath(ctx.dictionaryPath());
        final InstanceElement node = convertPortPathToNodeElement(instanceDicoRef.instancePathExpression);
        final InstanceElement component = convertPortPathToComponentElement(instanceDicoRef.instancePathExpression);
        final FinalExpression value = expressionVisitor.visit(ctx.val);
        final DictionaryElement dictionaryElement = new DictionaryElement(instanceDicoRef.dicoName, instanceDicoRef.frag, node, component);
        final SetCommand setCommand = new SetCommand(dictionaryElement, value.toText());
        return new Commands().addCommand(setCommand);
    }
}
