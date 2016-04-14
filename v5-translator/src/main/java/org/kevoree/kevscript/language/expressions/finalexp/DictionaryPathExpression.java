package org.kevoree.kevscript.language.expressions.finalexp;

/**
 * Created by mleduc on 23/03/16.
 */
public class DictionaryPathExpression implements FinalExpression {
    public final InstanceExpression instance;
    public final String dicoName;
    public final String frag;

    public DictionaryPathExpression(InstanceExpression instance, String dicoName, String frag) {
        this.instance = instance;
        this.dicoName = dicoName;
        this.frag = frag;
    }

    @Override
    public String toText() {
        final String ret;
        if (this.frag == null) {
            ret = this.instance.toText() + "#" + this.dicoName;
        } else {
            ret = this.instance.toText() + "#" + this.dicoName + "/" + frag;
        }
        return ret;
    }

    @Override
    public String toString() {
        return "DictionaryPathExpression{" +
                "dicoName='" + dicoName + '\'' +
                ", instance=" + instance +
                ", frag='" + frag + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryPathExpression that = (DictionaryPathExpression) o;

        if (instance != null ? !instance.equals(that.instance) : that.instance != null) return false;
        if (dicoName != null ? !dicoName.equals(that.dicoName) : that.dicoName != null) return false;
        return frag != null ? frag.equals(that.frag) : that.frag == null;

    }

    @Override
    public int hashCode() {
        int result = instance != null ? instance.hashCode() : 0;
        result = 31 * result + (dicoName != null ? dicoName.hashCode() : 0);
        result = 31 * result + (frag != null ? frag.hashCode() : 0);
        return result;
    }
}
