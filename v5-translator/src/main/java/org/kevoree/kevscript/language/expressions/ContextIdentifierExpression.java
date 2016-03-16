package org.kevoree.kevscript.language.expressions;

import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.language.context.Context;

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
    public Expression resolve(Context context) {

        // TODO : @element not resolved yet
        /*final String firstElementName = elems.get(0).id;*/
        //final boolean isAt = elems.get(0).isAt;
        /*Expression currentId = context.getIdentifiers().get(firstElementName);
        for (int i = 1; i < elems.size(); i++) {
            if (currentId != null) {
                if (currentId instanceof ObjectExpression) {
                    final String currentElementName = elems.get(i).id;
                    currentId = ((ObjectExpression) currentId).get(currentElementName);
                } else if (currentId instanceof FunctionCallExpression) {
                    currentId = ((FunctionCallExpression) currentId).getRetValue();
                } else if (currentId instanceof StringExpression || currentId instanceof InstanceExpression) {
                    continue;
                } else if (currentId instanceof IdentifierExpression) {
                    currentId = new StringExpression("TODO IdentifierAssignable");
                } else {
                    throw new NotImplementedException("Assignable type not resolvable yet " + currentId);
                }
            }
        }
*/
        return null;
    }

    private Expression resolve(List<String> atReference, Context context) {

        /*Expression assign = context.getIdentifiers().get(atReference.get(0));

        if(assign instanceof ObjectExpression) {
            assign = resolveAssign(atReference.subList(1, atReference.size()), assign);
        }

        return assign;*/
        return null;

    }

    private Expression resolveAssign(List<String> strings, Expression assign) {
        final Expression ret;
        if(assign instanceof ObjectDeclExpression) {
            ret = ((ObjectDeclExpression) assign).get(strings.get(0));
        } else {
            ret = assign;
        }
        return ret;
    }

    public void add(Expression a) {
        elems.add(a);
    }
}
