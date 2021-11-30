package me.muffin.oyveyplus.impl.modules.world;

import me.muffin.oyveyplus.api.module.Module;
import net.minecraft.network.play.client.CPacketChatMessage;

public class Spammer extends Module {
    public Spammer() {
        super("Spammer", "spams :/", Category.World);
    }

    public void onUpdate() {
        mc.player.connection.sendPacket(new CPacketChatMessage("ez mad OyVeyPlus on top noob!"));
    }
}
