package kevscript;

import org.antlr.v4.runtime.*;
import org.junit.Test;
import org.kevoree.kevscript.KevScriptLexer;
import org.kevoree.kevscript.KevScriptParser;

import java.io.InputStream;

public class WrongKevScriptTest {

    private void tester(final String str) throws IllegalStateException {
        KevScriptLexer l = new KevScriptLexer(new ANTLRInputStream(str));
        KevScriptParser p = new KevScriptParser(new CommonTokenStream(l));
        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + ":" + charPositionInLine + " due to " + msg, e);
            }
        });
        p.script();
    }

    @Test(expected = IllegalStateException.class)
    public void testInstanceListNamed() throws IllegalStateException {
        tester("instance a, b = \"boum\" T");
    }

    @Test(expected = IllegalStateException.class)
    public void testFuncDeclWithArrayDecl() throws IllegalStateException {
        tester("function decl([]) {}");
    }

    @Test(expected = IllegalStateException.class)
    public void testFuncDeclWithDottedIdentifier() throws IllegalStateException {
        tester("function decl(must.throw) {}");
    }

    @Test(expected = IllegalStateException.class)
    public void testFuncDeclWithObjectDecl() throws IllegalStateException {
        tester("function decl({}) {}");
    }

    @Test(expected = IllegalStateException.class)
    public void testFuncDeclWithFuncCall() throws IllegalStateException {
        tester("function decl(random()) {}");
    }

    @Test(expected = IllegalStateException.class)
    public void testFuncDeclWithFuncCallWithParams() throws IllegalStateException {
        tester("function decl(myFunc(\"nasty\")) {}");
    }

    @Test(expected = IllegalStateException.class)
    public void testFuncBodyMultipleReturn() throws IllegalStateException {
        tester("function decl() {\n  return \"one\"\n  return \"two\"\n}");
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveStringArray() throws IllegalStateException {
        tester("remove [\"should\", \"not\", \"work\"]");
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveObject() throws IllegalStateException {
        tester("remove {}");
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveString() throws IllegalStateException {
        tester("remove \"\"");
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveFunc() throws IllegalStateException {
        tester("remove");
    }

    @Test(expected = IllegalStateException.class)
    public void testFuncDeclInObjectDecl() throws IllegalStateException {
        tester("let wrong = { func: function () {} }");
    }

    @Test(expected = IllegalStateException.class)
    public void testArrayInArrayNetRemove() throws IllegalStateException {
        tester("net-remove wrong [[a]]");
    }

    @Test(expected = IllegalStateException.class)
    public void testAttachMultipleNodes() throws IllegalStateException {
        tester("attach a [b, c]");
    }

    @Test(expected = IllegalStateException.class)
    public void testDetachMultipleNodes() throws IllegalStateException {
        tester("detach a b");
    }

    @Test(expected = IllegalStateException.class)
    public void testReattachMultipleNodes() throws IllegalStateException {
        tester("reattach a [b, c]");
    }
}