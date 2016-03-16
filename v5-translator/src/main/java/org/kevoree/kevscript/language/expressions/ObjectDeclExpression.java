package org.kevoree.kevscript.language.expressions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mleduc on 02/03/16.
 */
public class ObjectDeclExpression extends Expression {

    private Map<String, Expression> values = new HashMap<>();

    public Expression put(String key, Expression value) {
        return values.put(key, value);
    }

    @Override
    public String toText() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append('\n');
        for(Map.Entry<String, Expression> aa: values.entrySet()) {
            sb.append(aa.getKey());
            sb.append(" : ");
            sb.append(aa.getValue().toText());
            sb.append('\n');
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean match(Expression identifier) {
        return false;
    }


    public Expression get(String chunk) {
        return this.values.get(chunk);
    }
}
