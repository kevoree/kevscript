package org.kevoree.kevscript.language.expressions.finalexp;

/**
 * Created by mleduc on 30/03/16.
 */
public class NullExpression implements FinalExpression {
    @Override
    public String toText() {
        return null;
    }

    @Override
    public String toString() {
        return "NullExpression{}";
    }


}
