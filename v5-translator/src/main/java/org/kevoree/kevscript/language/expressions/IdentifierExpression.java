package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 15/03/16.
 */
public class IdentifierExpression implements NonFinalExpression {

    public final FinalExpression left;
    public final FinalExpression right;

    public IdentifierExpression(final FinalExpression left) {
        this.left = left;
        this.right = null;
    }

    public IdentifierExpression(final FinalExpression left, final FinalExpression visit) {
        this.left = left;
        this.right = visit;
    }

    @Override
    public String toPath() {
        return serial();
    }

    private String serial() {
        final String ret;
        if (right != null) {
            ret = left.toText() + "." + right.toText();
        } else {
            ret = left.toText();
        }
        return ret;
    }
}
