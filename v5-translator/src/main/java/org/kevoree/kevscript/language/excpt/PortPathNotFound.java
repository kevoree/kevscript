package org.kevoree.kevscript.language.excpt;

/**
 * Created by mleduc on 21/03/16.
 */
public class PortPathNotFound extends RuntimeException {
    private final String identifier;

    public PortPathNotFound(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getMessage() {
        return "portPath " + identifier + " not found.";
    }
}
