package org.kevoree.kevscript.language.excpt;

/**
 * Created by mleduc on 30/03/16.
 */
public class ResourceNotFoundException extends RuntimeException {
    private final String pathText;

    public ResourceNotFoundException(String pathText) {
        this.pathText = pathText;
    }

    @Override
    public String getMessage() {
        return this.pathText + " not found";
    }
}
