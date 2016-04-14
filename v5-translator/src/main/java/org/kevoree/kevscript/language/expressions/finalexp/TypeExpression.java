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

    public TypeExpression(final String namespace, final String name, final VersionExpression versionExpr, final ObjectDeclExpression duVersionsExpr) {
        this.name = name;
        this.namespace = namespace;
        this.versionExpr = versionExpr;
        this.duVersionsExpr = duVersionsExpr;
    }

    @Override
    public String toString() {
        return "TypeExpression{" +
                "duVersionsExpr=" + duVersionsExpr +
                ", name='" + name + '\'' +
                ", namespace='" + namespace + '\'' +
                ", versionExpr=" + versionExpr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeExpression that = (TypeExpression) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (namespace != null ? !namespace.equals(that.namespace) : that.namespace != null) return false;
        if (versionExpr != null ? !versionExpr.equals(that.versionExpr) : that.versionExpr != null) return false;
        return duVersionsExpr != null ? duVersionsExpr.equals(that.duVersionsExpr) : that.duVersionsExpr == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
        result = 31 * result + (versionExpr != null ? versionExpr.hashCode() : 0);
        result = 31 * result + (duVersionsExpr != null ? duVersionsExpr.hashCode() : 0);
        return result;
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
