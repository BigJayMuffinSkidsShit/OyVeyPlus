package me.muffin.oyveyplus.api.event;

import me.muffin.oyveyplus.api.event.events.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.wrapper.Wrapper;

public class EventHandler implements Wrapper {

    @SubscribeEvent public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) OyVeyPlus.EVENTBUS.post(new EventKey(Keyboard.getEventKey()));
    }

    @SubscribeEvent public void onUpdate(TickEvent event) {
        OyVeyPlus.EVENTBUS.post(new EventTick());
    }

    @SubscribeEvent public void onWorldRender(RenderWorldLastEvent event) {
        OyVeyPlus.EVENTBUS.post(new EventRenderWorldLast(event.getContext(), event.getPartialTicks()));
    }
}
