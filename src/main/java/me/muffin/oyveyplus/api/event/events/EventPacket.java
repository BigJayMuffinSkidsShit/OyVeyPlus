package me.muffin.oyveyplus.api.event.events;

import me.muffin.oyveyplus.api.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {

    private final Packet<?> packet;

    public EventPacket(final Packet<?> packet) {
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) packet;
    }

    public final static class Receive extends EventPacket {
        public Receive(final Packet<?> packet) {
            super(packet);
        }
    }

    public final static class Send extends EventPacket {
        public Send(final Packet<?> packet) {
            super(packet);
        }
    }

    public final static class Post extends EventPacket {
        public Post(final Packet<?> packet) {
            super(packet);
        }
    }

}
