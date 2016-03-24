package org.kevoree.kevscript.language.commands;

import org.kevoree.kevscript.language.commands.element.InstanceElement;

/**
 * Created by mleduc on 24/03/16.
 */
public class MoveCommand extends AbstractCommand {
    public final InstanceElement targetInstance;
    public final InstanceElement sourceInstance;

    public MoveCommand(InstanceElement targetInstance, InstanceElement sourceInstance) {
        this.targetInstance = targetInstance;
        this.sourceInstance = sourceInstance;
    }
}
