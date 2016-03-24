package org.kevoree.kevscript.language.excpt;

/**
 * Created by mleduc on 22/03/16.
 */
public class WrongNumberOfArguments extends RuntimeException {
    private final String methodName;
    private final int definedNbr;
    private final int callerNbr;

    public WrongNumberOfArguments(String methodName, int definedNbr, int callerNbr) {
        this.methodName = methodName;
        this.definedNbr = definedNbr;
        this.callerNbr = callerNbr;
    }

    @Override
    public String getMessage() {
        return "method " + methodName + " expected " + definedNbr + " arguments but got " + callerNbr;
    }
}
