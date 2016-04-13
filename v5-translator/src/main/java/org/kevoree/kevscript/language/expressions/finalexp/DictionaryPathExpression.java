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
}
