package org.kevoree.kevscript.language.visitors.helper;

import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.commands.element.InstanceElement;
import org.kevoree.kevscript.language.commands.element.RootInstanceElement;
import org.kevoree.kevscript.language.commands.element.object.AbstractObjectElement;
import org.kevoree.kevscript.language.commands.element.object.ArrayElement;
import org.kevoree.kevscript.language.commands.element.object.ObjectElement;
import org.kevoree.kevscript.language.commands.element.object.StringElement;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.WrongTypeException;
import org.kevoree.kevscript.language.expressions.*;
import org.kevoree.kevscript.language.visitors.ExpressionVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        final FinalExpression nodeExpression = new ExpressionVisitor(context).visitIdentifier(node);
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

    public List<RootInstanceElement> getInstancesFromInstancePathContext(final KevScriptParser.InstancePathContext instancePathContext) {

        final List<RootInstanceElement> ret = new ArrayList<>();
        for (final IdentifierContext a : instancePathContext.identifier()) {
            ret.add(this.getInstanceFromIdentifierContext(a));
        }

        return ret;
    }

    public String getPortNameFromIdentifier(final Expression portNameIdentifier) {
        final StringExpression portNameExpr = context.lookup(portNameIdentifier, StringExpression.class);
        if (portNameExpr != null) {
            return portNameExpr.text;
        } else {
            return null;
        }
    }

    public RootInstanceElement getInstanceFromIdentifierContext(final IdentifierContext node) {
        final FinalExpression nodeExpression = new ExpressionVisitor(context).visit(node);
        final InstanceExpression nodeInstance = context.lookup(nodeExpression, InstanceExpression.class, false);
        final RootInstanceElement nodeRootInstanceElement;

        if (nodeInstance == null && node.DOT() == null && node.contextRef() == null) {
            nodeRootInstanceElement = new RootInstanceElement(node.basic_identifier().getText(), null, null);
        } else {
            final String nodeName = nodeInstance.instanceName;
            final Long nodeVersion = this.convertVersionToLong(nodeInstance.instanceTypeDefVersion);
            final String instanceTypeDefName = nodeInstance.instanceTypeDefName;
            nodeRootInstanceElement = new RootInstanceElement(nodeName, instanceTypeDefName, nodeVersion);
        }
        return nodeRootInstanceElement;
    }

    public RootInstanceElement convertPortPathToComponentElement(final InstancePathExpression instancePath) {
        final InstanceExpression componentExpression = context.lookup(instancePath.component, InstanceExpression.class, false);
        final RootInstanceElement component;
        if (componentExpression != null) {
            component = new RootInstanceElement(componentExpression.instanceName, componentExpression.instanceTypeDefName, this.convertVersionToLong(componentExpression.instanceTypeDefVersion));
        } else {
            component = new RootInstanceElement(context.lookup(instancePath.node, FinalExpression.class).toText());
        }
        return component;
    }

    public RootInstanceElement convertPortPathToNodeElement(final InstancePathExpression instancePath) {
        final InstanceExpression nodeExpression = context.lookup(instancePath.node, InstanceExpression.class, false);
        final RootInstanceElement node;
        if (nodeExpression != null) {
            node = new RootInstanceElement(nodeExpression.instanceName, nodeExpression.instanceTypeDefName, this.convertVersionToLong(nodeExpression.instanceTypeDefVersion));
        } else if (instancePath.node != null) {
            node = new RootInstanceElement(this.context.lookup(instancePath.node, FinalExpression.class).toText());
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

    public InstanceElement getInstanceElement(KevScriptParser.InstancePathContext instancePathContext) {
        final List<RootInstanceElement> listInstances = this.getInstancesFromInstancePathContext(instancePathContext);
        final InstanceElement instance;
        if (listInstances.size() == 1) {
            final RootInstanceElement childInstance = listInstances.get(0);
            instance = new InstanceElement(childInstance);
        } else {
            final RootInstanceElement nodeInstance = listInstances.get(0);
            final RootInstanceElement componentInstance = listInstances.get(1);
            instance = new InstanceElement(nodeInstance, componentInstance);
        }
        return instance;
    }

    public ObjectElement convertObjectDeclToObjectElement(final ObjectDeclExpression objectExpr) {
        final ObjectElement ret = new ObjectElement();
        for (Map.Entry<String, FinalExpression> entry : objectExpr.values.entrySet()) {
            final FinalExpression valueExpr = entry.getValue();
            ret.put(entry.getKey(), internalExpressionToObjectConversion(valueExpr));
        }
        return ret;
    }

    private ArrayElement convertArrayDeclExpressionToObjectElement(final ArrayDeclExpression arrayExpr) {
        final ArrayElement ret = new ArrayElement();
        for (final FinalExpression entry : arrayExpr.expressionList) {
            ret.add(internalExpressionToObjectConversion(entry));
        }
        return ret;
    }

    private AbstractObjectElement internalExpressionToObjectConversion(FinalExpression entry) {
        AbstractObjectElement value;
        if (entry instanceof StringExpression) {
            value = new StringElement(((StringExpression) entry).text);
        } else if (entry instanceof ObjectDeclExpression) {
            value = this.convertObjectDeclToObjectElement((ObjectDeclExpression) entry);
        } else if (entry instanceof ArrayDeclExpression) {
            value = this.convertArrayDeclExpressionToObjectElement((ArrayDeclExpression) entry);
        } else {
            throw new WrongTypeException(null, null);
        }
        return value;
    }
}
