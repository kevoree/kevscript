package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 15/03/16.
 */
public class ContextRefExpression extends ContextIdentifierExpression {
    @Override
    public String toPath() {
        return '&' + super.toPath();
    }
}
