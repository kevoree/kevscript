package org.kevoree.kevscript.language.visitors.helper;

import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.*;
import org.kevoree.kevscript.language.visitors.ExpressionVisitor;

import static org.kevoree.kevscript.KevScriptParser.IdentifierContext;

/**
 * Created by mleduc on 22/03/16.
 */
public class KevscriptHelper {
    private final Context context;

    public KevscriptHelper(Context context) {
        this.context = context;
    }

    public Long convertVersionToLong(final Expression expression) {

        final Long versionValue;
        if (expression instanceof VersionExpression) {
            versionValue = ((VersionExpression) expression).version;
        } else if (expression != null) {
            final FinalExpression version = context.lookup(expression, FinalExpression.class);
            if (version instanceof StringExpression) {
                versionValue = Long.parseLong(((StringExpression) version).text);
            } else if (version instanceof InstanceExpression) {
                final VersionExpression instanceTypeDefVersion = ((InstanceExpression) version).instanceTypeDefVersion;
                if (instanceTypeDefVersion != null) {
                    versionValue = instanceTypeDefVersion.version;
                } else {
                    versionValue = null;
                }
            } else {
                throw new WrongTypeException(expression.toString(), FinalExpression.class);
            }
        } else {
            versionValue = null;
        }
        return versionValue;
    }

    public InstanceExpression getInstanceExpressionFromContext(IdentifierContext node) {
        final FinalExpression nodeExpression = new ExpressionVisitor(context).visit(node);
        final InstanceExpression nodeInstance;
        if (nodeExpression instanceof IdentifierExpression) {
            nodeInstance = (InstanceExpression) nodeExpression;
        } else if (nodeExpression instanceof InstanceExpression) {
            nodeInstance = (InstanceExpression) nodeExpression;
        } else if (nodeExpression == null) {
            nodeInstance = new InstanceExpression(node.getText(), null, null, null);
        } else {
            throw new WrongTypeException(node.getText(), InstanceExpression.class);
        }
        final InstanceExpression nodeInstanceExpression;

        if (nodeInstance == null && node.DOT() == null && node.contextRef() == null) {
            nodeInstanceExpression = new InstanceExpression(node.basic_identifier().getText(), null, null, null);
        } else {
            nodeInstanceExpression = nodeInstance;
        }
        return nodeInstanceExpression;
    }

    public InstanceElement getInstanceFromContext(final IdentifierContext node) {
        final FinalExpression nodeExpression = new ExpressionVisitor(context).visit(node);
        final InstanceExpression nodeInstance = context.lookup(nodeExpression, InstanceExpression.class, false);
        final InstanceElement nodeInstanceElement;

        if (nodeInstance == null && node.DOT() == null && node.contextRef() == null) {
            nodeInstanceElement = new InstanceElement(node.basic_identifier().getText(), null, null);
        } else {
            final String nodeName = nodeInstance.instanceName;
            final Long nodeVersion = this.convertVersionToLong(nodeInstance.instanceTypeDefVersion);
            final String instanceTypeDefName = nodeInstance.instanceTypeDefName;
            nodeInstanceElement = new InstanceElement(nodeName, instanceTypeDefName, nodeVersion);
        }
        return nodeInstanceElement;
    }

    public String getPortNameFromIdentifier(final Expression portNameIdentifier) {
        final StringExpression portNameExpr = context.lookup(portNameIdentifier, StringExpression.class);
        if (portNameExpr != null) {
            return portNameExpr.text;
        } else {
            return null;
        }
    }

    public InstanceElement convertPortPathToComponentElement(final InstancePathExpression instancePath) {
        final InstanceExpression componentExpression = context.lookup(instancePath.component, InstanceExpression.class, false);
        final InstanceElement component;
        if (componentExpression != null) {
            component = new InstanceElement(componentExpression.instanceName, componentExpression.instanceTypeDefName, this.convertVersionToLong(componentExpression.instanceTypeDefVersion));
        } else {
            component = new InstanceElement(context.lookup(instancePath.node, FinalExpression.class).toText());
        }
        return component;
    }

    public InstanceElement convertPortPathToNodeElement(final InstancePathExpression instancePath) {
        final InstanceExpression nodeExpression = context.lookup(instancePath.node, InstanceExpression.class, false);
        final InstanceElement node;
        if (nodeExpression != null) {
            node = new InstanceElement(nodeExpression.instanceName, nodeExpression.instanceTypeDefName, this.convertVersionToLong(nodeExpression.instanceTypeDefVersion));
        } else if (instancePath.node != null) {
            node = new InstanceElement(this.context.lookup(instancePath.node, FinalExpression.class).toText());
        } else {
            node = null;
        }
        return node;
    }

    public String getPortPathFromIdentifier(final IdentifierContext identifier) {
        final Expression portNameIdentifier = new ExpressionVisitor(context).visit(identifier);
        final String portNameFromIdentifier = this.getPortNameFromIdentifier(portNameIdentifier);
        final String ret;
        if (portNameFromIdentifier == null) {
            ret = identifier.getText();
        } else {
            ret = portNameFromIdentifier;
        }
        return ret;
    }

}
