package org.kevoree.kevscript.language.expressions.nonfinalexp;

import org.kevoree.kevscript.language.expressions.nonfinalexp.ContextIdentifierExpression;

/**
 * Created by mleduc on 15/03/16.
 */
public class ContextRefExpression extends ContextIdentifierExpression {
    @Override
    public String toPath() {
        return '&' + super.toPath();
    }
}
