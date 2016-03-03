package org.kevoree.kevscript.language.assignable;

import org.kevoree.kevscript.language.assignable.Assignable;
import org.kevoree.kevscript.language.context.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mleduc on 02/03/16.
 */
public class ObjectAssignable extends Assignable {

    private Map<String, Assignable> values = new HashMap<>();

    public Assignable put(String key, Assignable value) {
        return values.put(key, value);
    }

    @Override
    public String toText() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append('\n');
        for(Map.Entry<String, Assignable> aa: values.entrySet()) {
            sb.append(aa.getKey());
            sb.append(" : ");
            sb.append(aa.getValue().toText());
            sb.append('\n');
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Assignable resolve(Context context) {
        return this;
    }

    public Assignable get(String chunk) {
        return this.values.get(chunk);
    }
}
