package org.kevoree.kevscript.language;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.kevoree.kevscript.language.commands.AddCommand;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.commands.InstanceCommand;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.expressions.finalexp.*;
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

    private final static InstanceExpression MODEL_ROOT = new InstanceExpression("/", null);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAdd0() throws Exception {
        final Commands commands = new Commands()
                .addCommand(new InstanceCommand("nodeName", new TypeExpression(null, "DotnetNode", new VersionExpression(1), null)))
                .addCommand(new AddCommand(MODEL_ROOT, new InstanceExpression("nodeName", null)));
        test(commands, "phase0/test1/new.kevs");
    }


    private void test(final Commands expected, final String kevscript) throws IOException {
        final String file = addTrailingSlash(kevscript);
        final String newStr = pathToString(file);

        final RootContext externalContext = new RootContext(pathToString(file));
        final ObjectDeclExpression expression = new ObjectDeclExpression();
        final ObjectDeclExpression value = new ObjectDeclExpression();
        value.put("name", new StringExpression("nodeName"));
        expression.put("node", value);
        externalContext.addExternalExpression("default", expression);

        assertEquals(expected, interpret(externalContext, newStr));
    }

    private String addTrailingSlash(final String file) {
        final String ret;
        if(file.startsWith("/")) {
           ret = file;
        } else {
            ret = "/" + file;
        }
        return ret;
    }


    private String pathToString(final String resource) throws IOException {
        final InputStream newKev = getClass().getResourceAsStream(resource);
        return IOUtils.toString(newKev);
    }

    private Commands interpret(final RootContext externalContext, final String expression) {
        return new KevscriptInterpreter().interpret(expression, new KevscriptVisitor(externalContext));
    }
}
