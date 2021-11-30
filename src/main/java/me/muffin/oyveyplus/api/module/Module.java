package me.muffin.oyveyplus.api.module;

import com.google.common.eventbus.Subscribe;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.event.events.EventRender;
import me.muffin.oyveyplus.api.event.events.EventTick;
import me.muffin.oyveyplus.api.event.events.Render3DEvent;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.MessageUtil;
import me.muffin.oyveyplus.api.wrapper.Wrapper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

/**
 * @author fuckyouthinkimboogieman
 */

public class Module implements Wrapper {
    private final String name, description;
    private final Category category;

    private int key = -1;

    private boolean enabled = false;

    public int delay=70;
    private int tmpdelay=0;
    
    public Module(String name, Category category) {
        super();
        this.name = name;
        this.description = "No description provided.";
        this.category = category;
    }

    public Module(String name, String description, Category category) {
        super();
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Category getCategory() {
        return this.category;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        if (isEnabled()) disable(); else enable();
    }

    public void enable() {
        setEnabled(true);
        onEnable();
    }

    public void disable() {
        setEnabled(false);
        onDisable();
    }

    public void onEnable() {
        OyVeyPlus.EVENTBUS.register(this);
        MessageUtil.instance.addMessage(ChatFormatting.GREEN + getName() + " enabled", false);
    }

    public void onDisable() {
        OyVeyPlus.EVENTBUS.unregister(this);
    	MessageUtil.instance.addMessage(ChatFormatting.RED + getName() + " disabled", false);
    }


    public void onUpdate() {
    }

    public void Update()
    {
        if(tmpdelay==delay) {
            tmpdelay=0;
            Update();
        }else {
            tmpdelay++;
        }
    }
    
    public void onTick() {
    }

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
    }

    public Setting<Boolean> register(String name, boolean value) {
        Setting<Boolean> setting = new Setting<>(name, this, getCategory(), value);
        OyVeyPlus.settingManager.register(setting);
        return setting;
    }

    public Setting<Double> register(String name, double value, double min, double max, int decimalPlaces) {
        Setting<Double> setting = new Setting<>(name, this, getCategory(), value, min, max, decimalPlaces);
        OyVeyPlus.settingManager.register(setting);
        return setting;
    }

    public Setting<String> register(String name, String value, String... modes) {
        Setting<String> setting = new Setting<>(name, this, getCategory(), Arrays.asList(modes), value);
        OyVeyPlus.settingManager.register(setting);
        return setting;
    }

    public enum Category {
        Combat,
        Movement,
        Render,
        Player,
        World,
        Client
    }
}
