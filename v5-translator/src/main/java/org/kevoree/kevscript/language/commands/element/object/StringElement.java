package org.kevoree.kevscript.language.commands.element.object;

/**
 * Created by mleduc on 25/03/16.
 */
public class StringElement implements AbstractObjectElement {

    public final String text;

    public StringElement(String text) {
        this.text = text;
    }
}
