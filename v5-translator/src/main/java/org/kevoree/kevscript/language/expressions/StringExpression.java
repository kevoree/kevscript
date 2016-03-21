package org.kevoree.kevscript.language.expressions;

/**
 * Created by mleduc on 02/03/16.
 */
public class StringExpression extends Expression {
    public final String text;

    public StringExpression(final String text) {
        this.text = text.replaceAll("\"", "");
    }

    @Override
    public String toText() {
        return text;
    }

    /*@Override
    public boolean match(Expression identifier) {*/
        /*final boolean ret;
        if(identifier instanceof IdentifierExpression) {
            final IdentifierExpression identifier1 = (IdentifierExpression) identifier;
            if(identifier1.right != null) {
                ret = false;
            } else {
                ret = this.match(identifier1.left);
            }
        } else if (identifier instanceof StringExpression) {
            ret = ((StringExpression) identifier).text.equals(name);
        }else {
            ret = false;
        }*/
        /*return false;
    }*/

    @Override
    public String toString() {
        return super.toString();
    }
}
