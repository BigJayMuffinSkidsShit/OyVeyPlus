package me.muffin.oyveyplus.impl.modules.world;

import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GamemodeChanger extends Module {
    public GamemodeChanger() {
        super("GamemodeChanger", Category.World);
    }


    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (GamemodeChanger.mc.player == null) {
            return;
        }
        Minecraft.getMinecraft();
        GamemodeChanger.mc.playerController.setGameType(GameType.CREATIVE);
    }

    @Override
    public void onDisable() {
        if (GamemodeChanger.mc.player == null) {
            return;
        }
        GamemodeChanger.mc.playerController.setGameType(GameType.SURVIVAL);
    }

}
