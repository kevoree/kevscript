package org.kevoree.kevscript.language.visitors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.commands.*;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.excpt.ImportException;
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
        final Commands cmds = new Commands();
        final ExpressionVisitor exprVisitor = new ExpressionVisitor(this.context);
        // target defaults to model root represented as '/'
        final InstanceExpression target;

        if (ctx.target != null) {
            // a specific target as been given
            final InstanceExpression targetExpr = exprVisitor.visitInstancePath(ctx.target);
            if (targetExpr == null) {
                // unable to find a reference for this identifier => use it literally
                target = new InstanceExpression(ctx.target.getText(), null);
            } else {
                if (targetExpr.instanceName.contains(":")) {
                    // TODO create a dedicated exception
                    throw new IllegalArgumentException("Cannot add instances to component " + targetExpr.toText());
                } else {
                    target = new InstanceExpression(targetExpr.toText(), null);
                }
            }
            for (InstancePathContext sourcesCtx : ctx.sources.instancePath()) {
                InstanceExpression sourceExpr = exprVisitor.visitInstancePath(sourcesCtx);
                cmds.addCommand(new AddCommand(target, new InstanceExpression(sourceExpr.toText(), null)));
            }
        } else {
            target = new InstanceExpression("/", null);
            for (InstancePathContext sourcesCtx : ctx.sources.instancePath()) {
                //final InstanceExpression sourceExpr = exprVisitor.visitInstancePath(sourcesCtx);
                final InstanceExpression sourceExpr = helper.getInstanceElement(sourcesCtx);
                cmds.addCommand(new AddCommand(target, new InstanceExpression(sourceExpr.toText(), null)));
            }
        }

        return cmds;
    }


    @Override
    public Commands visitRemove(final RemoveContext ctx) {
        final Commands commands = new Commands();
        final List<InstancePathContext> instancePathContextList = ctx.instanceList().instances;
        for (final InstancePathContext iPath : instancePathContextList) {
            final InstanceExpression instanceExpr = new ExpressionVisitor(this.context).visitInstancePath(iPath);
            commands.addCommand(new RemoveCommand(new InstanceExpression(instanceExpr.instanceName, null)));
        }
        return commands;
    }


    @Override
    public Commands visitInstance(final InstanceContext ctx) {
        final Commands cmds = new Commands();
        final TypeExpression typeExpr = new ExpressionVisitor(context).visitType(ctx.type());
        if (ctx.varName != null) {
            // only one instance creation
            final String instanceVarName = ctx.varName.getText();
            if (ctx.instanceName != null) {
                // instance creation using an expression for the name
                final FinalExpression nameExpr = new ExpressionVisitor(context).visitExpression(ctx.instanceName);
                final String instanceName = nameExpr.toText();
                final InstanceExpression instanceExpr = new InstanceExpression(instanceName, typeExpr);
                this.context.addExpression(instanceVarName, instanceExpr);
                cmds.addCommand(new InstanceCommand(instanceName, typeExpr));
            } else {
                // instance creation using the identifier name for the name
                final InstanceExpression instanceExpr = new InstanceExpression(instanceVarName, typeExpr);
                this.context.addExpression(instanceExpr.instanceName, instanceExpr);
                cmds.addCommand(new InstanceCommand(instanceVarName, typeExpr));
            }
        } else {
            // instance creation using the identifier name(s) for the name
            for (BasicIdentifierContext id : ctx.varIdentifierList().basicIdentifier()) {
                final InstanceExpression instanceExpr;
                instanceExpr = new InstanceExpression(id.getText(), typeExpr);
                this.context.addExpression(instanceExpr.instanceName, instanceExpr);
                cmds.addCommand(new InstanceCommand(id.getText(), typeExpr));
            }
        }

        return cmds;
    }

    @Override
    public Commands visitAttach(final AttachContext ctx) {
        final ExpressionVisitor exprVisitor = new ExpressionVisitor(this.context);
        InstanceExpression group;
        InstanceExpression node;

        FinalExpression groupExpr = exprVisitor.visitIdentifier(ctx.groupId);
        if (groupExpr == null) {
            // unable to resolve reference => using identifier as name
            group = new InstanceExpression(ctx.groupId.getText(), null);
        } else {
            group = new InstanceExpression(groupExpr.toText(), null);
        }

        FinalExpression nodeExpr = exprVisitor.visitIdentifier(ctx.nodeId);
        if (nodeExpr == null) {
            // unable to resolve reference => using identifier as name
            node = new InstanceExpression(ctx.nodeId.getText(), null);
        } else {
            node = new InstanceExpression(nodeExpr.toText(), null);
        }

        return new Commands().addCommand(new AttachCommand(group, node));
    }

    @Override
    public Commands visitDetach(DetachContext ctx) {
        final Commands cmds = new Commands();
        for (final InstancePathContext iPath : ctx.instanceList().instancePath()) {
            InstanceExpression instance = new ExpressionVisitor(this.context).visitInstancePath(iPath);
            cmds.addCommand(new DetachCommand(new InstanceExpression(instance.instanceName, null)));
        }
        return cmds;
    }

    @Override
    public Commands visitMove(final MoveContext ctx) {
        final Commands commands = new Commands();
        final ExpressionVisitor exprVisitor = new ExpressionVisitor(this.context);

        final InstanceExpression target = exprVisitor.visitInstancePath(ctx.instancePath());
        for (final InstancePathContext sourceCtx : ctx.instanceList().instancePath()) {
            final InstanceExpression source = exprVisitor.visitInstancePath(sourceCtx);
            commands.addCommand(new MoveCommand(target, new InstanceExpression(source.instanceName, null)));
        }

        return commands;
    }

    @Override
    public Commands visitStart(final StartContext ctx) {
        final Commands commands = new Commands();
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        for (final InstancePathContext instancePath : ctx.instanceList().instances) {
            final InstanceExpression instanceExpr = expressionVisitor.visitInstancePath(instancePath);
            commands.addCommand(new StartCommand(new InstanceExpression(instanceExpr.instanceName, null)));
        }
        return commands;
    }

    @Override
    public Commands visitStop(final StopContext ctx) {
        final Commands commands = new Commands();
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        for (final InstancePathContext instancePath : ctx.instanceList().instances) {
            final InstanceExpression instanceExpr = expressionVisitor.visitInstancePath(instancePath);
            commands.addCommand(new StopCommand(new InstanceExpression(instanceExpr.instanceName, null)));
        }
        return commands;
    }

    @Override
    public Commands visitLetDecl(final LetDeclContext ctx) {
        final boolean isExported = ctx.EXPORT() != null;
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        final FinalExpression expr = expressionVisitor.visitExpression(ctx.val);
        this.context.addExpression(ctx.basicIdentifier().getText(), expr, isExported);
        return expressionVisitor.aggregatedFunctionsCommands;
    }

    @Override
    public Commands visitBind(BindContext ctx) {
        final Commands cmds = new Commands();
        final ExpressionVisitor exprVisitor = new ExpressionVisitor(this.context);
        InstanceExpression chanInstance;
        FinalExpression chanExpr = exprVisitor.visitIdentifier(ctx.chan);
        if (chanExpr == null) {
            chanInstance = new InstanceExpression(ctx.chan.getText(), null);
        } else {
            chanInstance = new InstanceExpression(chanExpr.toText(), null);
        }

        for (PortPathContext pPath : ctx.portList().instances) {
            PortPathExpression pExpr = exprVisitor.visitPortPath(pPath);
            cmds.addCommand(new BindCommand(chanInstance, pExpr));
        }

        return cmds;
    }

    @Override
    public Commands visitUnbind(final UnbindContext ctx) {
        final Commands cmds = new Commands();
        final ExpressionVisitor exprVisitor = new ExpressionVisitor(this.context);
        final FinalExpression chanExpr = exprVisitor.visitIdentifier(ctx.chan);
        final InstanceExpression chanInstance;
        if (chanExpr == null) {
            chanInstance = new InstanceExpression(ctx.chan.getText(), null);
        } else {
            chanInstance = new InstanceExpression(chanExpr.toText(), null);
        }

        for (final PortPathContext pPath : ctx.portList().instances) {
            final PortPathExpression pExpr = exprVisitor.visitPortPath(pPath);
            cmds.addCommand(new UnbindCommand(new InstanceExpression(chanInstance.instanceName, null), pExpr));
        }

        return cmds;
    }

    @Override
    public Commands visitFuncDecl(final FuncDeclContext ctx) {
        final boolean exported = ctx.EXPORT() != null;
        if (ctx.NATIVE() == null) {
            // We snapshot the current context as the one in the scope of the function (for later call).
            final Context funcCtx = new Context();
            funcCtx.setContext(this.context.getInheritedContext());
            final FunctionExpression functionExpression = new FunctionExpression(funcCtx);
            if (ctx.parameters != null) {
                for (final BasicIdentifierContext param : ctx.parameters.basicIdentifier()) {
                    functionExpression.addParam(param.getText());
                }
            }
            functionExpression.setFunctionBody(ctx.funcBody());
            this.context.addExpression(ctx.functionName.getText(), functionExpression, exported);

        } else {
            final FunctionNativeExpression functionNativeExpression = new FunctionNativeExpression();
            if (ctx.parameters != null) {
                for (final BasicIdentifierContext param : ctx.parameters.basicIdentifier()) {
                    functionNativeExpression.addParam(param.getText());
                }
            }

            final String text = ctx.SOURCE_CODE().getText();
            functionNativeExpression.setFunctionBody(text.substring(4, text.length() - 4));
            this.context.addExpression(ctx.functionName.getText(), functionNativeExpression, exported);
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
    public Commands visitSet(final SetContext ctx) {
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        final DictionaryPathExpression instanceDicoRef = expressionVisitor.visitDictionaryPath(ctx.dictionaryPath());
        final FinalExpression value = expressionVisitor.visitExpression(ctx.val);
        final SetCommand setCommand = new SetCommand(instanceDicoRef, value.toText());
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
        InstanceExpression instance = this.helper.processIdentifierAsInstance(ctx.identifier(0));
        ObjectDeclExpression object = this.helper.getObjectByDeclOrIdentifier(ctx.objectDecl(), ctx.identifier(1));
        return new Commands().addCommand(new NetInitCommand(instance, object));
    }

    @Override
    public Commands visitNetmerge(NetmergeContext ctx) {
        InstanceExpression instance = this.helper.processIdentifierAsInstance(ctx.identifier(0));
        ObjectDeclExpression object = this.helper.getObjectByDeclOrIdentifier(ctx.objectDecl(), ctx.identifier(1));
        return new Commands().addCommand(new NetMergeCommand(instance, object));
    }

    @Override
    public Commands visitNetremove(final NetremoveContext ctx) {
        final InstanceExpression node = this.helper.processIdentifierAsInstance(ctx.identifier(0));
        final List<String> objectRefs = helper.getListObjectRefs(ctx.identifierList(), ctx.identifier(1));
        return new Commands().addCommand(new NetRemoveCommand(node, objectRefs));
    }


    @Override
    public Commands visitMetainit(final MetainitContext ctx) {
        InstanceExpression instance = this.helper.processIdentifierAsInstance(ctx.identifier(0));
        ObjectDeclExpression object = this.helper.getObjectByDeclOrIdentifier(ctx.objectDecl(), ctx.identifier(1));
        return new Commands().addCommand(new MetaInitCommand(instance, object));
    }

    @Override
    public Commands visitMetamerge(final MetamergeContext ctx) {
        InstanceExpression instance = this.helper.processIdentifierAsInstance(ctx.identifier(0));
        ObjectDeclExpression object = this.helper.getObjectByDeclOrIdentifier(ctx.objectDecl(), ctx.identifier(1));
        return new Commands().addCommand(new MetaMergeCommand(instance, object));
    }

    @Override
    public Commands visitMetaremove(final MetaremoveContext ctx) {
        final Commands cmds = new Commands();
        InstanceExpression instance = this.helper.processIdentifierAsInstance(ctx.identifier(0));

        if (ctx.identifierList() != null) {
            for (IdentifierContext idCtx : ctx.identifierList().identifiers) {
                ObjectDeclExpression object = this.helper.getObjectDeclExpression(idCtx);
                cmds.addCommand(new MetaRemoveCommand(instance, object));
            }
        } else {
            ObjectDeclExpression object = this.helper.getObjectDeclExpression(ctx.identifier(1));
            cmds.addCommand(new MetaRemoveCommand(instance, object));
        }

        return cmds;
    }

    @Override
    public Commands visitImportDecl(final ImportDeclContext ctx) {
        final String resourcePath = ctx.resource.getText();
        final Map<String, FinalExpression> inheritedContext = helper.loadContext(resourcePath, importsStore);

        final String qualifier;
        if (ctx.AS() != null) {
            final String root = ctx.basicIdentifier().getText();
            this.context.addExpression(root, new NullExpression());
            qualifier = root + ".";

        } else {
            qualifier = "";
        }

        // look for required components in the parsed script.
        if (ctx.qualifiers == null) {
            // import everything
            for (Map.Entry<String, FinalExpression> entry : inheritedContext.entrySet()) {
                final FinalExpression expression = entry.getValue();
                final String key = qualifier + entry.getKey();
                this.context.basicAddExpression(key, expression);
            }
        } else {
            // import only required elements
            for (final BasicIdentifierContext a : ctx.qualifiers.basicIdentifier()) {
                final String key = a.getText();
                if (inheritedContext.containsKey(key)) {
                    final FinalExpression expression = inheritedContext.get(key);
                    this.context.addExpression(qualifier + key, expression);
                } else {
                    throw new ImportException(key, resourcePath);
                }
            }
        }

        return new Commands();
    }


}
