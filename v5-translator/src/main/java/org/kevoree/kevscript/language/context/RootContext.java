package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.expressions.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mleduc on 02/03/16.
 */
public class RootContext extends Context {
    private final List<Expression> mapIdentifiers;
    private final Map<String, KevScriptParser.FuncDeclContext> functions;

    public RootContext() {
        mapIdentifiers = new ArrayList<>();
        functions = new HashMap<>();
    }

    public RootContext(Context rootContext) {
        this();
        mapIdentifiers.addAll(rootContext.getIdentifiers());
        functions.putAll(rootContext.getFunctions());
    }

    @Override
    public List<Expression> getIdentifiers() {
        return mapIdentifiers;
    }

    @Override
    public Map<String, KevScriptParser.FuncDeclContext> getFunctions() {
        return this.functions;
    }
}
