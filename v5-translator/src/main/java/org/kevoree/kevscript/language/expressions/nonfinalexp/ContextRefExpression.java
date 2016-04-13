package org.kevoree.kevscript.language.expressions.nonfinalexp;

/**
 * Created by mleduc on 15/03/16.
 */
public class ContextRefExpression extends ContextIdentifierExpression {
    @Override
    public String toPath() {
        return '&' + super.toPath();
    }

    @Override
    public String toString() {
        return "ContextRefExpression{"+super.toString()+"}";
    }
}
