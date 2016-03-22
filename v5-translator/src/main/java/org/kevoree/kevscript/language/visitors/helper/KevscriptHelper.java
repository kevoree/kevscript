package org.kevoree.kevscript.language.visitors.helper;

import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.InstanceExpression;
import org.kevoree.kevscript.language.expressions.StringExpression;
import org.kevoree.kevscript.language.expressions.VersionExpression;
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
            final Expression version = context.lookup(expression.toText(), Expression.class);
            if (version instanceof VersionExpression) {
                versionValue = ((VersionExpression) version).version;
            } else if (version instanceof StringExpression) {
                versionValue = Long.parseLong(((StringExpression) version).text);
            } else {
                throw new WrongTypeException(expression.toText(), VersionExpression.class);
            }
        } else {
            versionValue = null;
        }
        return versionValue;
    }

    public InstanceElement getInstanceFromContext(final Context context, KevScriptParser.IdentifierContext node) {
        final Expression nodeExpression = new ExpressionVisitor(context).visit(node);
        final InstanceExpression nodeInstance = context.lookup(nodeExpression.toText(), InstanceExpression.class, false);
        final InstanceElement nodeInstanceElement;

        if (nodeInstance == null && node.DOT() == null && node.contextRef() == null) {
            nodeInstanceElement = new InstanceElement(node.basic_identifier().getText(), null, null);
        } else {
            final String nodeName = nodeInstance.instanceName.toText();
            final Long nodeVersion = this.convertVersionToLong(context, nodeInstance.instanceTypeDefVersion);
            final String instanceTypeDefName = nodeInstance.instanceTypeDefName;
            nodeInstanceElement = new InstanceElement(nodeName, instanceTypeDefName, nodeVersion);
        }
        return nodeInstanceElement;
    }

    public String getPortNameFromIdentifier(final Context context, final Expression portNameIdentifier) {
        final StringExpression portNameExpr = context.lookup(portNameIdentifier.toText(), StringExpression.class, false);
        final String portName;
        if (portNameExpr != null) {
            portName = portNameExpr.text;
        } else {
            portName = portNameIdentifier.toText();
        }
        return portName;
    }
}
