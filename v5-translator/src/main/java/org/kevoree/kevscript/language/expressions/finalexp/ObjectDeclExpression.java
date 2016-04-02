package org.kevoree.kevscript.language.expressions.finalexp;

import org.kevoree.kevscript.language.expressions.Expression;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
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
        if (!values.isEmpty()) {
            sb.append('\n');
        }
        Iterator<Map.Entry<String, FinalExpression>> it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, FinalExpression> entry = it.next();
            sb.append("  ");
            sb.append(entry.getKey());
            sb.append(" : ");
            sb.append(entry.getValue().toText());
            if (it.hasNext()) {
                sb.append("\n");
            }
        }
        if (!values.isEmpty()) {
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

}
