package org.kevoree.kevscript.language.visitors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.commands.element.PortElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.InstanceNameNotFound;
import org.kevoree.kevscript.language.excpt.PortPathNotFound;
import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.InstanceExpression;
import org.kevoree.kevscript.language.expressions.PortPathExpression;
import org.kevoree.kevscript.language.expressions.StringExpression;
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
            final InstanceExpression parent = context.lookup(new ExpressionVisitor(context).visitIdentifier(ctx.identifier(0)).toText(), InstanceExpression.class);
            if (ctx.identifier(1) != null) {
                final InstanceExpression child = context.lookup(new ExpressionVisitor(context).visitIdentifier(ctx.identifier(1)).toText(), InstanceExpression.class);
                final StringExpression parentInstanceName = context.lookup(parent.instanceName.toText(), StringExpression.class);
                final StringExpression childInstanceName = context.lookup(child.instanceName.toText(), StringExpression.class);
                final InstanceElement parentElement = new InstanceElement(parentInstanceName.toText(), parent.instanceTypeDefName);
                final InstanceElement childElement = new InstanceElement(childInstanceName.toText(), child.instanceTypeDefName);
                ret.add(new AddCommand(parentElement, childElement));
            } else if (ctx.identifierList() != null && ctx.identifierList().identifiers != null) {
                for (final IdentifierContext instance : ctx.identifierList().identifiers) {
                    //context.loo
                    // TODO complete me
                }
            } else {
//                final StringExpression instanceName = context.lookup(parent.instanceName.toText(), StringExpression.class);
                final Long versionValue = helper.convertVersionToLong(this.context, parent.instanceTypeDefVersion);
                final InstanceElement root = new InstanceElement(parent.toText(), parent.instanceTypeDefName, versionValue);
                ret.add(new AddCommand(root));
            }

        } else if (ctx.LS_BRACKET() != null) {
            for (IdentifierContext identifier : ctx.identifierList().identifier()) {
                final InstanceExpression parent = context.lookup(new ExpressionVisitor(context).visitIdentifier(identifier).toText(), InstanceExpression.class);
                final String instanceName = parent.instanceName.toText();
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
        final Expression instanceTypeDefVersion;
        if (ctx.type().version() != null) {
            instanceTypeDefVersion = new ExpressionVisitor(context).visit(ctx.type().version());
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
            final Expression instanceName;
            if (ctx.instanceName != null) {
                instanceName = new ExpressionVisitor(context).visit(ctx.instanceName);
            } else {
                instanceName = new StringExpression(instanceVarName);
            }
            this.context.addExpression(instanceVarName, new InstanceExpression(instanceName, instanceTypeDefName, instanceTypeDefVersion, instanceDeployUnit));
        } else {
            for (Basic_identifierContext varName : ctx.varIdentifierList().basic_identifier()) {
                final String instanceVarName = varName.getText();
                final Expression instanceName = new StringExpression(varName.getText());
                this.context.addExpression(instanceVarName, new InstanceExpression(instanceName, instanceTypeDefName, instanceTypeDefVersion, instanceDeployUnit));
            }
        }
        return super.visitInstance(ctx);
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
        final Expression res = new ExpressionVisitor(context).visit(ctx.val);
        this.context.addExpression(ctx.basic_identifier().getText(), res);
        return super.visitLetDecl(ctx);
    }

    @Override
    public Commands visitBind(BindContext ctx) {
        final InstanceElement chan = this.helper.getInstanceFromContext(context, ctx.chan);

        final Commands ret = new Commands();
        for (final PortPathContext portPath : ctx.nodes.instances) {
            final InstancePathContext instancePathContext = portPath.instancePath();
            if (instancePathContext == null) {
                // instance path is empty so identifier must be a reference to an instance path declared previously.
                final Expression result = new ExpressionVisitor(context).visit(portPath.identifier());
                try {
                    final Expression instanceB = this.context.lookup(result.toText(), Expression.class);
                    if (instanceB instanceof PortPathExpression) {
                        final PortPathExpression instance = (PortPathExpression) instanceB;
                        final String name = instance.portName.toText();
                        if (instance.instancePath.node == null) {
                            final InstanceExpression componentExpression = context.lookup(instance.instancePath.component.toText(), InstanceExpression.class);
                            final InstanceElement component = new InstanceElement(componentExpression.instanceName.toText(), componentExpression.instanceTypeDefName, helper.convertVersionToLong(this.context, componentExpression.instanceTypeDefVersion));
                            final Boolean isInput = instance.isInput;
                            final PortElement portElement = new PortElement(name, null, component, isInput);
                            ret.addCommand(new BindCommand(chan, portElement));
                        } else {
                            final InstanceExpression nodeExpression = context.lookup(instance.instancePath.node.toText(), InstanceExpression.class, false);
                            final InstanceElement node;
                            if (nodeExpression != null) {
                                node = new InstanceElement(nodeExpression.instanceName.toText(), nodeExpression.instanceTypeDefName, helper.convertVersionToLong(this.context, nodeExpression.instanceTypeDefVersion));
                            } else {
                                node = new InstanceElement(instance.instancePath.node.toText());
                            }
                            final InstanceExpression componentExpression = context.lookup(instance.instancePath.component.toText(), InstanceExpression.class, false);
                            final InstanceElement component;
                            if (componentExpression != null) {
                                component = new InstanceElement(componentExpression.instanceName.toText(), componentExpression.instanceTypeDefName, helper.convertVersionToLong(this.context, componentExpression.instanceTypeDefVersion));
                            } else {
                                component = new InstanceElement(instance.instancePath.node.toText());
                            }
                            final Boolean isInput = instance.isInput;
                            final PortElement portElement = new PortElement(name, node, component, isInput);
                            ret.addCommand(new BindCommand(chan, portElement));
                        }
                    } else {
                        throw new PortPathNotFound(instanceB);
                    }
                } catch (InstanceNameNotFound e) {
                    throw new PortPathNotFound(result);
                }
            } else {
                if (instancePathContext.identifier().size() == 1) {
                    // direct reference to a component, must be in the current scope
                    final InstanceElement componentInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(0));

                    final Expression portNameIdentifier = new ExpressionVisitor(context).visit(portPath.identifier());
                    final StringExpression portNameStr = this.context.lookup(portNameIdentifier.toText(), StringExpression.class);
                    final PortElement port = new PortElement(portNameStr.toText(), null, componentInstance, portPath.LEFT_LIGHT_ARROW() != null);
                    ret.addCommand(new BindCommand(chan, port));
                } else {
                    // reference to a component via its node, might be found in the context or later in the CDN
                    final InstanceElement nodeInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(0));
                    final InstanceElement componentInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(1));
                    final Expression portNameIdentifier = new ExpressionVisitor(context).visit(portPath.identifier());
                    final String portName = this.helper.getPortNameFromIdentifier(context, portNameIdentifier);
                    final PortElement port = new PortElement(portName, nodeInstance, componentInstance, portPath.LEFT_LIGHT_ARROW() != null);
                    ret.addCommand(new BindCommand(chan, port));
                }
            }
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
                    final Expression instanceB = this.context.lookup(result.toText(), Expression.class);
                    if (instanceB instanceof PortPathExpression) {
                        final PortPathExpression instance = (PortPathExpression) instanceB;
                        final String name = instance.portName.toText();
                        if (instance.instancePath.node == null) {
                            final InstanceExpression componentExpression = context.lookup(instance.instancePath.component.toText(), InstanceExpression.class);
                            final InstanceElement component = new InstanceElement(componentExpression.instanceName.toText(), componentExpression.instanceTypeDefName, helper.convertVersionToLong(this.context, componentExpression.instanceTypeDefVersion));
                            final Boolean isInput = instance.isInput;
                            final PortElement portElement = new PortElement(name, null, component, isInput);
                            ret.addCommand(new UnbindCommand(chan, portElement));
                        } else {
                            final InstanceExpression nodeExpression = context.lookup(instance.instancePath.node.toText(), InstanceExpression.class, false);
                            final InstanceElement node;
                            if (nodeExpression != null) {
                                node = new InstanceElement(nodeExpression.instanceName.toText(), nodeExpression.instanceTypeDefName, helper.convertVersionToLong(this.context, nodeExpression.instanceTypeDefVersion));
                            } else {
                                node = new InstanceElement(instance.instancePath.node.toText());
                            }
                            final InstanceExpression componentExpression = context.lookup(instance.instancePath.component.toText(), InstanceExpression.class, false);
                            final InstanceElement component;
                            if (componentExpression != null) {
                                component = new InstanceElement(componentExpression.instanceName.toText(), componentExpression.instanceTypeDefName, helper.convertVersionToLong(this.context, componentExpression.instanceTypeDefVersion));
                            } else {
                                component = new InstanceElement(instance.instancePath.node.toText());
                            }
                            final Boolean isInput = instance.isInput;
                            final PortElement portElement = new PortElement(name, node, component, isInput);
                            ret.addCommand(new UnbindCommand(chan, portElement));
                        }
                    } else {
                        throw new PortPathNotFound(instanceB);
                    }
                } catch (InstanceNameNotFound e) {
                    throw new PortPathNotFound(result);
                }
            } else {
                if (instancePathContext.identifier().size() == 1) {
                    // direct reference to a component, must be in the current scope
                    final InstanceElement componentInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(0));
                    final Expression portNameIdentifier = new ExpressionVisitor(context).visit(portPath.identifier());
                    final String portName = this.helper.getPortNameFromIdentifier(context, portNameIdentifier);
                    final PortElement port = new PortElement(portName, null, componentInstance, portPath.LEFT_LIGHT_ARROW() != null);
                    ret.addCommand(new UnbindCommand(chan, port));
                } else {
                    // reference to a component via its node, might be found in the context or later in the CDN
                    final InstanceElement nodeInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(0));
                    final InstanceElement componentInstance = this.helper.getInstanceFromContext(context, instancePathContext.identifier(1));
                    final Expression portNameIdentifier = new ExpressionVisitor(context).visit(portPath.identifier());
                    final String portName = this.helper.getPortNameFromIdentifier(context, portNameIdentifier);
                    final PortElement port = new PortElement(portName, nodeInstance, componentInstance, portPath.LEFT_LIGHT_ARROW() != null);
                    ret.addCommand(new UnbindCommand(chan, port));
                }
            }
        }

        return ret;
    }


}
