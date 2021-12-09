package me.muffin.oyveyplus.impl.modules.render;

import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemFOV extends Module {
	public Setting<Double> fov = this.register("FOV",130,70,200,1);

	public ItemFOV() {
		super("ItemFOV", "what?", Category.Render);
	}

	@SubscribeEvent
    public void onItemFOV(EntityViewRenderEvent.FOVModifier event) {
    	event.setFOV((float) fov.getValue().intValue());
    }
}