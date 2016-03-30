package org.kevoree.kevscript.language.expressions;

import org.kevoree.kevscript.language.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 02/03/16.
 */
public class ContextIdentifierExpression implements NonFinalExpression {

    private List<String> elems = new ArrayList<>();

    public void add(String a) {
        elems.add(a);
    }

    @Override
    public String toPath() {
        return StringUtils.join(elems, ".");
    }

    public void addAll(ContextIdentifierExpression left) {
        elems.addAll(left.elems);
    }
}
