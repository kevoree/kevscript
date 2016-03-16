package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 02/03/16.
 */
public class ContextIdentifierExpression extends Expression {

    private List<Expression> elems = new ArrayList<>();

    @Override
    public String toText() {
        return StringUtils.join(elems, ".");
    }

    @Override
    public boolean match(Expression identifier) {
        return false;
    }
    public void add(Expression a) {
        elems.add(a);
    }
}
