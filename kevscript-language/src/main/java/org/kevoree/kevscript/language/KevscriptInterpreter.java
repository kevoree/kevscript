package org.kevoree.kevscript.language;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.kevoree.kevscript.KevScriptLexer;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.listener.DescriptiveErrorListener;
import org.kevoree.kevscript.language.visitors.KevscriptVisitor;

/**
 * Created by mleduc on 30/03/16.
 */
public class KevscriptInterpreter {
    public Commands interpreter(final String expression, final String basePath) {
        return this.interpret(expression, new KevscriptVisitor(new RootContext(basePath)));
    }

    public Commands interpret(final String expression, final KevscriptVisitor kevscriptVisitor) {
        final KevScriptLexer lexer = new KevScriptLexer(new ANTLRInputStream(expression));
        final KevScriptParser parser = new KevScriptParser(new CommonTokenStream(lexer));
        lexer.removeErrorListeners();
        lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);
        parser.removeErrorListeners();
        parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
        final ParseTree tree = parser.script();
        return kevscriptVisitor.visit(tree);
    }
}
