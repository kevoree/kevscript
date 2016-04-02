package org.kevoree.kevscript.language.expressions.finalexp;

/**
 *
 * Created by leiko on 4/1/16.
 */
public class TypeExpression implements FinalExpression {

    public final String name;
    public final String namespace;
    public final VersionExpression versionExpr;
    public final ObjectDeclExpression duVersionsExpr;

    public TypeExpression(String namespace, String name, VersionExpression versionExpr, ObjectDeclExpression duVersionsExpr) {
        this.name = name;
        this.namespace = namespace;
        this.versionExpr = versionExpr;
        this.duVersionsExpr = duVersionsExpr;
    }

    @Override
    public String toText() {
        String ret = "";
        if (this.namespace != null) {
            ret = this.namespace + ".";
        }
        ret += this.name;
        if (this.versionExpr != null) {
            ret += "/" + this.versionExpr.toText();
        }
        if (this.duVersionsExpr != null) {
            ret += " " + this.duVersionsExpr.toText();
        }
        return ret;
    }
}
