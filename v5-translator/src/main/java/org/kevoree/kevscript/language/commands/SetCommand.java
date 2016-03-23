package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.DictionaryElement;

/**
 * Created by mleduc on 22/03/16.
 */
public class SetCommand extends Commands {
    private DictionaryElement dictionaryElement;

    public SetCommand(DictionaryElement dictionaryElement) {
        this.dictionaryElement = dictionaryElement;
        // TODO : defining affected value
    }
}
