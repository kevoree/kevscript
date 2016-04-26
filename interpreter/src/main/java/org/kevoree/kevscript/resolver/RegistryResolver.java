package org.kevoree.kevscript.resolver;

import org.KevoreeView;
import org.kevoree.TypeDefinition;
import org.kevoree.kevscript.language.expressions.finalexp.TypeExpression;

/**
 *
 */
public interface RegistryResolver {

    public TypeDefinition resolve(KevoreeView kView, TypeExpression expr);
}
