package org.kevoree.kevscript.language.visitors;

import org.apache.commons.lang3.NotImplementedException;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.expressions.*;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 02/03/16.
 */
public class ExpressionVisitor extends KevScriptBaseVisitor<Expression> {
    private final Context context;

    public ExpressionVisitor(final Context context) {
        this.context = context;
    }

    @Override
    public Expression visitExpression(ExpressionContext ctx) {
        final Expression ret;
        if (ctx.CONCAT() != null) {
            ret = new ConcatExpression(this.visit(ctx.expression(0)), this.visit(ctx.expression(1)));
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
    public ContextIdentifierExpression visitContextIdentifier(ContextIdentifierContext ctx) {
        final ContextIdentifierExpression ret = new ContextIdentifierExpression();
        if (ctx.ID() != null) {
            ret.add(new StringExpression(ctx.ID().getText()));
        } else if (ctx.contextRef() != null) {
            ret.add(this.visit(ctx.contextRef()));
        } else if (ctx.arrayAccess() != null) {
            ret.add(this.visit(ctx.arrayAccess()));
        } else if (ctx.DOT() != null) {
            ret.add(this.visit(ctx.contextIdentifier(0)));
            ret.add(this.visit(ctx.contextIdentifier(1)));
        }
        return ret;
    }


    @Override
    public ArrayDeclExpression visitArrayDecl(ArrayDeclContext ctx) {
        final ArrayDeclExpression ret = new ArrayDeclExpression();
        if(ctx.expressionList() != null) {
            for (ExpressionContext expression : ctx.expressionList().expression()) {
                ret.add(this.visit(expression));
            }
        }
        return ret;
    }

    @Override
    public ArrayAccessExpression visitArrayAccess(ArrayAccessContext ctx) {
        return new ArrayAccessExpression(ctx.ID().getText(), Long.parseLong(ctx.NUMERIC_VALUE().getText()));
    }

    @Override
    public Expression visitIdentifier(IdentifierContext ctx) {
        final Expression ret;
        if(ctx.ID() != null) {
            final StringExpression left = new StringExpression(ctx.ID().getText());
            if(ctx.DOT() == null) {
                ret = new IdentifierExpression(left);
            } else {
                ret = new IdentifierExpression(left, this.visit(ctx.identifier()));
            }
        } else if(ctx.contextRef() != null) {
            ret = this.visit(ctx.contextRef());
        } else if(ctx.funcCall() != null) {
            final Expression left = this.visit(ctx.funcCall());
            if(ctx.DOT() == null) {
                ret = new IdentifierExpression(left);
            } else {
                ret = new IdentifierExpression(left, this.visit(ctx.identifier()));
            }
        } else if (ctx.arrayAccess() != null) {
            final Expression left = this.visit(ctx.arrayAccess());
            if(ctx.DOT() == null) {
                ret = new IdentifierExpression(left);
            } else {
                ret = new IdentifierExpression(left, this.visit(ctx.identifier()));
            }
        } else {
            throw  new NotImplementedException(ctx + "identifier unknow.");
        }
        return ret;
    }

    @Override
    public FunctionCallExpression visitFuncCall(FuncCallContext ctx) {
        final FunctionCallExpression ret = new FunctionCallExpression(ctx.AT() != null, ctx.ID().getText());
        if(ctx.parameters != null && ctx.parameters.expression() != null) {
            for (final ExpressionContext param : ctx.parameters.expression()) {
                ret.add(this.visit(param));
            }
        }
        return ret;
    }

    @Override
    public Expression visitInstancePath(InstancePathContext ctx) {
        final InstancePathExpression ret = new InstancePathExpression();
        for(IdentifierContext identifier : ctx.identifier()) {
            ret.add(this.visit(identifier));
        }
        return ret;
    }

    @Override
    public Expression visitPortPath(PortPathContext ctx) {
        final Expression ret;
        if(ctx.instancePath() != null) {
            ret = new PortPathExpression(this.visit(ctx.instancePath()), ctx.LEFT_LIGHT_ARROW() != null, this.visit(ctx.identifier()));
        } else {
            ret = this.visit(ctx.identifier());
        }
        return ret;
    }

    @Override
    public Expression visitVersion(VersionContext ctx) {
        final Expression ret;
        if(ctx.NUMERIC_VALUE() != null) {
            ret = new VersionExpression(Long.parseLong(ctx.NUMERIC_VALUE().getText()));
        } else {
            ret = this.visit(ctx.identifier());
        }
        return ret;
    }
}
