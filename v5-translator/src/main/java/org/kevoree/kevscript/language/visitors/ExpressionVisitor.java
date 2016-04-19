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
        final String namespace;
        final String name;
        final TypeNameContext typeNameContext = ctx.typeName();
        final List<BasicIdentifierContext> basicIdentifierContexts = typeNameContext.basicIdentifier();
        if (basicIdentifierContexts.size() > 1) {
            namespace = typeNameContext.basicIdentifier(0).getText();
            name = typeNameContext.basicIdentifier(1).getText();
        } else {
            name = typeNameContext.basicIdentifier(0).getText();
            namespace = null;
        }
        final VersionExpression versionExpr = this.visitVersion(ctx.version());
        final ObjectDeclExpression duVersions = this.visitDuVersions(ctx.duVersions());
        return new TypeExpression(namespace, name, versionExpr, duVersions);
    }

    @Override
    public NumericExpression visitExpressionNumber(final ExpressionNumberContext ctx) {
        return new NumericExpression(Integer.parseInt(ctx.getText()));
    }

    @Override
    public StringExpression visitExpressionConcat(final ExpressionConcatContext ctx) {
        final PrimitiveExpression left = this.context.lookup(this.visit(ctx.expression(0)), PrimitiveExpression.class, ctx);
        final PrimitiveExpression right = this.context.lookup(this.visit(ctx.expression(1)), PrimitiveExpression.class, ctx);
        return new StringExpression(left.toText() + right.toText());
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
        return this.context.lookup(identifier, ctx);
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
    public FinalExpression visitIdentifierBasicIdentifier(IdentifierBasicIdentifierContext ctx) {
        return visitBasicIdentifier(ctx);
    }

    @Override
    public FinalExpression visitIdentifierFunCall(final IdentifierFunCallContext ctx) {
        final FinalExpression ret;
        if (ctx.DOT() != null) {
            ret = visitPostFunctionObjectReference(ctx.identifier(), visitFuncCall(ctx.funcCall()));
        } else {
            ret = visitFuncCall(ctx.funcCall());
        }
        return ret;
    }

    @Override
    public FinalExpression visitIdentifierFuncCallArrayAccess(IdentifierFuncCallArrayAccessContext ctx) {
        final FinalExpression ret;
        if (ctx.arrayAccess() != null) {
            ret = visitPostFunctionArrayReference(ctx.arrayAccess(), ctx.identifier(), visitFuncCall(ctx.funcCall()));
        } else if (ctx.DOT() != null) {
            ret = visitPostFunctionObjectReference(ctx, visitFuncCall(ctx.funcCall()));
        } else {
            ret = visitFuncCall(ctx.funcCall());
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
            ret = new ExpressionVisitor(context).visit(ctx);
        } else {
            if (returnedExpression == null) {
                throw new WrongTypeException(ctx, ObjectDeclExpression.class, NullExpression.class);
            } else {
                throw new WrongTypeException(ctx, ObjectDeclExpression.class, returnedExpression.getClass());
            }
        }
        return ret;
    }

    private FinalExpression visitPostFunctionArrayReference(final ArrayAccessContext ctx, final IdentifierContext identifierContext, final FinalExpression arrayExpression) {
        final FinalExpression ret;
        if (arrayExpression instanceof ArrayDeclExpression) {
            final ArrayDeclExpression arrayDecl = (ArrayDeclExpression) arrayExpression;
            final FinalExpression arrayIndexExpr = this.visit(ctx);
            if (!(arrayIndexExpr instanceof NumericExpression)) {
                throw new WrongTypeException(ctx, NumericExpression.class, arrayIndexExpr.getClass());
            } else {
                final int arrayIndex = ((NumericExpression) arrayIndexExpr).value;
                if (arrayDecl.expressionList.size() - 1 >= arrayIndex) {
                    ret = arrayDecl.expressionList.get(arrayIndex);
                } else {
                    throw new ArrayIndexOutOfBoundException(ctx.getText(), arrayIndex);
                }
            }
        } else {
            if (arrayExpression == null) {
                throw new WrongTypeException(ctx, ArrayDeclExpression.class, NullExpression.class);
            } else {
                throw new WrongTypeException(ctx, ArrayDeclExpression.class, arrayExpression.getClass());
            }
        }
        return ret;
    }

    @Override
    public ObjectDeclExpression visitDuVersions(final DuVersionsContext ctx) {
        final ObjectDeclExpression ret;
        if (ctx != null) {
            if (ctx.objectDecl() != null) {
                ret = this.visitObjectDecl(ctx.objectDecl());
            } else {
                final IdentifierContext identifier1 = ctx.identifier();
                final FinalExpression identifier = this.visit(identifier1);
                final FinalExpression expr = this.context.lookup(identifier, ctx);
                if (expr instanceof ObjectDeclExpression) {
                    ret = (ObjectDeclExpression) expr;
                } else {
                    throw new WrongTypeException(identifier1, ObjectDeclExpression.class, expr.getClass());
                }
            }
        } else {
            ret = null;
        }
        return ret;
    }

    private FinalExpression visitBasicIdentifier(final IdentifierContext ctx) {
        // this will process basicIdentifier as well as basicIdentifier + arrayAccess
        return this.context.lookupByStrKey(ctx.getText(), FinalExpression.class, false, ctx);
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
                this.context.lookupByStrKey(functionName, AbstractFunctionExpression.class, ctx);

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
            final FinalExpression expression = this.visit(expressionCtx);
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
            final FinalExpression returnRes = expressionVisitor.visit(functionBody.returnStatement().expression());
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
        final IdentifierContext identifier = ctx.identifier();
        if (ctx.instancePath() != null) {
            // its a full path (eg. node:comp<-port)
            final InstanceExpression chanExpr = this.visitInstancePath(ctx.instancePath());
            FinalExpression portNameExpr = this.visit(identifier);
            if (portNameExpr == null) {
                // unable to resolve expr => use identifier as name
                portNameExpr = new InstanceExpression(identifier.getText(), null);
            }

            ret = new PortPathExpression(new InstanceExpression(chanExpr.instanceName, null), ctx.LEFT_ARROW() != null, portNameExpr.toText());
        } else {
            // its a reference to port with instancePath => instance must be found in context
            final FinalExpression portNameExpr = this.visit(identifier);
            if (portNameExpr instanceof PortPathExpression) {
                ret = (PortPathExpression) portNameExpr;
            } else {
                if (portNameExpr != null) {
                    throw new WrongTypeException(identifier, PortPathExpression.class, portNameExpr.getClass());
                } else {
                    throw new WrongTypeException(identifier, PortPathExpression.class, NullExpression.class);
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
                final FinalExpression lookup = this.context.lookup(this.visit(ctx.identifier()), StringExpression.class, ctx);
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
        final IdentifierContext name = ctx.name;
        final FinalExpression paramNameExpr = this.visit(name);
        final String paramName;
        if (paramNameExpr != null) {
            if (paramNameExpr instanceof StringExpression) {
                paramName = paramNameExpr.toText();
            } else {
                throw new WrongTypeException(name, StringExpression.class, null);
            }
        } else {
            paramName = ctx.identifier(0).getText();
        }

        final InstanceExpression instanceExprTmp = this.visitInstancePath(ctx.instancePath());
        final InstanceExpression instanceExpr = new InstanceExpression(instanceExprTmp.instanceName, null);
        final DictionaryPathExpression ret;
        if (ctx.fragmentName != null) {
            final FinalExpression fragExpr = this.visit(ctx.fragmentName);
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
    public FinalExpression visitIterableArrayDecl(final IterableArrayDeclContext ctx) {
        return this.visitArrayDecl(ctx.arrayDecl());
    }

    @Override
    public FinalExpression visitIterableContextIdentifier(final IterableContextIdentifierContext ctx) {
        final ContextIdentifierContext context = ctx.contextIdentifier();
        final FinalExpression res = new ExpressionVisitor(this.context).visitContextIdentifier(context);

        final ArrayDeclExpression ret;
        if (res instanceof ArrayDeclExpression) {
            ret = (ArrayDeclExpression) res;
        } else {
            throw new WrongTypeException(context, ArrayDeclExpression.class, res.getClass());
        }

        return ret;
    }

    @Override
    public FinalExpression visitIterableIdentifier(final IterableIdentifierContext ctx) {
        return this.context.lookup(this.visit(ctx.identifier()), ArrayDeclExpression.class, ctx);
    }

    @Override
    public FinalExpression visitArrayAccessConst(ArrayAccessConstContext ctx) {
        return new NumericExpression(Integer.parseInt(ctx.NUMERIC_VALUE().getText()));
    }

    @Override
    public FinalExpression visitArrayAccessVariable(ArrayAccessVariableContext ctx) {
        final FinalExpression expression = this.visit(ctx.expression());
        final FinalExpression ret;
        if (expression instanceof NumericExpression) {
            ret = expression;
        } else if (expression instanceof StringExpression) {
            final StringExpression expressionStr = (StringExpression) expression;
            try {
                ret = new NumericExpression(Integer.parseInt(expressionStr.text));
            } catch (NumberFormatException e) {
                throw new WrongTypeException(ctx.expression(), NumericExpression.class, expressionStr.getClass());
            }
        } else {
            throw new WrongTypeException(ctx.expression(), NumericExpression.class, expression.getClass());
        }
        return ret;
    }


    @Override
    public FinalExpression visitBasicIdentifier(BasicIdentifierContext ctx) {
        return this.context.lookupByStrKey(ctx.getText(), FinalExpression.class, false, ctx);
    }

    @Override
    public FinalExpression visitIdentifierArrayAccess(IdentifierArrayAccessContext ctx) {
        final FinalExpression basicIdentifierExpression = this.visitBasicIdentifier(ctx.basicIdentifier());
        return visitPostFunctionArrayReference(ctx.arrayAccess(), ctx.identifier(), basicIdentifierExpression);
    }
}


