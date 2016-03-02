import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kevoree.kevscript.KevScriptLexer;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.KevscriptVisitor;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by mleduc on 02/03/16.
 */
public class TestCases {

    @Test
    public void testA()  throws Exception {
        analyzeDirectory("add_0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testB() throws Exception {
        interpret(pathToString("/add_1/new.kevs"));
    }

    private void analyzeDirectory(String add_0) throws IOException {
        final String newStr = pathToString("/" + add_0 + "/new.kevs");
        final String oldStr = pathToString("/" + add_0 + "/old.kevs");
        assertEquals(oldStr.trim(), interpret(newStr).trim());
    }

    private String pathToString(String name1) throws IOException {
        final InputStream newKev =  getClass().getResourceAsStream(name1);
        return IOUtils.toString(newKev);
    }

    private String interpret(String expression) {
        final KevScriptLexer lexer = new KevScriptLexer(new ANTLRInputStream(expression));
        final KevScriptParser parser = new KevScriptParser(new CommonTokenStream(lexer));
        final ParseTree tree = parser.script();
        return new KevscriptVisitor().visit(tree);
    }
}
