package org.kevoree.kevscript.language.expressions.finalexp;

/**
 * Created by mleduc on 16/03/16.
 */
public class VersionExpression implements FinalExpression {
    public final long version;
    private boolean exported;

    public VersionExpression(long version) {
        this.version = version;
    }

    @Override
    public String toText() {
        return String.valueOf(version);
    }

    @Override
    public boolean isExported() {
        return this.exported;
    }

    @Override
    public void setExported(boolean exported) {
        this.exported = exported;
    }
}
