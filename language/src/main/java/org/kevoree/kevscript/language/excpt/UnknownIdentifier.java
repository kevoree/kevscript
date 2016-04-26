package org.kevoree.kevscript.language.excpt;

/**
 * Created by mleduc on 16/03/16.
 */
public class UnknownIdentifier extends RuntimeException {

    private final String identifier;


    public UnknownIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getMessage() {
        return "identifier " + identifier + " unknown";
    }
}
