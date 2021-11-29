package me.muffin.oyveyplus.impl.modules.client;

import me.muffin.oyveyplus.DiscordRpc;
import me.muffin.oyveyplus.api.module.Module;

public class RPC extends Module {
    public static RPC INSTANCE;
    public RPC() {
        super("RPC", Category.Client);
    }

    @Override
    public void onEnable() {
        DiscordRpc.start();
    }

    @Override
    public void onDisable() {
        DiscordRpc.stop();
    }
}
