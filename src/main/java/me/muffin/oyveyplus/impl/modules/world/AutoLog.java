package me.muffin.oyveyplus.impl.modules.world;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;

public class AutoLog extends Module {
    public AutoLog() {
        super("AutoLog", Category.World);
    }
    private static AutoLog INSTANCE = new AutoLog();
    public Setting<Double> health = this.register("Health",5,0,36, 0);
    public Setting<Boolean> logout = this.register("Logout",true);

    public static AutoLog getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(AutoLog INSTANCE) {
        AutoLog.INSTANCE = INSTANCE;
    }

    @Override
    public void onTick() {
        if (AutoLog.mc.player.getHealth() <= this.health.getValue().floatValue()) {
            disable();
            AutoLog.mc.player.connection.sendPacket(new SPacketDisconnect(new TextComponentString("AutoLogged")));
            if (this.logout.getValue().booleanValue()) {
                this.disable();
            }
        }
    }
}
