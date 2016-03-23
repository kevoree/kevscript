package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.language.excpt.InstanceNameNotFound;
import org.kevoree.kevscript.language.excpt.NameCollisionException;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mleduc on 02/03/16.
 */
public class Context {
    private final Map<String, FinalExpression> mapIdentifiers;

    public Context() {
        mapIdentifiers = new HashMap<>();
    }

    public Context(Context rootContext) {
        this();
        mapIdentifiers.putAll(rootContext.getIdentifiers());
    }

    public Map<String, FinalExpression> getIdentifiers() {
        return mapIdentifiers;
    }

    public FinalExpression lookup(final Expression identifier) {
        return lookup(identifier, FinalExpression.class);
    }

    public <T extends FinalExpression> T lookup(final Expression identifier, final Class<T> clazz) {
        return lookup(identifier, clazz, true);
    }

    public <T extends FinalExpression> T lookupByStrKey(final String identifier, final Class<T> clazz) {
        return lookupByStrKey(identifier, clazz, true);
    }

    public <T extends FinalExpression> T lookup(final Expression identifier, Class<T> clazz, boolean throwException) {
        final T ret;
        if (identifier instanceof FinalExpression) {
            ret = (T) identifier;
        } else if (identifier != null) {
            ret = lookupByStrKey(((NonFinalExpression) identifier).toPath(), clazz, throwException);
        } else {
            ret = null;
        }
        return ret;
    }

    private <T extends FinalExpression> T lookupByStrKey(String key, Class<T> clazz, boolean throwException) {
        if (this.mapIdentifiers.containsKey(key)) {
            final FinalExpression expression = this.mapIdentifiers.get(key);
            if (clazz != null && !clazz.isAssignableFrom(expression.getClass())) {
                throw new WrongTypeException(key, clazz);
            }
            return (T) expression;
        } else if (throwException) {
            throw new InstanceNameNotFound(key);
        }
        return null;
    }

    public void addExpression(final String identifier, final FinalExpression expression) {

        if (expression instanceof ArrayDeclExpression) {
            final ArrayDeclExpression arr = (ArrayDeclExpression) expression;
            int i = 0;
            for (final FinalExpression nx : arr.expressionList) {
                addExpression(identifier + "[" + (i++) + "]", nx);
            }
        } else if (expression instanceof ObjectDeclExpression) {
            final ObjectDeclExpression obj = (ObjectDeclExpression) expression;
            for (Map.Entry<String, FinalExpression> x : obj.values.entrySet()) {
                addExpression(identifier + "." + x.getKey(), x.getValue());
            }
        }
        basicAddExpression(identifier, expression);
    }

    private void basicAddExpression(String identifier, FinalExpression expression) {
        if (this.mapIdentifiers.containsKey(identifier)) {
            throw new NameCollisionException(identifier);
        }
        this.mapIdentifiers.put(identifier, expression);
    }

}
