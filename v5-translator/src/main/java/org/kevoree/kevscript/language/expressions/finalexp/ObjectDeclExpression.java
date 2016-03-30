package org.kevoree.kevscript.language.expressions.finalexp;

import org.kevoree.kevscript.language.expressions.Expression;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mleduc on 02/03/16.
 */
public class ObjectDeclExpression implements FinalExpression {

    public Map<String, FinalExpression> values = new HashMap<>();

    public Expression put(String key, FinalExpression value) {
        return values.put(key, value);
    }

    @Override
    public String toText() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append('\n');
        for (Map.Entry<String, FinalExpression> itt : values.entrySet()) {
            sb.append(itt.getKey());
            sb.append(" : ");
            sb.append(itt.getValue().toText());
            sb.append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
}
