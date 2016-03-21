package org.kevoree.kevscript.language.excpt;

import org.kevoree.kevscript.language.expressions.Expression;

/**
 * Created by mleduc on 21/03/16.
 */
public class PortPathNotFound extends RuntimeException {
    private final Expression identifier;

    public PortPathNotFound(Expression identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getMessage() {
        return identifier + " portPath not found.";
    }
}
