package me.muffin.oyveyplus.api.utils;

import me.muffin.oyveyplus.api.wrapper.Wrapper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public enum MessageUtil implements Wrapper {
    instance;

    public void addMessage(String message, boolean silent) {
        String prefix = TextFormatting.AQUA + "<OyVey +> " + TextFormatting.RESET;
        if (silent) mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(prefix + message), 1); else mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + message));
    }
}
