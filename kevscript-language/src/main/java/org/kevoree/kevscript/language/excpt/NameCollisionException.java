package org.kevoree.kevscript.language.excpt;

/**
 * Created by mleduc on 17/03/16.
 */
public class NameCollisionException extends RuntimeException {
    private final String varName;

    public NameCollisionException(String varName) {
        this.varName = varName;
    }

    @Override
    public String getMessage() {
        return varName + " already declared in this scope";
    }
}
