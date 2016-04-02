package org.kevoree.kevscript.language.excpt;

import org.kevoree.kevscript.language.expressions.Expression;

/**
 * Created by mleduc on 16/03/16.
 */
public class VersionNotFound extends RuntimeException {
    public VersionNotFound(Expression expression) {
        super(expression.toText());
    }
}
