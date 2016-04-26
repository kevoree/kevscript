package org.kevoree.kevscript.language.processor.instance.version;

import org.kevoree.kevscript.language.commands.InstanceCommand;
import org.kevoree.kevscript.language.commands.TimeCommand;
import org.kevoree.kevscript.language.commands.WorldCommand;

import java.util.ArrayList;

/**
 *
 * Created by mleduc on 20/04/16.
 */
//public class InstancesAggregationVisitor extends CollectionCommandVisitor<ArrayList<InstanceContext>, InstanceContext> {
//    private long world;
//    private long time;
//
//    public InstancesAggregationVisitor(long world, long time) {
//        this.world = world;
//        this.time = time;
//    }
//
//    @Override
//    public ArrayList<InstanceContext> visitWorldCommand(WorldCommand worldCommand) {
//        final long oldWorld = world;
//        world = worldCommand.world;
//        final ArrayList<InstanceContext> ret = super.visitWorldCommand(worldCommand);
//        world = oldWorld;
//        return ret;
//    }
//
//    @Override
//    public ArrayList<InstanceContext> visitTimeCommand(TimeCommand timeCommand) {
//        final long oldTime = time;
//        time = timeCommand.time;
//        final ArrayList<InstanceContext> ret = super.visitTimeCommand(timeCommand);
//        time = oldTime;
//        return ret;
//    }
//
//    @Override
//    public ArrayList<InstanceContext> visitInstanceCommand(InstanceCommand instanceCommand) {
//        final ArrayList<InstanceContext> ret = new ArrayList<>();
//        ret.add(new InstanceContext(this.world, this.time, instanceCommand));
//        return ret;
//    }
//}
