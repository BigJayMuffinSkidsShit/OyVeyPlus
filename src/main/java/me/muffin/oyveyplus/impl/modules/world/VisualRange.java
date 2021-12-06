package me.muffin.oyveyplus.impl.modules.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.utils.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;


//TODO: this module really buggy. when ppl go out of visual range it spams a client message :/ probably will fix it later
public class VisualRange extends Module {
    public VisualRange() {
        super("VisualRange","Really buggy could do with some fixes", Category.World);
    }

    public List<EntityPlayer> loadedEntities = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate() {
        for(EntityPlayer entity : mc.world.playerEntities) {
            if(entity == mc.player) continue;
            if(!loadedEntities.contains(entity)) {
                MessageUtil.instance.addMessage(ChatFormatting.GREEN + entity.getDisplayNameString() + ChatFormatting.RESET + " has entered your visual range.", false);
                loadedEntities.add(entity);
            }
        }
        try {
            for(EntityPlayer entity : loadedEntities) {
                if(!mc.world.playerEntities.contains(entity)) {
                    MessageUtil.instance.addMessage(ChatFormatting.GREEN + entity.getDisplayName().getFormattedText() + ChatFormatting.RESET + " has left your visual range.", false);
                    loadedEntities.add(entity);
                }
            }
        }catch (Exception ignored) {} //So it doesn't crash
    }
}
