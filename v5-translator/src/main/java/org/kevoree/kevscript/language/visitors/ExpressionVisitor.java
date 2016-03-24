package org.kevoree.kevscript.language.visitors;

import org.apache.commons.lang3.NotImplementedException;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.VersionNotFound;
import org.kevoree.kevscript.language.excpt.WrongNumberOfArguments;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.*;
import org.kevoree.kevscript.language.visitors.helper.KevscriptHelper;

import java.util.ArrayList;
import java.util.List;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 02/03/16.
 */
public class ExpressionVisitor extends KevScriptBaseVisitor<FinalExpression> {
    private final Context context;
    private final KevscriptHelper helper;


    public final Commands aggregatedFunctionsCommands = new Commands();

    public ExpressionVisitor(final Context context) {
        this.context = context;
        this.helper = new KevscriptHelper(this.context);
    }

    @Override
    public FinalExpression visitExpression(ExpressionContext ctx) {
        final FinalExpression ret;
        if (ctx.CONCAT() != null) {
            final StringExpression left = this.context.lookup(this.visit(ctx.expression(0)), StringExpression.class);
            final StringExpression right = this.context.lookup(this.visit(ctx.expression(1)), StringExpression.class);
            ret = new StringExpression(left.toText() + right.toText());
        } else if (ctx.string() != null) {
            ret = this.visit(ctx.string());
        } else if (ctx.objectDecl() != null) {
            ret = this.visit(ctx.objectDecl());
        } else if (ctx.contextIdentifier() != null) {
            ret = this.visit(ctx.contextIdentifier());
        } else if (ctx.arrayDecl() != null) {
            ret = this.visit(ctx.arrayDecl());
        } else if (ctx.arrayAccess() != null) {
            ret = this.visit(ctx.arrayAccess());
        } else if (ctx.identifier() != null) {
            ret = this.visit(ctx.identifier());
        } else if (ctx.funcCall() != null) {
            ret = this.visit(ctx.funcCall());
        } else if (ctx.instancePath() != null) {
            ret = this.visit(ctx.instancePath());
        } else if (ctx.portPath() != null) {
            ret = this.visit(ctx.portPath());
        } else {
            throw new NotImplementedException(ctx + " expression context unknow.");
        }
        return ret;
    }

    @Override
    public StringExpression visitString(StringContext ctx) {
        return new StringExpression(ctx.getText());
    }

    @Override
    public ObjectDeclExpression visitObjectDecl(ObjectDeclContext ctx) {
        final ObjectDeclExpression ret = new ObjectDeclExpression();
        for (KeyAndValueContext value : ctx.values) {
            ret.put(value.key.getText(), this.visit(value.value));
        }
        return ret;
    }

    @Override
    public FinalExpression visitContextIdentifier(final ContextIdentifierContext ctx) {
        final ContextIdentifierExpression ret = recVisitContextIdentifier(ctx);
        return this.context.lookup(ret);
    }

    private ContextIdentifierExpression recVisitContextIdentifier(final ContextIdentifierContext ctx) {
        final ContextIdentifierExpression ret = new ContextIdentifierExpression();
        if (ctx.basic_identifier() != null) {
            ret.add(ctx.basic_identifier().getText());
        } else if (ctx.contextRef() != null) {
            ret.add(this.visit(ctx.contextRef()).toText());
        } else if (ctx.arrayAccess() != null) {
            ret.add(this.visit(ctx.arrayAccess()).toText());
        } else if (ctx.DOT() != null) {
            final ContextIdentifierExpression left = this.recVisitContextIdentifier(ctx.contextIdentifier(0));
            final ContextIdentifierExpression right = this.recVisitContextIdentifier(ctx.contextIdentifier(1));
            ret.addAll(left);
            ret.addAll(right);
        }
        return ret;
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
    public FinalExpression visitArrayAccess(ArrayAccessContext ctx) {
        return new ArrayAccessExpression(ctx.basic_identifier().getText(), Long.parseLong(ctx.NUMERIC_VALUE().getText()));
    }

    @Override
    public FinalExpression visitIdentifier(IdentifierContext ctx) {
        final FinalExpression ret;
        if (ctx.basic_identifier() != null) {
            ret = visitBasicIdentifier(ctx);
        } else if (ctx.contextRef() != null) {
            ret = this.visit(ctx.contextRef());
        } else if (ctx.funcCall() != null) {
            ret = visitFunctionCall(ctx);
        } else if (ctx.arrayAccess() != null) {
            ret = visitArrayAccess(ctx);
        } else {
            throw new NotImplementedException(ctx + "identifier unknow.");
        }
        return ret;
    }

    private FinalExpression visitArrayAccess(IdentifierContext ctx) {
        Expression ret;
        final FinalExpression left = this.visit(ctx.arrayAccess());
        if (ctx.DOT() == null) {
            ret = new InstanceExpression(left.toText(), null, null, null);
        } else {
            ret = new IdentifierExpression(left, this.visit(ctx.identifier()));
        }
        return this.context.lookup(ret, FinalExpression.class);
    }

    private FinalExpression visitFunctionCall(IdentifierContext ctx) {
        Expression ret;
        final FinalExpression left = this.visit(ctx.funcCall());
        if (ctx.DOT() == null) {
            ret = left;
        } else {
            ret = new IdentifierExpression(left, this.visit(ctx.identifier()));
        }
        return this.context.lookup(ret, FinalExpression.class);
    }

    private FinalExpression visitBasicIdentifier(final IdentifierContext ctx) {
        final Expression ret;
        final BasicIdentifierExpression left = new BasicIdentifierExpression(ctx.basic_identifier().getText());
        final FinalExpression res = this.context.lookup(left, FinalExpression.class, false);
        if (ctx.DOT() == null) {
            /*if(res != null && res instanceof InstanceExpression) {
                ret = res;
            } else {
                ret = new InstanceExpression(new StringExpression(left.toPath()), null, null, null);
            }*/
            if (res != null) {
                ret = res;
            } else {
                ret = left;
            }
        } else {
            ret = this.context.lookup(new IdentifierExpression(res, this.visit(ctx.identifier())));
        }
        FinalExpression lookup = this.context.lookup(ret, FinalExpression.class, false);

        return lookup;
    }

    @Override
    public FinalExpression visitFuncCall(final FuncCallContext ctx) {
        final String functionName = ctx.basic_identifier().getText();
        final FunctionExpression functionExpression = this.context.lookupByStrKey(functionName, FunctionExpression.class);

        final Context functionContext = new Context(this.context);
        int parameterCount = 0;

        final List<ExpressionContext> parameters;
        if (ctx.parameters != null) {
            parameters = ctx.parameters.expression();
        } else {
            parameters = new ArrayList<>();
        }
        final int definedNbr = functionExpression.getParamtersSize();
        final int callerNbr = parameters.size();
        if (callerNbr != definedNbr) {
            throw new WrongNumberOfArguments(functionName, definedNbr, callerNbr);
        }
        for (final ExpressionContext expressionCtx : parameters) {
            final FinalExpression expression = this.visitExpression(expressionCtx);
            final String parameterName = functionExpression.getParameter(parameterCount++);
            functionContext.addExpression(parameterName, expression);
        }


        final KevscriptVisitor kevscriptVisitor = new KevscriptVisitor(functionContext);
        final Commands commands = new Commands();
        final FuncBodyContext functionBody = functionExpression.getFunctionBody();
        for (final StatementContext a : functionBody.statement()) {
            final Commands visit = kevscriptVisitor.visit(a);
            commands.addAll(visit);
        }

        final FinalExpression returnValue;
        if (functionBody.returnStatement() != null) {
            final ExpressionVisitor expressionVisitor = new ExpressionVisitor(kevscriptVisitor.getContext());
            final FinalExpression returnRes = expressionVisitor.visit(functionBody.returnStatement().expression());
            returnValue = returnRes;
        } else {
            returnValue = null;
        }

        this.aggregatedFunctionsCommands.addAll(commands);

        return returnValue;
    }

    @Override
    public InstancePathExpression visitInstancePath(InstancePathContext ctx) {
        final InstancePathExpression ret;
        if (ctx.identifier().size() == 1) {
            ret = new InstancePathExpression(this.helper.getInstanceExpressionFromContext(ctx.identifier(0)));
        } else {
            ret = new InstancePathExpression(this.helper.getInstanceExpressionFromContext(ctx.identifier(0)),
                    this.helper.getInstanceExpressionFromContext(ctx.identifier(1)));
        }

        return ret;
    }

    @Override
    public FinalExpression visitPortPath(PortPathContext ctx) {
        final FinalExpression ret;
        final FinalExpression visit = this.visit(ctx.identifier());
        if (ctx.instancePath() != null) {
            final String portName;
            if (visit != null) {
                portName = visit.toText();
            } else {
                portName = ctx.identifier().getText();
            }
            ret = new PortPathExpression(this.visitInstancePath(ctx.instancePath()), ctx.LEFT_LIGHT_ARROW() != null, portName);
        } else {
            ret = visit;
        }
        return ret;
    }

    @Override
    public VersionExpression visitVersion(VersionContext ctx) {
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


    @Override
    public DictionaryPathExpression visitDictionaryPath(DictionaryPathContext ctx) {
        final FinalExpression visit = this.visit(ctx.identifier(0));
        final String dicoName;
        if (visit != null) {
            if(visit instanceof StringExpression) {
                dicoName = visit.toText();
            } else {
                throw new WrongTypeException(ctx.identifier(0).getText(), StringExpression.class);
            }
        } else {
            dicoName = ctx.identifier(0).getText();
        }

        final String frag;
        if(ctx.identifier(1) != null) {
            frag = this.visit(ctx.identifier(1)).toText();
        } else {
            frag = null;
        }

        return new DictionaryPathExpression(this.visitInstancePath(ctx.key), dicoName, frag);
    }

    @Override
    public ArrayDeclExpression visitIterable(final IterableContext ctx) {
        final ArrayDeclExpression ret;
        if(ctx.arrayDecl() != null) {
            // TODO array decl
            ret = this.visitArrayDecl(ctx.arrayDecl());
        } else if(ctx.identifier() != null) {
            ret = this.context.lookup(this.visitIdentifier(ctx.identifier()), ArrayDeclExpression.class);
        } else {
            // TODO context reference
            ret = null;
        }
        return ret;
    }

    public Context getContext() {
        return context;
    }
}
