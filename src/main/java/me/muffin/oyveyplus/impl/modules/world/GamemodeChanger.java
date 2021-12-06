package me.muffin.oyveyplus.impl.modules.world;

import me.muffin.oyveyplus.api.module.Module;
import net.minecraft.network.play.client.CPacketChatMessage;

public class GamemodeChanger extends Module {
    public GamemodeChanger() {
        super("GamemodeChanger", Category.World);
    }


    public void onEnable() {
        mc.player.connection.
                sendPacket(new CPacketChatMessage
                        ("/gamemode creative"));
        disable();
    }

}
