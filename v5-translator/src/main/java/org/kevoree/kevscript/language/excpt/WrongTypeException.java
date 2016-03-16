package org.kevoree.kevscript.language.excpt;

import org.kevoree.kevscript.language.expressions.Expression;

/**
 * Created by mleduc on 16/03/16.
 */
public class WrongTypeException extends RuntimeException {
    public WrongTypeException(String identifier, Expression expression) {
    }
}
