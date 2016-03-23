package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.DictionaryElement;

/**
 * Created by mleduc on 22/03/16.
 */
public class SetCommand extends AbstractCommand {
    public DictionaryElement dictionaryElement;
    public String value;

    public SetCommand(DictionaryElement dictionaryElement, String value) {
        this.dictionaryElement = dictionaryElement;
        this.value = value;
    }
}
