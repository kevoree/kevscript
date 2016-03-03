package org.kevoree.kevscript.language.assignable;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.language.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mleduc on 02/03/16.
 */
public class LongIdentifierAssignable extends Assignable {

    private List<Chunk> elems = new ArrayList<>();

    @Override
    public String toText() {
        return StringUtils.join(elems, ".");
    }

    @Override
    public String resolve(Context context) {

        // TODO : @element not resolved yet
        Assignable currentId = context.getMapIdentifiers().get(elems.get(0).id);
        for(int i=1; i<elems.size(); i++) {
            if(currentId instanceof  ObjectAssignable) {
                currentId = ((ObjectAssignable) currentId).get(elems.get(i).id);
            } else {
                throw new NotImplementedException("Assignable type not resolvable yet");
            }
        }
        return currentId.toText();
    }

    public void add(Chunk a) {
        elems.add(a);
    }

    public static Chunk at(String id) {
        return new LongIdentifierAssignable.Chunk(true, id);
    }

    public static Chunk identifier(String id) {
        return new LongIdentifierAssignable.Chunk(false, id);
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
            if(isAt) {
                return "@" + id;
            } else {
                return id;
            }
        }
    }
}
