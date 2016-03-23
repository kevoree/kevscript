package org.kevoree.kevscript.language.expressions;

import org.kevoree.kevscript.language.commands.Commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 03/03/16.
 */
public class FunctionCallExpression implements FinalExpression {
    public final Commands commands;
    public final String returnValue;

    public FunctionCallExpression(Commands commands) {
        this.commands = commands;
        this.returnValue = null;
    }

    public FunctionCallExpression(Commands commands, String returnValue) {
        this.commands = commands;
        this.returnValue = returnValue;
    }

    @Override
    public String toText() {
        return returnValue;
    }
}
