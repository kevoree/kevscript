package org.kevoree.kevscript.language.visitors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.commands.element.DictionaryElement;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.commands.element.PortElement;
import org.kevoree.kevscript.language.commands.element.RootInstanceElement;
import org.kevoree.kevscript.language.commands.element.object.ObjectElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.excpt.ImportException;
import org.kevoree.kevscript.language.excpt.InstanceNameNotFound;
import org.kevoree.kevscript.language.excpt.PortPathNotFound;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.finalexp.*;
import org.kevoree.kevscript.language.expressions.finalexp.function.FunctionExpression;
import org.kevoree.kevscript.language.expressions.finalexp.function.FunctionNativeExpression;
import org.kevoree.kevscript.language.visitors.helper.KevscriptHelper;

import java.util.List;
import java.util.Map;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 01/03/16.
 */
public class KevscriptVisitor extends KevScriptBaseVisitor<Commands> {

    private final Context context;
    private final KevscriptHelper helper;
    private final ImportsStore importsStore;

    public KevscriptVisitor(final String basePath) {
        this.context = new RootContext(basePath);
        this.helper = new KevscriptHelper(this.context);
        this.importsStore = new ImportsStore();
    }

    public KevscriptVisitor(final Context context) {
        this.context = new Context(context);
        this.helper = new KevscriptHelper(this.context);
        this.importsStore = new ImportsStore();
    }

    public KevscriptVisitor(final ImportsStore importsStore, final String basePath) {
        this.context = new RootContext(basePath);
        this.helper = new KevscriptHelper(this.context);
        this.importsStore = importsStore;
    }

    public KevscriptVisitor(final Context context, final ImportsStore importsStore) {
        this.context = context;
        this.helper = new KevscriptHelper(this.context);
        this.importsStore = importsStore;
    }

    @Override
    public Commands visitScript(final ScriptContext ctx) {
        return loopOverChildren(ctx);
    }

    private Commands loopOverChildren(ParserRuleContext ctx) {
        final Commands c = new Commands();
        if (ctx.children != null) {
            for (ParseTree child : ctx.children) {
                final Commands visit = this.visit(child);
                if (visit != null) {
                    c.addAll(visit);
                }
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
                    final RootInstanceElement nodeRootInstanceElement = this.helper.getInstanceFromIdentifierContext(instance);
                    ret.addCommand(new AddCommand(new InstanceElement(nodeRootInstanceElement)));
                }
            } else {
                ret.add(new AddCommand(new InstanceElement(parent)));
            }

        } else if (ctx.LS_BRACKET() != null) {
            for (IdentifierContext identifier : ctx.identifierList().identifier()) {
                final RootInstanceElement root = helper.identifierContextToRootInstance(identifier);
                ret.add(new AddCommand(new InstanceElement(root)));
            }
        } else {
            for (final InstancePathContext instancePathContext : ctx.instanceList().instances) {
                ret.addCommand(new AddCommand(helper.getInstanceElement(instancePathContext)));
            }
        }
        return ret;
    }


    @Override
    public Commands visitRemove(final RemoveContext ctx) {
        final Commands commands = new Commands();
        final List<InstancePathContext> instancePathContextList = ctx.instanceList().instances;
        for (final InstancePathContext instancePathContext : instancePathContextList) {
            commands.addCommand(new RemoveCommand(helper.getInstanceElement(instancePathContext)));
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
                final Expression tmp = new ExpressionVisitor(context).visitExpression(ctx.instanceName);
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
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        if (!ctx.identifier().isEmpty()) {
            // case with identifiers and possible array
            final RootInstanceElement target = helper.identifierContextToRootInstance(ctx.identifier(0));
            final InstanceElement targetInstance = new InstanceElement(target);
            if (ctx.identifierList() != null) {
                for (IdentifierContext a : ctx.identifierList().identifiers) {
                    final RootInstanceElement source = helper.identifierContextToRootInstance(a);
                    commands.addCommand(new MoveCommand(targetInstance, new InstanceElement(source)));
                }
            } else if (ctx.identifier().size() == 2) {
                final RootInstanceElement source = helper.identifierContextToRootInstance(ctx.identifier(1));
                commands.addCommand(new MoveCommand(targetInstance, new InstanceElement(source)));
            } else {
                for (InstancePathContext instancePath : ctx.instanceList().instances) {
                    final InstanceElement instance1 = helper.instancePathToInstanceElement(expressionVisitor.visitInstancePath(instancePath));
                    commands.addCommand(new MoveCommand(targetInstance, instance1));
                }
            }
        } else {
            // case with two direct references
            final InstanceElement targetInstance = this.helper.getInstanceElement(ctx.instancePath(0));
            final InstanceElement sourceInstance = this.helper.getInstanceElement(ctx.instancePath(1));
            final MoveCommand e = new MoveCommand(targetInstance, sourceInstance);
            commands.add(e);
        }
        return commands;
    }

    @Override
    public Commands visitStart(StartContext ctx) {
        final Commands commands = new Commands();
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        for (InstancePathContext instancePath : ctx.instanceList().instances) {
            final InstanceElement instance = helper.instancePathToInstanceElement(expressionVisitor.visitInstancePath(instancePath));
            commands.addCommand(new StartCommand(instance));
        }

        return commands;
    }

    @Override
    public Commands visitStop(StopContext ctx) {
        final Commands commands = new Commands();
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        for (InstancePathContext instancePath : ctx.instanceList().instances) {
            final InstanceElement instance = helper.instancePathToInstanceElement(expressionVisitor.visitInstancePath(instancePath));
            commands.addCommand(new StopCommand(instance));
        }

        return commands;
    }

    @Override
    public Commands visitLetDecl(final LetDeclContext ctx) {
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        final FinalExpression res = expressionVisitor.visit(ctx.val);
        res.setExported(ctx.EXPORT() != null);
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
                final InstanceElement instance1 = helper.instancePathToInstanceElement(instance.instancePath);
                final PortElement portElement = new PortElement(instance.portName, instance1, instance.isInput);
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
                ret.addAll(visitUnbindInstancePath(chan, portPath, instancePathContext));
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
                final InstanceElement instance1 = helper.instancePathToInstanceElement(instance.instancePath);
                final PortElement portElement = new PortElement(instance.portName, instance1, instance.isInput);
                ret.addCommand(new UnbindCommand(chan, portElement));
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

        final boolean exported = ctx.EXPORT() != null;
        if (ctx.NATIVE() == null) {
            final FunctionExpression functionExpression = new FunctionExpression();
            if (ctx.parameters != null) {
                for (final Basic_identifierContext param : ctx.parameters.basic_identifier()) {
                    functionExpression.addParam(param.getText());
                }
            }
            functionExpression.setFunctionBody(ctx.funcBody());
            functionExpression.setExported(exported);
            this.context.addExpression(ctx.functionName.getText(), functionExpression);
        } else {
            final FunctionNativeExpression functionNativeExpression = new FunctionNativeExpression();
            if (ctx.parameters != null) {
                for (final Basic_identifierContext param : ctx.parameters.basic_identifier()) {
                    functionNativeExpression.addParam(param.getText());
                }
            }

            final String text = ctx.SOURCE_CODE().getText();
            functionNativeExpression.setFunctionBody(text.substring(4, text.length() - 4));
            functionNativeExpression.setExported(exported);
            this.context.addExpression(ctx.functionName.getText(), functionNativeExpression);
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
        final FinalExpression value = expressionVisitor.visitExpression(ctx.val);
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

    @Override
    public Commands visitNetinit(final NetinitContext ctx) {
        final RootInstanceElement node = this.helper.getInstanceFromIdentifierContext(ctx.identifier(0));
        final ObjectElement network = helper.getObjectByDeclOrIdentifier(ctx.identifier(1), ctx.objectDecl());
        return new Commands().addCommand(new NetInitCommand(node, network));
    }

    @Override
    public Commands visitNetmerge(NetmergeContext ctx) {
        final RootInstanceElement node = this.helper.getInstanceFromIdentifierContext(ctx.identifier(0));
        final ObjectElement network = helper.getObjectByDeclOrIdentifier(ctx.identifier(1), ctx.objectDecl());
        return new Commands().addCommand(new NetMergeCommand(node, network));
    }

    @Override
    public Commands visitNetremove(final NetremoveContext ctx) {
        final RootInstanceElement node = this.helper.getInstanceFromIdentifierContext(ctx.identifier(0));
        final List<String> objectRefs = helper.getListObjectRefs(ctx.identifierList(), ctx.identifier(1));
        return new Commands().addCommand(new NetRemoveCommand(node, objectRefs));
    }


    @Override
    public Commands visitMetainit(final MetainitContext ctx) {
        final RootInstanceElement instance = this.helper.getInstanceFromIdentifierContext(ctx.identifier(0));
        final ObjectElement metas = helper.getObjectByDeclOrIdentifier(ctx.identifier(1), ctx.objectDecl());
        return new Commands().addCommand(new MetaInitCommand(instance, metas));
    }

    @Override
    public Commands visitMetamerge(final MetamergeContext ctx) {
        final RootInstanceElement instance = this.helper.getInstanceFromIdentifierContext(ctx.identifier(0));
        final ObjectElement metas = helper.getObjectByDeclOrIdentifier(ctx.identifier(1), ctx.objectDecl());
        return new Commands().addCommand(new MetaMergeCommand(instance, metas));
    }

    @Override
    public Commands visitMetaremove(final MetaremoveContext ctx) {
        final RootInstanceElement instance = this.helper.getInstanceFromIdentifierContext(ctx.identifier(0));
        final List<String> objectRefs = helper.getListObjectRefs(ctx.identifierList(), ctx.identifier(1));
        return new Commands().addCommand(new MetaRemoveCommand(instance, objectRefs));
    }

    @Override
    public Commands visitImportDecl(final ImportDeclContext ctx) {
        final String text = ctx.resource.getText();
        final Context importedContext = helper.loadContext(text, importsStore);

        final String qualifier;
        if (ctx.AS() != null) {
            qualifier = ctx.basic_identifier().getText() + ".";
        } else {
            qualifier = "";
        }

        // look for required components in the parsed script.
        final Map<String, FinalExpression> inheritedContext = importedContext.getInheritedContext();
        if (ctx.qualifiers == null) {
            // import everything
            for (Map.Entry<String, FinalExpression> entry : inheritedContext.entrySet()) {
                final FinalExpression expression = entry.getValue();
                if (expression.isExported()) {
                    this.context.addExpression(qualifier + entry.getKey(), expression);
                }
            }
        } else {
            // import only required elements
            for (final Basic_identifierContext a : ctx.qualifiers.basic_identifier()) {
                final String key = a.getText();
                if (inheritedContext.containsKey(key)) {
                    final FinalExpression expression = inheritedContext.get(key);
                    if (expression.isExported()) {
                        this.context.addExpression(qualifier + key, expression);
                    } else {
                        throw new ImportException(key, ctx.resource.getText());
                    }
                } else {
                    throw new ImportException(key, ctx.resource.getText());
                }
            }
        }

        return new Commands();
    }


}
