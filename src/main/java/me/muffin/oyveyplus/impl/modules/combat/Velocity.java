package me.muffin.oyveyplus.impl.modules.combat;

import me.muffin.oyveyplus.api.event.events.EventPacket;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.common.MinecraftForge;

public class Velocity extends Module {
    public Velocity() {
        super ("Velocity", "velocity", Category.Combat);
    }

    private final Setting<Boolean> exp = register("Crystal",true);
      private final Setting<Boolean> bobbers = register("bobbers",true);


    @Override
    public void onEnable() {
      MinecraftForge.EVENT_BUS.register(listener1);
    }

    @Override
    public void onDisable() {
      MinecraftForge.EVENT_BUS.unregister(listener1);
    }
  
  @EventHandler
    private final Listener<EventPacket.Receive> listener1 = new Listener<>(event -> {
        if(event.getPacket() instanceof SPacketEntityVelocity) {
            if(((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                event.cancel();
            }
        }

        if(event.getPacket() instanceof SPacketExplosion && exp.getValue()) {
            event.cancel();
        }

        if(event.getPacket() instanceof SPacketEntityStatus && bobbers.getValue()) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if(packet.getOpCode() == 31) {
                final Entity entity = packet.getEntity(mc.world);
                if(entity != null && entity instanceof EntityFishHook) {
                    final EntityFishHook fishHook = (EntityFishHook) entity;
                    if(fishHook.caughtEntity == mc.player) {
                        event.cancel();
                    }
                }
            }
        }
    });

  

}
