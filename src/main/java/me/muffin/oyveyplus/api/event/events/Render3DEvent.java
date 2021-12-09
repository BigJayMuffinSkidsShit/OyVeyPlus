package me.muffin.oyveyplus.api.event.events;

import me.muffin.oyveyplus.api.event.Event;
import me.muffin.oyveyplus.api.event.EventStage;

public class Render3DEvent
        extends EventStage {
    private final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
