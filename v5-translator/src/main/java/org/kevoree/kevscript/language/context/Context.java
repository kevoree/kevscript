package org.kevoree.kevscript.language.context;

import org.apache.commons.lang3.math.NumberUtils;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.excpt.InstanceNameNotFound;
import org.kevoree.kevscript.language.excpt.NameCollisionException;
import org.kevoree.kevscript.language.excpt.VersionNotFound;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.InstanceExpression;
import org.kevoree.kevscript.language.expressions.StringExpression;
import org.kevoree.kevscript.language.expressions.VersionExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mleduc on 02/03/16.
 */
public class Context {
    private final List<Expression> mapIdentifiers;
    private final Map<String, KevScriptParser.FuncDeclContext> functions;

    public Context() {
        mapIdentifiers = new ArrayList<>();
        functions = new HashMap<>();
    }

    public Context(Context rootContext) {
        this();
        mapIdentifiers.addAll(rootContext.getIdentifiers());
        functions.putAll(rootContext.getFunctions());
    }

    public List<Expression> getIdentifiers() {
        return mapIdentifiers;
    }

    public Map<String, KevScriptParser.FuncDeclContext> getFunctions() {
        return this.functions;
    }

    public Expression lookup(final Expression identifier) {
        return lookup(identifier, null);
    }

    public <T extends Expression> T lookup(final Expression identifier, final Class<T> expectedClass) {
        for(final Expression expression: this.getIdentifiers()) {
            if(expression.match(identifier)) {
                if(expectedClass != null && !expression.getClass().isAssignableFrom(expectedClass)) {
                    throw new WrongTypeException(identifier.getName(), expression);
                }
                return (T) expression;
            }
        }
        return null;
    }

    public StringExpression lookupString(final Expression identifier) {
        if(identifier instanceof StringExpression) {
            return (StringExpression) identifier;
        }
        for(final Expression expression: this.getIdentifiers()) {
            if(expression.match(identifier)) {
                if(!(expression instanceof StringExpression)) {
                    continue;
                }
                return (StringExpression) expression;
            }
        }
        throw new InstanceNameNotFound(identifier);
    }

    public VersionExpression lookupVersion(final Expression identifier) {
        if(identifier instanceof VersionExpression) {
            return (VersionExpression) identifier;
        }
        for(final Expression expression: this.getIdentifiers()) {
            if(expression.match(identifier)) {
                if(expression instanceof VersionExpression) {
                    return (VersionExpression) expression;
                } else if(expression instanceof  StringExpression) {
                    final String stringExpr = ((StringExpression) expression).text;
                    if(NumberUtils.isDigits(stringExpr)) {
                        return new VersionExpression(Long.parseLong(stringExpr));
                    }
                }
            }
        }
        throw new VersionNotFound(identifier);
    }

    public void addExpression(Expression instanceExpression) {
        if(this.lookup(new StringExpression(instanceExpression.getName())) != null) {
            throw new NameCollisionException(instanceExpression.getName());
        }
        this.mapIdentifiers.add(instanceExpression);
    }

}
