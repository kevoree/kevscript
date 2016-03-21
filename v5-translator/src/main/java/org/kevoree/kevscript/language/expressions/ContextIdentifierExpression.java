package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 02/03/16.
 */
public class ContextIdentifierExpression extends Expression {

    private List<Expression> elems = new ArrayList<>();

    @Override
    public String toText() {
        final List<String> strElemens = new ArrayList<>();
        for(Expression e: elems) {
            strElemens.add(e.toText());
        }
        return StringUtils.join(strElemens, ".");
    }

    public void add(Expression a) {
        elems.add(a);
    }
}
