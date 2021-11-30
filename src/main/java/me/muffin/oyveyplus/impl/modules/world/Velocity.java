package me.muffin.oyveyplus.impl.modules.world;

import me.muffin.oyveyplus.api.event.events.EventPacket;
import me.muffin.oyveyplus.api.event.events.PacketEvent;
import me.muffin.oyveyplus.api.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Category.Combat);
    }

}
