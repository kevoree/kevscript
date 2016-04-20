package org.kevoree.kevscript.language.visitors.helper;

import org.antlr.v4.runtime.ParserRuleContext;
import org.kevoree.kevscript.KevScriptParser.IdentifierBasicIdentifierContext;
import org.kevoree.kevscript.KevScriptParser.IdentifierListContext;
import org.kevoree.kevscript.KevScriptParser.InstancePathContext;
import org.kevoree.kevscript.KevScriptParser.ObjectDeclContext;
import org.kevoree.kevscript.language.KevscriptInterpreter;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.ResourceNotFoundException;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.finalexp.*;
import org.kevoree.kevscript.language.expressions.nonfinalexp.IdentifierExpression;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.kevoree.kevscript.KevScriptParser.IdentifierContext;

/**
 *
 *
 */
public class KevscriptHelper {
    private final Context context;
    private final UrlDownloader urlDownloader;

    public KevscriptHelper(Context context) {
        this.context = context;
        this.urlDownloader = new UrlDownloader();
    }

    public InstanceExpression processIdentifierAsInstance(final IdentifierContext ctx) {
        final FinalExpression instanceExpr = new ExpressionVisitor(this.context).visit(ctx);
        InstanceExpression instance;
        if (instanceExpr == null) {
            instance = new InstanceExpression(ctx.getText(), null);
        } else {
            instance = new InstanceExpression(instanceExpr.toText(), null);
        }

        return instance;
    }

    public ObjectDeclExpression getObjectByDeclOrIdentifier(final ObjectDeclContext objCtx, final IdentifierContext idCtx) {
        final ExpressionVisitor exprVisitor = new ExpressionVisitor(this.context);
        final ObjectDeclExpression object;
        if (objCtx != null) {
            // statement directly defines an object
            object = exprVisitor.visitObjectDecl(objCtx);
        } else {
            // statement refers to an object via a reference
            object = getObjectDeclExpression(idCtx);
        }

        return object;
    }

    public ObjectDeclExpression getObjectDeclExpression(final IdentifierContext ctx) {
        final ExpressionVisitor exprVisitor = new ExpressionVisitor(this.context);
        final FinalExpression objRef = exprVisitor.visit(ctx);
        final ObjectDeclExpression object;
        if (objRef instanceof ObjectDeclExpression) {
            object = (ObjectDeclExpression) objRef;
        } else if (objRef == null) {
            throw new WrongTypeException(ctx, ObjectDeclExpression.class, NullExpression.class);
        } else {
            throw new WrongTypeException(ctx, ObjectDeclExpression.class, objRef.getClass());
        }
        return object;
    }


    /**
     * Load the context for the required script.
     * Either from the import store if the same script had already been asked by another script
     * or interpret it and memoize the resulting context.
     *
     * @param resourcePath
     */
    public Map<String, FinalExpression> loadContext(final String resourcePath, final ImportsStore importsStore) {
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

    private String getScriptFromResourcePath(final String resourcePath, final String basePath) {
        final String pathText = resourcePath.substring(1, resourcePath.length() - 1);
        String res;
        try {
            final URL url = new URL(pathText);
            res = urlDownloader.saveUrl(url);
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

    public InstanceExpression getInstanceElement(final InstancePathContext instancePathContext) {

        final InstanceExpression ret;
        if (instancePathContext.identifier().size() == 1) {
            // direct reference to a component, must be in the current scope
            final InstanceExpression componentInstance = this.getInstanceFromIdentifierContext(instancePathContext.identifier(0));
            ret = componentInstance;

        } else {
            // reference to a component via its node, might be found in the context or later in the CDN
            final InstanceExpression nodeInstance = this.getInstanceFromIdentifierContext(instancePathContext.identifier(0));
            final InstanceExpression componentInstance = this.getInstanceFromIdentifierContext(instancePathContext.identifier(1));
            ret = new InstanceExpression(nodeInstance.instanceName + ":" + componentInstance.instanceName, componentInstance.typeExpr);
        }
        return ret;
    }

    public InstanceExpression getInstanceFromIdentifierContext(final IdentifierContext node) {
        final FinalExpression nodeExpression = new ExpressionVisitor(context).visit(node);
        final InstanceExpression nodeInstance = context.lookup(nodeExpression, InstanceExpression.class, false, node);
        final InstanceExpression ret;

        if ((nodeInstance == null) && (node instanceof IdentifierBasicIdentifierContext)) {
            ret = new InstanceExpression(((IdentifierBasicIdentifierContext) node).basicIdentifier().getText(), null);
        } else {
            final String nodeName = nodeInstance.instanceName;
            final Long nodeVersion = this.convertVersionToLong(nodeInstance.typeExpr.versionExpr, node);
            final String instanceTypeDefName = nodeInstance.typeExpr.name;
            final VersionExpression versionExpr;
            if (nodeVersion != null) {
                versionExpr = new VersionExpression(nodeVersion);
            } else {
                versionExpr = null;
            }
            final TypeExpression typeExpr = new TypeExpression(null, instanceTypeDefName, versionExpr, null);
            ret = new InstanceExpression(nodeName, typeExpr);
        }
        return ret;
    }

    public Long convertVersionToLong(final Expression expression, final ParserRuleContext ctx) {

        final Long versionValue;
        if (expression instanceof VersionExpression) {
            versionValue = ((VersionExpression) expression).version;
        } else if (expression != null) {
            final FinalExpression version = context.lookup(expression, FinalExpression.class, ctx);
            if (version instanceof StringExpression) {
                versionValue = Long.parseLong(((StringExpression) version).text);
            } else if (version instanceof InstanceExpression) {
                final VersionExpression instanceTypeDefVersion = ((InstanceExpression) version).typeExpr.versionExpr;
                if (instanceTypeDefVersion != null) {
                    versionValue = instanceTypeDefVersion.version;
                } else {
                    versionValue = null;
                }
            } else {
                throw new WrongTypeException(ctx, FinalExpression.class, null);
            }
        } else {
            versionValue = null;
        }
        return versionValue;
    }

    public InstanceExpression getInstanceExpressionFromContext(final IdentifierContext node) {
        final FinalExpression nodeExpression = new ExpressionVisitor(context).visit(node);
        final InstanceExpression nodeInstance;
        if (nodeExpression instanceof IdentifierExpression) {
            nodeInstance = (InstanceExpression) nodeExpression;
        } else if (nodeExpression instanceof InstanceExpression) {
            nodeInstance = (InstanceExpression) nodeExpression;
        } else if (nodeExpression == null) {
            nodeInstance = new InstanceExpression(node.getText(), null);
        } else {
            throw new WrongTypeException(node, InstanceExpression.class, null);
        }
        final InstanceExpression nodeInstanceExpression;
        if (nodeInstance == null && node instanceof IdentifierBasicIdentifierContext) {
            nodeInstanceExpression = new InstanceExpression(((IdentifierBasicIdentifierContext) node).basicIdentifier().getText(), null);
        } else {
            nodeInstanceExpression = nodeInstance;
        }
        return nodeInstanceExpression;
    }

    public List<String> getListObjectRefs(final IdentifierListContext identifierListContext, final IdentifierContext identifierContext) {
        final List<String> objectRefs = new ArrayList<>();
        if (identifierListContext != null) {
            for (final IdentifierContext identifier : identifierListContext.identifier()) {
                objectRefs.add(identifier.getText());
            }
        } else {
            objectRefs.add(identifierContext.getText());
        }
        return objectRefs;
    }

}
