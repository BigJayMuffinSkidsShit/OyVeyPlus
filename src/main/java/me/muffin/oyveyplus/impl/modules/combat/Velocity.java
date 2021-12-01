package me.muffin.oyveyplus.impl.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.*;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Velocity extends Module {
    public Velocity() {
        super ("Velocity", "velocity", Category.Combat);
    }

    private final Setting<Boolean> exp = register("Crystal",true);
      private final Setting<Boolean> bobb= register("bobbers",true);


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
