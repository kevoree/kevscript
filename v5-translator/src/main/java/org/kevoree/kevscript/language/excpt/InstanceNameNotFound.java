package org.kevoree.kevscript.language.excpt;

import org.kevoree.kevscript.language.expressions.Expression;

/**
 * Created by mleduc on 16/03/16.
 */
public class InstanceNameNotFound extends RuntimeException {
    private final Expression expression;

    public InstanceNameNotFound(final Expression expression) {
        this.expression = expression;
    }

    @Override
    public String getMessage() {
        return "instance " + expression.toText() + " not found";
    }
}
