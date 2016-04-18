package org.kevoree.kevscript.language.visitors;

import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.ArrayIndexOutOfBoundException;
import org.kevoree.kevscript.language.excpt.VersionNotFound;
import org.kevoree.kevscript.language.excpt.WrongNumberOfArguments;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.finalexp.*;
import org.kevoree.kevscript.language.expressions.finalexp.function.AbstractFunctionExpression;
import org.kevoree.kevscript.language.expressions.finalexp.function.FunctionExpression;
import org.kevoree.kevscript.language.expressions.nonfinalexp.ContextIdentifierExpression;
import org.kevoree.kevscript.language.expressions.nonfinalexp.ContextRefExpression;
import org.kevoree.kevscript.language.utils.JsEngine;
import org.kevoree.kevscript.language.utils.NotImplementedException;
import org.kevoree.kevscript.language.utils.StringUtils;
import org.kevoree.kevscript.language.visitors.helper.KevscriptHelper;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 *
 *
 */
public class ExpressionVisitor extends KevScriptBaseVisitor<FinalExpression> {
    public final Commands aggregatedFunctionsCommands = new Commands();
    private final Context context;
    private final KevscriptHelper helper;

    public ExpressionVisitor(final Context context) {
        this.context = context;
        this.helper = new KevscriptHelper(this.context);
    }

    @Override
    public TypeExpression visitType(TypeContext ctx) {
        String namespace = null;
        String name;
        if (ctx.typeName().basicIdentifier().size() > 1) {
            namespace = ctx.typeName().basicIdentifier(0).getText();
            name = ctx.typeName().basicIdentifier(1).getText();
        } else {
            name = ctx.typeName().basicIdentifier(0).getText();
        }
        VersionExpression versionExpr = this.visitVersion(ctx.version());
        ObjectDeclExpression duVersions = this.visitDuVersions(ctx.duVersions());
        return new TypeExpression(namespace, name, versionExpr, duVersions);
    }

    @Override
    public FinalExpression visitExpression(ExpressionContext ctx) {
        final FinalExpression ret;
        if (ctx.CONCAT() != null) {
            final StringExpression left = this.context.lookup(this.visit(ctx.expression(0)), StringExpression.class);
            final StringExpression right = this.context.lookup(this.visit(ctx.expression(1)), StringExpression.class);
            ret = new StringExpression(left.toText() + right.toText());
        } else if (ctx.string() != null) {
            ret = this.visitString(ctx.string());
        } else if (ctx.objectDecl() != null) {
            ret = this.visitObjectDecl(ctx.objectDecl());
        } else if (ctx.contextRef() != null) {
            ret = this.visitContextRef(ctx.contextRef());
        } else if (ctx.arrayDecl() != null) {
            ret = this.visitArrayDecl(ctx.arrayDecl());
        } else if (ctx.identifier() != null) {
            ret = this.visitIdentifier(ctx.identifier());
        } else if (ctx.instancePath() != null) {
            ret = this.visitInstancePath(ctx.instancePath());
        } else if (ctx.portPath() != null) {
            ret = this.visitPortPath(ctx.portPath());
        } else {
            throw new NotImplementedException(ctx + " expression context unknown");
        }
        return ret;
    }

    @Override
    public StringExpression visitString(StringContext ctx) {
        return new StringExpression(ctx.getText().substring(1, ctx.getText().length() - 1));
    }

    @Override
    public ObjectDeclExpression visitObjectDecl(ObjectDeclContext ctx) {
        final ObjectDeclExpression ret = new ObjectDeclExpression();
        for (KeyAndValueContext value : ctx.values) {
            final FinalExpression visit = this.visit(value.value);
            if (visit != null) {
                ret.put(value.key.getText(), visit);
            } else {
                ret.put(value.key.getText(), new InstanceExpression(value.value.getText(), null));
            }
        }
        return ret;
    }

    @Override
    public FinalExpression visitContextIdentifier(final ContextIdentifierContext ctx) {
        final FinalExpression res;
        if (ctx.contextRef() != null) {
            res = this.visitContextRef(ctx.contextRef());
        } else {
            final ContextIdentifierExpression ret = recVisitContextIdentifier(ctx);
            res = this.context.lookup(ret);
        }
        return res;
    }

    private ContextIdentifierExpression recVisitContextIdentifier(final ContextIdentifierContext ctx) {
        final ContextIdentifierExpression ret = new ContextIdentifierExpression();
        if (ctx.basicIdentifier() != null) {
            ret.add(ctx.basicIdentifier().getText());
        } else if (ctx.DOT() != null) {
            final ContextIdentifierExpression left = this.recVisitContextIdentifier(ctx.contextIdentifier(0));
            final ContextIdentifierExpression right = this.recVisitContextIdentifier(ctx.contextIdentifier(1));
            ret.addAll(left);
            ret.addAll(right);
        }
        return ret;
    }

    @Override
    public FinalExpression visitContextRef(ContextRefContext ctx) {
        final ContextIdentifierExpression ret = recVisitContextIdentifier(ctx.contextIdentifier());
        final ContextRefExpression identifier = new ContextRefExpression();
        identifier.addAll(ret);
        return this.context.lookup(identifier);
    }

    @Override
    public ArrayDeclExpression visitArrayDecl(ArrayDeclContext ctx) {
        final ArrayDeclExpression ret = new ArrayDeclExpression();
        if (ctx.expressionList() != null) {
            for (ExpressionContext expression : ctx.expressionList().expression()) {
                ret.add(this.visit(expression));
            }
        }
        return ret;
    }

    @Override
    public FinalExpression visitIdentifier(IdentifierContext ctx) {
        FinalExpression ret;
        if (ctx.basicIdentifier() != null) {
            ret = visitBasicIdentifier(ctx);
        } else if (ctx.contextRef() != null) {
            ret = this.visit(ctx.contextRef());
        } else if (ctx.funcCall() != null) {
            ret = visitFuncCall(ctx.funcCall());
            if (ctx.arrayAccess() != null) {
                ret = visitPostFunctionArrayReference(ctx, ret);
            } else if (ctx.DOT() != null) {
                ret = visitPostFunctionObjectReference(ctx, ret);
            }
        } else {
            throw new NotImplementedException(ctx + "identifier unknown");
        }
        return ret;
    }

    private FinalExpression visitPostFunctionObjectReference(final IdentifierContext ctx, final FinalExpression returnedExpression) {
        final FinalExpression ret;
        if (returnedExpression instanceof ObjectDeclExpression) {
            final ObjectDeclExpression objDecl = (ObjectDeclExpression) returnedExpression;
            final Context context = new Context();
            for (final Map.Entry<String, FinalExpression> objectEntry : objDecl.values.entrySet()) {
                context.addExpression(objectEntry.getKey(), objectEntry.getValue());
            }
            ret = new ExpressionVisitor(context).visitIdentifier(ctx.identifier());
        } else {
            if (returnedExpression == null) {
                throw new WrongTypeException(ctx.getText(), ObjectDeclExpression.class, NullExpression.class);
            } else {
                throw new WrongTypeException(ctx.getText(), ObjectDeclExpression.class, returnedExpression.getClass());
            }
        }
        return ret;
    }

    private FinalExpression visitPostFunctionArrayReference(final IdentifierContext ctx, final FinalExpression returnExpression) {
        final FinalExpression ret;
        if (returnExpression instanceof ArrayDeclExpression) {
            final ArrayDeclExpression arrayDecl = (ArrayDeclExpression) returnExpression;
            final int arrayIndex = this.helper.convertArrayAccessToInt(ctx.arrayAccess());
            if (arrayDecl.expressionList.size() - 1 >= arrayIndex) {
                ret = arrayDecl.expressionList.get(arrayIndex);
            } else {
                throw new ArrayIndexOutOfBoundException(ctx.getText(), arrayIndex);
            }
        } else {
            if (returnExpression == null) {
                throw new WrongTypeException(ctx.getText(), ArrayDeclExpression.class, NullExpression.class);
            } else {
                throw new WrongTypeException(ctx.getText(), ArrayDeclExpression.class, returnExpression.getClass());
            }
        }
        return ret;
    }

    @Override
    public ObjectDeclExpression visitDuVersions(DuVersionsContext ctx) {
        if (ctx != null) {
            if (ctx.objectDecl() != null) {
                return this.visitObjectDecl(ctx.objectDecl());
            } else {
                FinalExpression identifier = this.visitIdentifier(ctx.identifier());
                FinalExpression expr = this.context.lookup(identifier);
                if (expr instanceof ObjectDeclExpression) {
                    return (ObjectDeclExpression) expr;
                } else {
                    throw new WrongTypeException(ctx.identifier().getText(), ObjectDeclExpression.class, expr.getClass());
                }
            }
        }
        return null;
    }

    private FinalExpression visitBasicIdentifier(final IdentifierContext ctx) {
        // this will process basicIdentifier as well as basicIdentifier + arrayAccess
        return this.context.lookupByStrKey(ctx.getText(), FinalExpression.class, false);
    }

    @Override
    public FinalExpression visitFuncCall(final FuncCallContext ctx) {
        final String functionName;
        if (ctx.basicIdentifier().size() == 1) {
            functionName = ctx.basicIdentifier(0).getText();
        } else {
            functionName = ctx.basicIdentifier(0).getText() + "." + ctx.basicIdentifier(1).getText();
        }
        final AbstractFunctionExpression functionExpression =
                this.context.lookupByStrKey(functionName, AbstractFunctionExpression.class);

        final List<ExpressionContext> parameters;
        if (ctx.parameters != null) {
            parameters = ctx.parameters.expression();
        } else {
            parameters = new ArrayList<>();
        }

        final int definedNbr = functionExpression.getParametersSize();
        final int callerNbr = parameters.size();
        if (callerNbr != definedNbr) {
            throw new WrongNumberOfArguments(functionName, definedNbr, callerNbr);
        }

        final FinalExpression returnValue;
        if (functionExpression instanceof FunctionExpression) {
            returnValue = visitFunctionExpression((FunctionExpression) functionExpression, parameters);
        } else {
            returnValue = visitFunctionNativeExpression(functionName, functionExpression, parameters);
        }

        return returnValue;
    }

    private FinalExpression visitFunctionNativeExpression(String functionName, AbstractFunctionExpression functionExpression, List<ExpressionContext> parameters) {
        FinalExpression returnValue;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("function ");
            sb.append(functionName);
            sb.append("(");
            sb.append(StringUtils.join(functionExpression.getParameters(), ", "));
            sb.append(")");
            sb.append("{");
            sb.append(functionExpression.getFunctionBody());
            sb.append('\n');
            sb.append("}");
            final List<String> parametersStr = new ArrayList<>();
            for (ExpressionContext x : parameters) {
                parametersStr.add(x.getText().replaceAll("\"", ""));
            }

            sb.append('\n');
            sb.append(functionName);
            sb.append('(');
            sb.append(StringUtils.join(parametersStr, ", "));
            sb.append(')');
            returnValue = new JsEngine().evaluateFunction(sb.toString());
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return returnValue;
    }

    private FinalExpression visitFunctionExpression(final FunctionExpression funcExpr, final List<ExpressionContext> parameters) {
        final Commands commands = new Commands();
        int parameterCount = 0;
        final Context context = new Context(funcExpr.context);
        for (final ExpressionContext expressionCtx : parameters) {
            final FinalExpression expression = this.visitExpression(expressionCtx);
            final String parameterName = funcExpr.getParameter(parameterCount++);
            context.addExpression(parameterName, expression);
        }

        final KevscriptVisitor kevscriptVisitor = new KevscriptVisitor(context);
        final FuncBodyContext functionBody = funcExpr.getFunctionBody();
        for (final StatementContext stmt : functionBody.statement()) {
            commands.addAll(kevscriptVisitor.visit(stmt));
        }

        final FinalExpression returnValue;
        if (functionBody.returnStatement() != null) {
            final ExpressionVisitor expressionVisitor = new ExpressionVisitor(kevscriptVisitor.getContext());
            final FinalExpression returnRes = expressionVisitor.visitExpression(functionBody.returnStatement().expression());
            commands.addAll(expressionVisitor.aggregatedFunctionsCommands);
            returnValue = returnRes;
        } else {
            returnValue = null;
        }

        this.aggregatedFunctionsCommands.addAll(commands);
        return returnValue;
    }

    @Override
    public InstanceExpression visitInstancePath(final InstancePathContext ctx) {
        final InstanceExpression ret;
        if (ctx.identifier().size() == 1) {
            ret = this.helper.getInstanceExpressionFromContext(ctx.identifier(0));
        } else {
            final InstanceExpression parent = this.helper.getInstanceExpressionFromContext(ctx.identifier(0));
            final InstanceExpression children = this.helper.getInstanceExpressionFromContext(ctx.identifier(1));
            return new InstanceExpression(parent.instanceName + ":" + children.instanceName, children.typeExpr);
        }

        return ret;
    }

    @Override
    public PortPathExpression visitPortPath(final PortPathContext ctx) {
        final PortPathExpression ret;
        if (ctx.instancePath() != null) {
            // its a full path (eg. node:comp<-port)
            final InstanceExpression chanExpr = this.visitInstancePath(ctx.instancePath());
            FinalExpression portNameExpr = this.visitIdentifier(ctx.identifier());
            if (portNameExpr == null) {
                // unable to resolve expr => use identifier as name
                portNameExpr = new InstanceExpression(ctx.identifier().getText(), null);
            }

            ret = new PortPathExpression(new InstanceExpression(chanExpr.instanceName, null), ctx.LEFT_ARROW() != null, portNameExpr.toText());
        } else {
            // its a reference to port with instancePath => instance must be found in context
            final FinalExpression portNameExpr = this.visitIdentifier(ctx.identifier());
            if (portNameExpr instanceof PortPathExpression) {
                ret = (PortPathExpression) portNameExpr;
            } else {
                if (portNameExpr != null) {
                    throw new WrongTypeException(ctx.identifier().getText(), PortPathExpression.class, portNameExpr.getClass());
                } else {
                    throw new WrongTypeException(ctx.identifier().getText(), PortPathExpression.class, NullExpression.class);
                }
            }
        }
        return ret;
    }

    @Override
    public VersionExpression visitVersion(VersionContext ctx) {
        if (ctx != null) {
            final VersionExpression ret;
            if (ctx.NUMERIC_VALUE() != null) {
                ret = new VersionExpression(Long.parseLong(ctx.NUMERIC_VALUE().getText()));
            } else {
                final FinalExpression lookup = this.context.lookup(this.visitIdentifier(ctx.identifier()), StringExpression.class);
                if (lookup instanceof StringExpression) {
                    ret = new VersionExpression(Long.parseLong(((StringExpression) lookup).text));
                } else {
                    throw new VersionNotFound(lookup);
                }
            }

            return ret;
        }
        return null;
    }


    @Override
    public DictionaryPathExpression visitDictionaryPath(final DictionaryPathContext ctx) {
        final FinalExpression paramNameExpr = this.visitIdentifier(ctx.name);
        final String paramName;
        if (paramNameExpr != null) {
            if (paramNameExpr instanceof StringExpression) {
                paramName = paramNameExpr.toText();
            } else {
                throw new WrongTypeException(ctx.name.getText(), StringExpression.class, null);
            }
        } else {
            paramName = ctx.identifier(0).getText();
        }

        final InstanceExpression instanceExprTmp = this.visitInstancePath(ctx.instancePath());
        final InstanceExpression instanceExpr = new InstanceExpression(instanceExprTmp.instanceName, null);
        final DictionaryPathExpression ret;
        if (ctx.fragmentName != null) {
            final FinalExpression fragExpr = this.visitIdentifier(ctx.fragmentName);
            final String fragName;
            if (fragExpr == null) {
                // looks like ctx.fragmentName is not resolvable => try to use the name identifier as fragment name
                fragName = ctx.fragmentName.getText();
            } else {
                fragName = fragExpr.toText();
            }
            ret = new DictionaryPathExpression(instanceExpr, paramName, fragName);
        } else {
            ret = new DictionaryPathExpression(instanceExpr, paramName, null);
        }
        return ret;
    }

    @Override
    public ArrayDeclExpression visitIterable(final IterableContext ctx) {
        final ArrayDeclExpression ret;
        if (ctx.arrayDecl() != null) {
            ret = this.visitArrayDecl(ctx.arrayDecl());
        } else if (ctx.identifier() != null) {
            ret = this.context.lookup(this.visitIdentifier(ctx.identifier()), ArrayDeclExpression.class);
        } else {
            final FinalExpression res = new ExpressionVisitor(context).visitContextIdentifier(ctx.contextIdentifier());
            if (res instanceof ArrayDeclExpression) {
                ret = (ArrayDeclExpression) res;
            } else {
                throw new WrongTypeException(ctx.contextIdentifier().getText(), ArrayDeclExpression.class, res.getClass());
            }
        }
        return ret;
    }
}
