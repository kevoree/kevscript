package org.kevoree.kevscript.language.visitors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.commands.element.DictionaryElement;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.commands.element.RootInstanceElement;
import org.kevoree.kevscript.language.commands.element.PortElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.InstanceNameNotFound;
import org.kevoree.kevscript.language.excpt.PortPathNotFound;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.*;
import org.kevoree.kevscript.language.visitors.helper.KevscriptHelper;

import java.util.List;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 01/03/16.
 */
public class KevscriptVisitor extends KevScriptBaseVisitor<Commands> {

    private final Context context;
    private final KevscriptHelper helper;

    public KevscriptVisitor() {
        this.context = new Context();
        this.helper = new KevscriptHelper(this.context);
    }

    public KevscriptVisitor(final Context context) {
        this.context = new Context(context);
        this.helper = new KevscriptHelper(this.context);
    }

    @Override
    public Commands visitScript(final ScriptContext ctx) {
        return loopOverChildren(ctx);
    }

    private Commands loopOverChildren(ParserRuleContext ctx) {
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
            final RootInstanceElement parent = this.helper.getInstanceFromIdentifierContext(ctx.identifier(0));
            if (ctx.identifier(1) != null) {
                final InstanceExpression child = context.lookup(new ExpressionVisitor(context).visitIdentifier(ctx.identifier(1)), InstanceExpression.class);
                final RootInstanceElement childElement = new RootInstanceElement(child.instanceName, child.instanceTypeDefName);
                ret.add(new AddCommand(new InstanceElement(parent, childElement)));
            } else if (ctx.identifierList() != null && ctx.identifierList().identifiers != null) {
                for (final IdentifierContext instance : ctx.identifierList().identifiers) {
                    // TODO complete me
                }
            } else {
                ret.add(new AddCommand(new InstanceElement(parent)));
            }

        } else if (ctx.LS_BRACKET() != null) {
            for (IdentifierContext identifier : ctx.identifierList().identifier()) {
                final InstanceExpression parent = context.lookup(new ExpressionVisitor(context).visitIdentifier(identifier), InstanceExpression.class);
                final String instanceName = parent.instanceName;
                final Long versionValue = helper.convertVersionToLong(parent.instanceTypeDefVersion);
                final RootInstanceElement root = new RootInstanceElement(instanceName, parent.instanceTypeDefName, versionValue);
                ret.add(new AddCommand(new InstanceElement(root)));
            }
        } else {

            for(final InstancePathContext instancePathContext : ctx.instanceList().instances) {
                //final InstanceElement chan = this.helper.getInstanceFromIdentifierContext(in);
                final InstanceElement instance;
                if (instancePathContext.identifier().size() == 1) {
                    // direct reference to a component, must be in the current scope
                    final RootInstanceElement componentInstance = this.helper.getInstanceFromIdentifierContext(instancePathContext.identifier(0));
                    instance = new InstanceElement(componentInstance);
                } else {
                    // reference to a component via its node, might be found in the context or later in the CDN
                    final RootInstanceElement nodeInstance = this.helper.getInstanceFromIdentifierContext(instancePathContext.identifier(0));
                    final RootInstanceElement componentInstance = this.helper.getInstanceFromIdentifierContext(instancePathContext.identifier(1));
                    instance = new InstanceElement(nodeInstance, componentInstance);
                }
                ret.addCommand(new AddCommand(instance));
            }
        }
        return ret;
    }

    @Override
    public Commands visitRemove(final RemoveContext ctx) {
        final Commands commands = new Commands();
        final List<InstancePathContext> instancePathContextList = ctx.instanceList().instances;
        for(final InstancePathContext instancePathContext : instancePathContextList) {
            //final InstanceElement chan = this.helper.getInstanceFromIdentifierContext(in);
            final InstanceElement instance;
            if (instancePathContext.identifier().size() == 1) {
                // direct reference to a component, must be in the current scope
                final RootInstanceElement componentInstance = this.helper.getInstanceFromIdentifierContext(instancePathContext.identifier(0));
                //final PortElement port = new PortElement(portNameStr.toText(), null, componentInstance, portPath.LEFT_LIGHT_ARROW() != null);
                instance = new InstanceElement(componentInstance);

            } else {
                // reference to a component via its node, might be found in the context or later in the CDN
                final RootInstanceElement nodeInstance = this.helper.getInstanceFromIdentifierContext(instancePathContext.identifier(0));
                final RootInstanceElement componentInstance = this.helper.getInstanceFromIdentifierContext(instancePathContext.identifier(1));
                instance = new InstanceElement(nodeInstance, componentInstance);
            }
            commands.addCommand(new RemoveCommand(instance));
        }
        return commands;
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
                if (instanceName2 != null) {
                    if (instanceName2 instanceof StringExpression) { // || instanceName2 instanceof InstanceExpression
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
        final RootInstanceElement group = this.helper.getInstanceFromIdentifierContext(ctx.identifier());

        // node conversion from expression to command element
        final Commands ret = new Commands();
        for (final IdentifierContext node : ctx.nodesId.identifier()) {
            final RootInstanceElement nodeRootInstanceElement = this.helper.getInstanceFromIdentifierContext(node);

            // command instanciation
            ret.addCommand(new AttachCommand(group, nodeRootInstanceElement));
        }
        return ret;
    }

    @Override
    public Commands visitDetach(DetachContext ctx) {
        final RootInstanceElement group = this.helper.getInstanceFromIdentifierContext(ctx.identifier());

        // node conversion from expression to command element
        final Commands ret = new Commands();
        for (final IdentifierContext node : ctx.nodesId.identifier()) {
            final RootInstanceElement nodeRootInstanceElement = this.helper.getInstanceFromIdentifierContext(node);
            // command instanciation
            ret.addCommand(new DetachCommand(group, nodeRootInstanceElement));
        }
        return ret;
    }

    @Override
    public Commands visitMove(MoveContext ctx) {
        final Commands commands = new Commands();
        if(ctx.identifier() != null) {
            // case with identifiers and possible array
        } else {
            // case with two direct references
            final List<RootInstanceElement> targetInstance = this.helper.getInstancesFromInstancePathContext(ctx.instancePath(0));
            if(targetInstance.size() == 1) {

            }

            /*final MoveCommand e = new MoveCommand(nodeTarget, componentTarget, );*/
            /*commands.add(e);*/
        }
        return commands;
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
        final RootInstanceElement chan = this.helper.getInstanceFromIdentifierContext(ctx.chan);

        final Commands ret = new Commands();
        for (final PortPathContext portPath : ctx.nodes.instances) {
            ret.addAll(visitBindNode(chan, portPath));
        }

        return ret;
    }

    private Commands visitBindNode(final RootInstanceElement chan, final PortPathContext portPath) {
        final Commands ret;
        final InstancePathContext instancePathContext = portPath.instancePath();
        if (instancePathContext == null) {
            // instance path is empty so identifier must be a reference to an instance path declared previously.
            ret = visitBindNodeInstanceReference(chan, portPath);
        } else {
            ret = visitBindNodeInstancePath(chan, portPath, instancePathContext);
        }
        return ret;
    }

    private Commands visitBindNodeInstancePath(final RootInstanceElement chan, final PortPathContext portPath, final InstancePathContext instancePathContext) {
        final IdentifierContext identifier = portPath.identifier();
        final String portName = helper.getPortPathFromIdentifier(identifier);
        final boolean isInput = portPath.LEFT_LIGHT_ARROW() != null;
        final InstanceElement instance = this.helper.getInstanceElement(instancePathContext);
        final PortElement port = new PortElement(portName, instance, isInput);
        return new Commands().addCommand(new BindCommand(chan, port));
    }



    private Commands visitBindNodeInstanceReference(RootInstanceElement chan, PortPathContext portPath) {
        final Commands ret = new Commands();
        final Expression result = new ExpressionVisitor(context).visit(portPath.identifier());
        try {
            if (result instanceof PortPathExpression) {
                final PortPathExpression instance = (PortPathExpression) result;
                final String name = instance.portName;
                final Boolean isInput = instance.isInput;
                final InstanceElement instance1;
                if (instance.instancePath.node == null) {
                    final InstanceExpression componentExpression = context.lookup(instance.instancePath.component, InstanceExpression.class);
                    final String instanceName = componentExpression.instanceName;
                    final String instanceTypeDefName = componentExpression.instanceTypeDefName;
                    final Long version = helper.convertVersionToLong(componentExpression.instanceTypeDefVersion);
                    final RootInstanceElement component = new RootInstanceElement(instanceName, instanceTypeDefName, version);
                    instance1 = new InstanceElement(component);
                } else {
                    final RootInstanceElement node = helper.convertPortPathToNodeElement(instance.instancePath);
                    final RootInstanceElement component = helper.convertPortPathToComponentElement(instance.instancePath);
                    instance1 = new InstanceElement(node, component);
                }
                final PortElement portElement = new PortElement(name, instance1, isInput);
                ret.addCommand(new BindCommand(chan, portElement));
            } else {
                throw new PortPathNotFound(portPath.identifier().getText());
            }
        } catch (InstanceNameNotFound e) {
            throw new PortPathNotFound(result.toString());
        }
        return ret;
    }

    @Override
    public Commands visitUnbind(UnbindContext ctx) {
        final RootInstanceElement chan = this.helper.getInstanceFromIdentifierContext(ctx.chan);

        final Commands ret = new Commands();
        for (final PortPathContext portPath : ctx.nodes.instances) {
            final InstancePathContext instancePathContext = portPath.instancePath();
            if (instancePathContext == null) {
                // instance path is empty so identifier must be a reference to an instance path declared previously.
                ret.addAll(visitUnbindInstanceRef(chan, portPath));
            } else {
                ret .addAll(visitUnbindInstancePath(chan, portPath, instancePathContext));
            }
        }

        return ret;
    }

    private Commands visitUnbindInstancePath(final RootInstanceElement chan, final PortPathContext portPath, final InstancePathContext instancePathContext) {
        final boolean isInput = portPath.LEFT_LIGHT_ARROW() != null;
        final String portName = helper.getPortPathFromIdentifier(portPath.identifier());
        final InstanceElement instanceElement = this.helper.getInstanceElement(instancePathContext);
        final PortElement port = new PortElement(portName, instanceElement, isInput);
        return new Commands().addCommand(new UnbindCommand(chan, port));
    }

    private Commands visitUnbindInstanceRef(RootInstanceElement chan, PortPathContext portPath) {
        final Commands ret = new Commands();
        final Expression result = new ExpressionVisitor(context).visit(portPath.identifier());
        try {
            final FinalExpression instanceB = this.context.lookup(result, FinalExpression.class);
            if (instanceB instanceof PortPathExpression) {
                final PortPathExpression instance = (PortPathExpression) instanceB;
                final String name = instance.portName;
                if (instance.instancePath.node == null) {
                    final InstanceExpression componentExpression = context.lookup(instance.instancePath.component, InstanceExpression.class);
                    final RootInstanceElement component = new RootInstanceElement(componentExpression.instanceName, componentExpression.instanceTypeDefName, helper.convertVersionToLong(componentExpression.instanceTypeDefVersion));
                    final Boolean isInput = instance.isInput;
                    final PortElement portElement = new PortElement(name, new InstanceElement(component), isInput);
                    ret.addCommand(new UnbindCommand(chan, portElement));
                } else {
                    final RootInstanceElement node = helper.convertPortPathToNodeElement(instance.instancePath);
                    final RootInstanceElement component = helper.convertPortPathToComponentElement(instance.instancePath);
                    final Boolean isInput = instance.isInput;
                    final PortElement portElement = new PortElement(name, new InstanceElement(node, component), isInput);
                    ret.addCommand(new UnbindCommand(chan, portElement));
                }
            } else {
                throw new PortPathNotFound(portPath.identifier().getText());
            }
        } catch (InstanceNameNotFound e) {
            throw new PortPathNotFound(result.toString());
        }
        return ret;
    }


    @Override
    public Commands visitFuncDecl(final FuncDeclContext ctx) {

        if (ctx.NATIVE() == null) {
            final FunctionExpression functionExpression = new FunctionExpression();
            if (ctx.parameters != null) {
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
        expressionVisitor.visitFuncCall(ctx);
        return expressionVisitor.aggregatedFunctionsCommands;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public Commands visitSet(SetContext ctx) {
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        final DictionaryPathExpression instanceDicoRef = expressionVisitor.visitDictionaryPath(ctx.dictionaryPath());
        final RootInstanceElement node = helper.convertPortPathToNodeElement(instanceDicoRef.instancePathExpression);
        final RootInstanceElement component = helper.convertPortPathToComponentElement(instanceDicoRef.instancePathExpression);
        final FinalExpression value = expressionVisitor.visit(ctx.val);
        final DictionaryElement dictionaryElement = new DictionaryElement(instanceDicoRef.dicoName, instanceDicoRef.frag, new InstanceElement(node, component));
        final SetCommand setCommand = new SetCommand(dictionaryElement, value.toText());
        return new Commands().addCommand(setCommand);
    }

    @Override
    public Commands visitForDecl(ForDeclContext ctx) {
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        final ArrayDeclExpression iterable = expressionVisitor.visitIterable(ctx.iterable());
        final Commands forCommands = new Commands();

        int i = 0;
        for (FinalExpression expression : iterable.expressionList) {
            final Context localForContext = new Context(context);
            if (ctx.index != null) {
                localForContext.addExpression(ctx.index.getText(), new StringExpression(String.valueOf(i++)));
            }

            localForContext.addExpression(ctx.val.getText(), expression);
            forCommands.addAll(new KevscriptVisitor(localForContext).visit(ctx.forBody()));
        }

        return forCommands;
    }

    @Override
    public Commands visitForBody(final ForBodyContext ctx) {
        return loopOverChildren(ctx);
    }


}
