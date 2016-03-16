package org.kevoree.kevscript.language.context;

import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.expressions.Expression;

import java.util.List;
import java.util.Map;

/**
 * Created by mleduc on 02/03/16.
 */
public abstract class Context {
    public abstract List<Expression> getIdentifiers();
    public abstract Map<String, KevScriptParser.FuncDeclContext> getFunctions();
}
