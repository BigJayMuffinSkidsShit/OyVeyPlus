package me.muffin.oyveyplus.api.event;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.event.events.EventKey;
import me.muffin.oyveyplus.api.event.events.EventRenderWorldLast;
import me.muffin.oyveyplus.api.event.events.EventTick;
import me.muffin.oyveyplus.api.wrapper.Wrapper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

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
