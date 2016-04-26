package org.kevoree.kevscript.language.expressions.finalexp;

/**
 *
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

    @Override
    public String toString() {
        return "VersionExpression{" +
                "version=" + version +
                '}';
    }
}
