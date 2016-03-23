package org.kevoree.kevscript.language.visitors.helper;

import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.*;
import org.kevoree.kevscript.language.visitors.ExpressionVisitor;

/**
 * Created by mleduc on 22/03/16.
 */
public class KevscriptHelper {
    public Long convertVersionToLong(final Context context, final Expression expression) {

        final Long versionValue;
        if (expression instanceof VersionExpression) {
            versionValue = ((VersionExpression) expression).version;
        } else if (expression != null) {
            final FinalExpression version = context.lookup(expression, FinalExpression.class);
            if (version instanceof StringExpression) {
                versionValue = Long.parseLong(((StringExpression) version).text);
            } else if(version instanceof InstanceExpression) {
                final VersionExpression instanceTypeDefVersion = ((InstanceExpression) version).instanceTypeDefVersion;
                if(instanceTypeDefVersion != null) {
                    versionValue = instanceTypeDefVersion.version;
                }   else {
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

    public InstanceExpression getInstanceExpressionFromContext(final Context context, KevScriptParser.IdentifierContext node) {
        final FinalExpression nodeExpression = new ExpressionVisitor(context).visit(node);
        final InstanceExpression nodeInstance;
        if(nodeExpression instanceof  IdentifierExpression) {
            nodeInstance = (InstanceExpression) nodeExpression;
        } else if (nodeExpression instanceof  InstanceExpression) {
            nodeInstance = (InstanceExpression) nodeExpression;
        }else if(nodeExpression == null) {
            nodeInstance = new InstanceExpression(node.getText(), null, null, null);
        } else
        {
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

    public InstanceElement getInstanceFromContext(final Context context, KevScriptParser.IdentifierContext node) {
        final FinalExpression nodeExpression = new ExpressionVisitor(context).visit(node);
        final InstanceExpression nodeInstance = context.lookup(nodeExpression, InstanceExpression.class, false);
        final InstanceElement nodeInstanceElement;

        if (nodeInstance == null && node.DOT() == null && node.contextRef() == null) {
            nodeInstanceElement = new InstanceElement(node.basic_identifier().getText(), null, null);
        } else {
            final String nodeName = nodeInstance.instanceName;
            final Long nodeVersion = this.convertVersionToLong(context, nodeInstance.instanceTypeDefVersion);
            final String instanceTypeDefName = nodeInstance.instanceTypeDefName;
            nodeInstanceElement = new InstanceElement(nodeName, instanceTypeDefName, nodeVersion);
        }
        return nodeInstanceElement;
    }

    public String getPortNameFromIdentifier(final Context context, final Expression portNameIdentifier) {
        final StringExpression portNameExpr = context.lookup(portNameIdentifier, StringExpression.class);
        if(portNameExpr != null) {
            return portNameExpr.text;
        } else {
            return null;
        }
    }
}
