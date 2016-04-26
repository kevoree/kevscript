package org.kevoree.kevscript.exception;

/**
 *
 */
public class AddCommandException extends RuntimeException {

    public AddCommandException() {
        super("Add command can only target nodes");
    }

    public AddCommandException(String source, String target, String cause) {
        super("Unable to add "+source+" to "+target+": "+cause);
    }
}
