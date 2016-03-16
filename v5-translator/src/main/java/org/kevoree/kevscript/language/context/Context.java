package org.kevoree.kevscript.language.context;

import org.apache.commons.lang3.math.NumberUtils;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.InstanceExpression;
import org.kevoree.kevscript.language.expressions.StringExpression;
import org.kevoree.kevscript.language.expressions.VersionExpression;

import java.util.List;
import java.util.Map;

/**
 * Created by mleduc on 02/03/16.
 */
public abstract class Context {
    public abstract List<Expression> getIdentifiers();
    public abstract Map<String, KevScriptParser.FuncDeclContext> getFunctions();

    public InstanceExpression lookupInstanceIdentifier(final Expression identifier) {
        for(final Expression expression: this.getIdentifiers()) {
            if(expression.match(identifier)) {
                if(!(expression instanceof InstanceExpression)) {
                    continue;
                }
                return (InstanceExpression) expression;
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
        return null;
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
        return null;
    }
}
