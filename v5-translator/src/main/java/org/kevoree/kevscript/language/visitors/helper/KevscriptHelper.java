package org.kevoree.kevscript.language.visitors.helper;

import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.KevscriptInterpreter;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.ResourceNotFoundException;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.finalexp.*;
import org.kevoree.kevscript.language.utils.StringUtils;
import org.kevoree.kevscript.language.utils.UrlDownloader;
import org.kevoree.kevscript.language.visitors.ExpressionVisitor;
import org.kevoree.kevscript.language.visitors.ImportsStore;
import org.kevoree.kevscript.language.visitors.KevscriptVisitor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import static org.kevoree.kevscript.KevScriptParser.IdentifierContext;

/**
 *
 *
 */
public class KevscriptHelper {
    private final Context context;

    public KevscriptHelper(Context context) {
        this.context = context;
    }

    public InstanceExpression processIdentifierAsInstance(IdentifierContext ctx) {
        InstanceExpression instance;
        FinalExpression instanceExpr = new ExpressionVisitor(this.context).visitIdentifier(ctx);
        if (instanceExpr == null) {
            instance = new InstanceExpression(ctx.getText(), null);
        } else {
            instance = new InstanceExpression(instanceExpr.toText(), null);
        }

        return instance;
    }

    public ObjectDeclExpression getObjectByDeclOrIdentifier(KevScriptParser.ObjectDeclContext objCtx, IdentifierContext idCtx) {
        final ExpressionVisitor exprVisitor = new ExpressionVisitor(this.context);
        ObjectDeclExpression object;
        if (objCtx != null) {
            // statement directly defines an object
            object = exprVisitor.visitObjectDecl(objCtx);
        } else {
            // statement refers to an object via a reference
            object = getObjectDeclExpression(idCtx);
        }

        return object;
    }

    public ObjectDeclExpression getObjectDeclExpression(IdentifierContext ctx) {
        final ExpressionVisitor exprVisitor = new ExpressionVisitor(this.context);
        ObjectDeclExpression object;
        FinalExpression objRef = exprVisitor.visitIdentifier(ctx);
        if (objRef instanceof ObjectDeclExpression) {
            object = (ObjectDeclExpression) objRef;
        } else {
            if (objRef == null) {
                throw new WrongTypeException(ctx.getText(), ObjectDeclExpression.class, NullExpression.class);
            } else {
                throw new WrongTypeException(ctx.getText(), ObjectDeclExpression.class, objRef.getClass());
            }
        }
        return object;
    }

    public int convertArrayAccessToInt(KevScriptParser.ArrayAccessContext ctx) {
        return Integer.valueOf(ctx.NUMERIC_VALUE().getText());
    }

    /**
     * Load the context for the required script.
     * Either from the import store if the same script had already been asked by another script
     * or interpret it and memoize the resulting context.
     *
     * @param resourcePath
     */
    public Map<String, FinalExpression> loadContext(String resourcePath, ImportsStore importsStore) {
        final Map<String, FinalExpression> importedContext;
        if (importsStore.containsKey(resourcePath)) {
            importedContext = importsStore.get(resourcePath);
        } else {
            final String res = getScriptFromResourcePath(resourcePath, this.context.getBasePath());
            final KevscriptVisitor kevscriptVisitor = new KevscriptVisitor(importsStore, this.context.getBasePath());
            new KevscriptInterpreter().interpret(res, kevscriptVisitor);
            importedContext = kevscriptVisitor.getContext().getLocalyExportedContext();
            importsStore.put(resourcePath, importedContext);
        }
        return importedContext;
    }

    private String getScriptFromResourcePath(final String resourcePath, String basePath) {
        final String pathText = resourcePath.substring(1, resourcePath.length() - 1);
        String res;
        try {
            final URL url = new URL(pathText);
            res = new UrlDownloader().saveUrl(url);
        } catch (MalformedURLException e) {
            final File file = new File(new File(basePath), pathText);
            if (file.exists()) {
                try {
                    res = StringUtils.join(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8), "\n");
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                throw new ResourceNotFoundException(pathText);
            }
        } catch (IOException e) {
            throw new ResourceNotFoundException(pathText);
        }
        return res;
    }
}
