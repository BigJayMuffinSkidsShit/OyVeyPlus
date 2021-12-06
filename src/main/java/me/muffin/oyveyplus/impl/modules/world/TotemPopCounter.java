package me.muffin.oyveyplus.impl.modules.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.utils.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class TotemPopCounter extends Module {
    public TotemPopCounter() {
        super("TotemPopCounter", Category.World);
    }

    public static HashMap<String, Integer> TotemPopContainer = new HashMap();
    public static TotemPopCounter INSTANCE = new TotemPopCounter();

    public static TotemPopCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TotemPopCounter();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        TotemPopContainer.clear();
    }

    public void onDeath(EntityPlayer player) {
        if (TotemPopContainer.containsKey(player.getName())) {
            int l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                MessageUtil.instance.addMessage(ChatFormatting.WHITE + player.getName() + ChatFormatting.GREEN + " just got ezzed after popping " + ChatFormatting.WHITE + l_Count + ChatFormatting.GREEN + " totem " + ChatFormatting.WHITE,false);
            } else {
                MessageUtil.instance.addMessage(ChatFormatting.WHITE + player.getName() + ChatFormatting.GREEN + " just got ezzed after popping " + ChatFormatting.WHITE + l_Count + ChatFormatting.GREEN + " totems " + ChatFormatting.WHITE, false);
            }
        }
    }

    public void onTotemPop(EntityPlayer player) {
        if (this == null) {
            return;
        }
        if (TotemPopCounter.mc.player.equals(player)) {
            return;
        }
        int l_Count = 1;
        if (TotemPopContainer.containsKey(player.getName())) {
            l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.put(player.getName(), ++l_Count);
        } else {
            TotemPopContainer.put(player.getName(), l_Count);
        }
        if (l_Count == 1) {
            MessageUtil.instance.addMessage(ChatFormatting.WHITE + player.getName() + ChatFormatting.GREEN + " lmao this noob just popped " + ChatFormatting.WHITE + l_Count + ChatFormatting.GREEN + " totem " + ChatFormatting.WHITE, false);
        } else {
            MessageUtil.instance.addMessage(ChatFormatting.WHITE + player.getName() + ChatFormatting.GREEN + " lmao this noob just popped " + ChatFormatting.WHITE + l_Count + ChatFormatting.GREEN + " totems " + ChatFormatting.WHITE, false);
        }
    }

}
