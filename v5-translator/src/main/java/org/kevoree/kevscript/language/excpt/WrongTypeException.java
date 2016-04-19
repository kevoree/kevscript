package org.kevoree.kevscript.language.excpt;

import org.antlr.v4.runtime.ParserRuleContext;
import org.kevoree.kevscript.language.expressions.finalexp.FinalExpression;

/**
 * Created by mleduc on 16/03/16.
 */
public class WrongTypeException extends RuntimeException {
    private final ParserRuleContext context;
    private final Class<?> expectedClass;
    private final Class<?> currentClass;

    public <T extends FinalExpression> WrongTypeException(ParserRuleContext context, Class<T> expectedClass, Class<?> currentClass) {
        this.context = context;
        this.expectedClass = expectedClass;
        this.currentClass = currentClass;
    }


    @Override
    public String getMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.context.getText());
        sb.append(" is expected to be of type ");
        if (expectedClass != null) {
            sb.append(this.expectedClass.getSimpleName());
        }
        if (currentClass != null) {
            sb.append(" but is ");
            sb.append(this.currentClass.getSimpleName());
        }
        sb.append(" [l: ");
        sb.append(this.context.getStart().getLine());
        sb.append("]");
        return sb.toString();
    }
}
