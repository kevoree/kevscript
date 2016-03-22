package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.excpt.InstanceNameNotFound;
import org.kevoree.kevscript.language.excpt.NameCollisionException;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.Expression;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mleduc on 02/03/16.
 */
public class Context {
    private final Map<String, Expression> mapIdentifiers;
    private final Map<String, KevScriptParser.FuncDeclContext> functions;

    public Context() {
        mapIdentifiers = new HashMap<>();
        functions = new HashMap<>();
    }

    public Context(Context rootContext) {
        this();
        mapIdentifiers.putAll(rootContext.getIdentifiers());
        functions.putAll(rootContext.getFunctions());
    }

    public Map<String, Expression> getIdentifiers() {
        return mapIdentifiers;
    }

    public Map<String, KevScriptParser.FuncDeclContext> getFunctions() {
        return this.functions;
    }


    public <T extends Expression> T lookup(final String identifier, final Class<T> clazz) {
        return lookup(identifier, clazz, true);
    }

    public <T extends Expression> T lookup(final String identifier, Class<T> clazz, boolean throwException) {
        if (this.mapIdentifiers.containsKey(identifier)) {
            final Expression expression = this.mapIdentifiers.get(identifier);
            //if(clazz != null && !expression.getClass().isAssignableFrom(clazz)) {
            if (clazz != null && !clazz.isAssignableFrom(expression.getClass())) {
                throw new WrongTypeException(identifier, clazz);
            }
            return (T) expression;
        } else if (throwException) {
            throw new InstanceNameNotFound(identifier);
        }
        return null;
    }


    public void addExpression(final String identifier, final Expression instanceExpression) {

        // TODO flatten object to be able to find every entry by on lookup
        if (this.mapIdentifiers.containsKey(identifier)) {
            throw new NameCollisionException(identifier);
        }
        this.mapIdentifiers.put(identifier, instanceExpression);
    }

}
