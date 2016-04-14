package org.kevoree.kevscript;

import org.KevoreeView;
import org.kevoree.Model;

/**
 * Created by leiko on 3/1/16.
 */
public class KevScriptWalker extends KevScriptBaseListener {

    private Model model;
    private KevoreeView kView;

    public KevScriptWalker(Model model, KevoreeView kView) {
        this.model = model;
        this.kView = kView;
    }

    // TODO
}
