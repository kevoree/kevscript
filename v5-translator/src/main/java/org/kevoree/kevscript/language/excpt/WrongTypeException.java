package org.kevoree.kevscript.language.excpt;

import org.kevoree.kevscript.language.expressions.finalexp.FinalExpression;

/**
 *
 * Created by mleduc on 16/03/16.
 */
public class WrongTypeException extends RuntimeException {
    private final String identifier;
    private final Class<?> expectedClass;
    private final Class<?> currentClass;

    public <T extends FinalExpression> WrongTypeException(String identifier, Class<T> expectedClass, Class<?> currentClass) {
        this.identifier = identifier;
        this.expectedClass = expectedClass;
        this.currentClass = currentClass;
    }

    @Override
    public String getMessage() {
        return this.identifier + " is expected to be of type " + this.expectedClass.getSimpleName() + " but is " + this.currentClass.getSimpleName();
    }
}
