package me.muffin.oyveyplus.impl.modules.movement;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.module.Module.Category;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.NoClipData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class NoClip extends Module{

	private static final NoClipData data = new NoClipData();
	public Setting<Boolean> offground = this.register("OffGround", true);
	
	public NoClip() {
		super("Noclip", "noclip", Category.Combat);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		/*
        if (mc.player.posY < 6 && !mc.player.isElytraFlying() && mc.player.onGround) {
            Minecraft.getMinecraft().player.setSneaking(true);
            EntityUtil.packetJump((boolean)offground.GetValue());
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, ((mc.player.posY + 1.1) * -1), mc.player.posZ, (boolean) offground.GetValue()));
            EntityUtil.stopSneaking(false);
            disable();
        }
        */
	//	System.out.print*()
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		player.noClip = true;
		player.fallDistance = 0;
		player.onGround = false;
		
		player.capabilities.isFlying = false;
		player.setVelocity(0, 0, 0);
		
		float speed = 0.2F;
		player.capabilities.setFlySpeed(speed);
		
		Minecraft.getMinecraft().player.setJumping(true);
		player.addVelocity(0, speed, 0);
		Minecraft.getMinecraft().player.setSneaking(true);
		player.addVelocity(0, -speed, 0);
		super.onUpdate();
	}
    public static void sendNoClipData() {
        ByteBuf buf = Unpooled.buffer().writeBoolean(data.noClip());
        CPacketCustomPayload payload = new CPacketCustomPayload("NOCLIP", new PacketBuffer(buf));
        Minecraft.getMinecraft().player.connection.sendPacket(payload);
    }
}
