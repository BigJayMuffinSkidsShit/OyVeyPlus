package me.muffin.oyveyplus.impl.modules.world;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.BlockUtil;
import me.muffin.oyveyplus.api.utils.Timer;
import me.muffin.oyveyplus.impl.mixin.mixins.accessors.AccessorKeybinding;
import net.minecraft.util.math.BlockPos;

public class BurrowBypass extends Module {
    public BurrowBypass() {
        super("BurrowBypass", Category.World);
    }
    BlockPos position;
    int time;
    BlockPos pos;
    int stages;
    int delay, pdelay, stage, jumpdelay,toggledelay;
    boolean jump;
    Timer timer = new Timer();

    @Override
    public void onEnable() {
        position = new BlockPos(mc.player.getPosition());

        time = 0;
        pos = null;
        stages = 0;

        pdelay = 0;
        stage = 1;
        toggledelay = 0;
        jumpdelay = 0;
        timer.reset();
        jump = false;

        position = null;
        delay = 0;

    }

    @Override public void onUpdate() {
        if (stage == 1) {
            delay++;
            ((AccessorKeybinding) mc.gameSettings.keyBindJump).isKeyDown();
            OyVeyPlus.TIMER_VALUE = 30;
            if (delay >= 42) {
                stage = 2;
                delay = 0;
                OyVeyPlus.TIMER_VALUE = 1;
                ((AccessorKeybinding) mc.gameSettings.keyBindJump).isKeyDown();
            }
        }
        if (stage == 2){
            OyVeyPlus.TIMER_VALUE = 1;
            if (mc.player.onGround) ((AccessorKeybinding) mc.gameSettings.keyBindJump).isKeyDown();;
            BlockUtil.placeBlock(position);
            pdelay++;
            if (pdelay >= 30){
                stage = 3;
                pdelay = 0;
                ((AccessorKeybinding) mc.gameSettings.keyBindJump).isKeyDown();
                OyVeyPlus.TIMER_VALUE = 1;

            }
        }
        if (stage == 3){
            toggledelay++;
            OyVeyPlus.TIMER_VALUE = 30;
            ((AccessorKeybinding) mc.gameSettings.keyBindJump).isKeyDown();
            if (toggledelay >= 25) {
                mc.player.motionY -= 0.4;
                OyVeyPlus.TIMER_VALUE = 1;
                ((AccessorKeybinding) mc.gameSettings.keyBindJump).isKeyDown();
                setEnabled(false);
            }
        }
    }

}
