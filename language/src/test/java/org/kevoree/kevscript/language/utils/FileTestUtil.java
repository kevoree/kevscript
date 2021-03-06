package org.kevoree.kevscript.language.utils;

import org.apache.commons.io.IOUtils;
import org.kevoree.kevscript.language.commands.Commands;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.expressions.finalexp.InstanceExpression;
import org.kevoree.kevscript.language.visitors.KevScriptVisitor;
import org.kevoree.kevscript.language.visitors.helper.KevScriptHelper;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.IOException;
import java.io.InputStream;

import static java.io.File.separatorChar;

/**
 *
 *
 */
public class FileTestUtil {

    public final static InstanceExpression MODEL_ROOT = new InstanceExpression("/");

    public String pathToString(String name1) throws IOException {
        final InputStream newKev = getClass().getResourceAsStream(name1);
        return IOUtils.toString(newKev);
    }

    public Commands interpretPhase1(String expression) {
        return this.interpretPhase1(null, expression);
    }

    public Commands interpretPhase1(final String basePath, final String expression) {
        return KevScriptHelper.interpret(expression, new KevScriptVisitor(new RootContext(basePath)));
    }

    private void validate(final Commands expected, final String dirPath, final String fileContent) {
        ReflectionAssert.assertReflectionEquals(expected, interpretPhase1(dirPath, fileContent));
    }

    public void validateFile(final Commands expected, final String dirPath, final String fileName) throws IOException {
        final String filePath = dirPath + separatorChar + fileName;
        final String fileContent = this.pathToString(filePath);
        this.validate(expected, dirPath, fileContent);
    }

    public void analyzeDirectory(final Commands expected, final String path) throws IOException {
        final String pathB;
        if (path.startsWith("/")) {
            pathB = path.substring(1);
        } else {
            pathB = path;
        }

        final String basePathStr = "/" + pathB;
        final String filePath = basePathStr + "/new.kevs";
        final String dirPath = getClass().getResource(basePathStr).getPath();
        final String fileContent = this.pathToString(filePath);
        this.validate(expected, dirPath, fileContent);
    }
}
