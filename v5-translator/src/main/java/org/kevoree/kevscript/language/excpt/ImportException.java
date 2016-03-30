package org.kevoree.kevscript.language.excpt;

/**
 * Created by mleduc on 30/03/16.
 */
public class ImportException extends RuntimeException {
    private final String key;
    private final String resource;

    public ImportException(final String key, final String resource) {
        this.key = key;
        this.resource = resource;
    }

    @Override
    public String getMessage() {
        return key + " not found in " + resource;
    }
}
