package org.kevoree.kevscript.stub;

import org.KevoreeView;
import org.kevoree.TypeDefinition;
import org.kevoree.kevscript.language.expressions.finalexp.TypeExpression;
import org.kevoree.kevscript.language.expressions.finalexp.VersionExpression;
import org.kevoree.kevscript.resolver.RegistryResolver;

/**
 *
 */
public class StubRegistryResolver implements RegistryResolver {

    @Override
    public TypeDefinition resolve(KevoreeView kView, TypeExpression expr) {
        TypeDefinition type;

        if (expr.name.contains("Node")) {
            type = kView.createNodeType();
        } else if (expr.name.contains("Comp")) {
            type = kView.createComponentType();
        } else if (expr.name.contains("Chan")) {
            type = kView.createChannelType();
        } else if (expr.name.contains("Connector")) {
            type = kView.createModelConnectorType();
        } else {
            throw new RuntimeException("Unknown type definition in stubbed registry: "+expr.toString());
        }

        if (type != null) {
            type.setName(expr.name);
            type.setVersion(this.resolve(expr.versionExpr));
            type.setDescription("No description");
        }

        return type;
    }

    public int resolve(VersionExpression expr) {
        int version = 1;
        if (expr != null && !expr.toText().isEmpty()) {
            return Integer.valueOf(expr.toText());
        }
        return version;
    }
}
