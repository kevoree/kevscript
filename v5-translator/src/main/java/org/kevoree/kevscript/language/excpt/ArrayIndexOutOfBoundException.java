package org.kevoree.kevscript.language.excpt;

/**
 *
 *
 */
public class ArrayIndexOutOfBoundException extends RuntimeException {
    public ArrayIndexOutOfBoundException(String arrayStatement, int index) {
        super("Array "+arrayStatement+" out of bound with index "+index);
    }
}
