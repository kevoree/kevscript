package org.kevoree.kevscript.language.assignable;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.excpt.CustomException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mleduc on 02/03/16.
 */
public class IdentifierAssignable extends Assignable {

    private List<Chunk> elems = new ArrayList<>();

    @Override
    public String toText() {
        return StringUtils.join(elems, ".");
    }

    @Override
    public Assignable resolve(Context context) {

        // TODO : @element not resolved yet
        final String firstElementName = elems.get(0).id;
        final boolean isAt = elems.get(0).isAt;
        Assignable currentId;
        if(!isAt) {
            currentId = context.getMapIdentifiers().get(firstElementName);
            for (int i = 1; i < elems.size(); i++) {
                if (currentId != null) {
                    if (currentId instanceof ObjectAssignable) {
                        final String currentElementName = elems.get(i).id;
                        currentId = ((ObjectAssignable) currentId).get(currentElementName);
                    } else if (currentId instanceof FunctionAssignable) {
                        currentId = ((FunctionAssignable) currentId).getRetValue();
                    } else if (currentId instanceof StringAssignable) {
                        continue;
                    } else if (currentId instanceof IdentifierAssignable) {
                        currentId = new StringAssignable("TODO IdentifierAssignable");
                    } else {
                        throw new NotImplementedException("Assignable type not resolvable yet " + currentId);
                    }
                }
            }
        } else {
            throw  new NotImplementedException("@assignable not implemented");
        }

        return currentId;
    }

    public void add(Chunk a) {
        elems.add(a);
    }

    public static Chunk at(String id) {
        return new IdentifierAssignable.Chunk(true, id);
    }

    public static Chunk identifier(String id) {
        return new IdentifierAssignable.Chunk(false, id);
    }

    public static class Chunk {
        public final boolean isAt;
        public final String id;

        public Chunk(final boolean isAt, final String id) {
            this.isAt = isAt;
            this.id = id;
        }

        @Override
        public String toString() {
            if (isAt) {
                return "@" + id;
            } else {
                return id;
            }
        }
    }
}
