package me.muffin.oyveyplus.api.event.events;

import me.muffin.oyveyplus.api.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class UpdateWalkingPlayerEvent
        extends EventStage {
    public UpdateWalkingPlayerEvent(int stage) {
        super(stage);
    }
}



