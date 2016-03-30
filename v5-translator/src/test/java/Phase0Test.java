import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.kevoree.kevscript.KevScriptLexer;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.expressions.finalexp.ObjectDeclExpression;
import org.kevoree.kevscript.language.expressions.finalexp.StringExpression;
import org.kevoree.kevscript.language.listener.DescriptiveErrorListener;
import org.kevoree.kevscript.language.processor.CommandsToString;
import org.kevoree.kevscript.language.visitors.KevscriptVisitor;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Preconditions :
 * - grammatically valid script
 * <p>
 * Controls :
 * - local variable names collision
 * - does not throw error on missing instance names (this will be check on a next phase)
 */
public class Phase0Test {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAdd0() throws Exception {
        analyzeDirectory("phase0/test1");
    }



    private void analyzeDirectory(String add_0) throws IOException {
        final String newStr = pathToString("/" + add_0 + "/new.kevs");
        final String oldStr = pathToString("/" + add_0 + "/old.kevs");

        final RootContext externalContext = new RootContext();
        final ObjectDeclExpression expression = new ObjectDeclExpression();
        final ObjectDeclExpression value = new ObjectDeclExpression();
        value.put("name", new StringExpression("nodeName"));
        expression.put("node", value);
        externalContext.addExternalExpression("default", expression);
        assertEquals(oldStr.trim(), interpretPhase1(externalContext, newStr).trim());
    }



    private String pathToString(String name1) throws IOException {
        final InputStream newKev = getClass().getResourceAsStream(name1);
        return IOUtils.toString(newKev);
    }

    private String interpretPhase1(final RootContext externalContext, final String expression) {
        final KevScriptLexer lexer = new KevScriptLexer(new ANTLRInputStream(expression));
        final KevScriptParser parser = new KevScriptParser(new CommonTokenStream(lexer));
        lexer.removeErrorListeners();
        lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);
        parser.removeErrorListeners();
        parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
        final ParseTree tree = parser.script();
        return new CommandsToString().proceed(new KevscriptVisitor(externalContext).visit(tree));
    }
}
