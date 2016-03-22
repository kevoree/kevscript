package org.kevoree.kevscript.language.excpt;

/**
 * Created by mleduc on 16/03/16.
 */
public class InstanceNameNotFound extends RuntimeException {

    private final String identifier;


    public InstanceNameNotFound(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getMessage() {
        return "instance " + identifier + " not found";
    }
}
