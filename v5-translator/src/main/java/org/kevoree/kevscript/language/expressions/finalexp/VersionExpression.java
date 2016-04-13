package org.kevoree.kevscript.language.expressions.finalexp;

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

    @Override
    public String toString() {
        return "VersionExpression{" +
                "version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VersionExpression that = (VersionExpression) o;

        return version == that.version;

    }

    @Override
    public int hashCode() {
        return (int) (version ^ (version >>> 32));
    }
}
