package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by mleduc on 15/03/16.
 */
public class Commands extends ArrayList<AbstractCommand> {

    public Commands addCommand(final AbstractCommand command) {
        this.add(command);
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Commands{\n\t");
        sb.append(StringUtils.join(this, "\n\t"));
        sb.append("\n}");
        return sb.toString();
    }

    /*@Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;

        ListIterator<AbstractCommand> e1 = listIterator();
        ListIterator e2 = ((List) o).listIterator();
        int i =0;
        while (e1.hasNext() && e2.hasNext()) {
            AbstractCommand o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2))) {
                System.out.println("index "+i+" failed !");
                return false;
            }
            i++;
        }
        return !(e1.hasNext() || e2.hasNext());
    }*/
}
