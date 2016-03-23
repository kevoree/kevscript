package org.kevoree.kevscript.language.excpt;

import org.kevoree.kevscript.language.expressions.Expression;
import org.kevoree.kevscript.language.expressions.FinalExpression;

/**
 * Created by mleduc on 16/03/16.
 */
public class WrongTypeException extends RuntimeException {
    public <T extends FinalExpression> WrongTypeException(String identifier, Class<T> clazz) {

    }
}
