package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 02/03/16.
 */
public class ContextIdentifierExpression implements FinalExpression {

    private List<FinalExpression> elems = new ArrayList<>();

    @Override
    public String toText() {
        final List<String> strElemens = new ArrayList<>();
        for (FinalExpression e : elems) {
            strElemens.add(e.toText());
        }
        return StringUtils.join(strElemens, ".");
    }

    public void add(FinalExpression a) {
        elems.add(a);
    }
}
