package org.kevoree.kevscript.language.assignable;

/**
 * Created by mleduc on 02/03/16.
 */
public class StringAssignable extends Assignable {
    private final String text;

    public StringAssignable(final String text) {
        this.text = text;
    }

    @Override
    public String toText() {
        return text;
    }
}
