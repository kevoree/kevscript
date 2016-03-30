package org.kevoree.kevscript.language.expressions.finalexp;

import org.kevoree.kevscript.language.expressions.finalexp.FinalExpression;

/**
 * Created by mleduc on 16/03/16.
 */
public class VersionExpression implements FinalExpression {
    public final long version;

    public VersionExpression(long version) {
        this.version = version;
    }

    @Override
    public String toText() {
        return String.valueOf(version);
    }
}
