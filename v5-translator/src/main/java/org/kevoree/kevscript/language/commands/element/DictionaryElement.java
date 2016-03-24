package org.kevoree.kevscript.language.commands.element;

/**
 * Created by mleduc on 22/03/16.
 */
public class DictionaryElement {
    public final String dicoName;
    public final String frag;
    public final InstanceElement instance;

    public DictionaryElement(final String dicoName, final String frag, final InstanceElement instance) {
        this.dicoName = dicoName;
        this.frag = frag;
        this.instance = instance;
    }


}
