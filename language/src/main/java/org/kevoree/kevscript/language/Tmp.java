package org.kevoree.kevscript.language;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.kevoree.kevscript.KevScriptLexer;
import org.kevoree.kevscript.KevScriptParser;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 29/02/16.
 */
public class Tmp {
    public static void main(String args[]) {
        final String input = "add node0: JavaNode\n" +
                "add node1, node2: JavaNode/1 {}";
        final KevScriptLexer lexer = new KevScriptLexer(new ANTLRInputStream(input));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final KevScriptParser parser = new KevScriptParser(tokens);
        final ScriptContext kevscriptContext = parser.script();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final KevscriptListener listener = new KevscriptListener();
        walker.walk(listener, kevscriptContext);
    }
}
