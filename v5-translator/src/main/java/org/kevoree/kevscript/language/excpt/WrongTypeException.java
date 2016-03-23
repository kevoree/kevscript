package org.kevoree.kevscript.language.excpt;

import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.FinalExpression;

/**
 * Created by mleduc on 16/03/16.
 */
public class WrongTypeException extends RuntimeException {
    private final String identifier;
    private final Class<?> clazz;

    public <T extends FinalExpression> WrongTypeException(String identifier, Class<T> clazz) {
        this.identifier = identifier;
        this.clazz = clazz;
    }

    @Override
    public String getMessage() {
        return this.identifier + " is expected to be of type " + this.clazz.getSimpleName();
    }
}
