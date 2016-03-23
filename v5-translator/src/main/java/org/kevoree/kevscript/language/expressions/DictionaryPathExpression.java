package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 23/03/16.
 */
public class DictionaryPathExpression implements FinalExpression{
    public final InstancePathExpression instancePathExpression;
    public final String dicoName;
    public final String frag;

    public DictionaryPathExpression(InstancePathExpression instancePathExpression, String dicoName, String frag) {
        this.instancePathExpression = instancePathExpression;
        this.dicoName = dicoName;
        this.frag = frag;
    }

    @Override
    public String toText() {
        final String ret;
        if(this.frag == null) {
            ret = this.instancePathExpression.toText() + "#" + this.dicoName;
        } else {
            ret  = this.instancePathExpression.toText() + "#" + this.dicoName + "/" + frag;
        }
        return ret;
    }
}
