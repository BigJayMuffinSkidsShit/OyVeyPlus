package me.muffin.oyveyplus.impl.mixin.mixins;

import java.io.IOException;

import me.muffin.oyveyplus.api.event.events.EventNetworkPacketEvent;
import me.muffin.oyveyplus.api.event.events.PacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
	
    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendPacketPre(Packet<?> packet, CallbackInfo info) {
        PacketEvent.Send event = new PacketEvent.Send(0, packet);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="RETURN")}, cancellable=true)
    private void onSendPacketPost(Packet<?> packet, CallbackInfo info) {
        PacketEvent.Send event = new PacketEvent.Send(1, packet);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method={"channelRead0"}, at={@At(value="HEAD")}, cancellable=true)
    private void onChannelReadPre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo info) {
        PacketEvent.Receive event = new PacketEvent.Receive(0, packet);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    private void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_, CallbackInfo info) {
        if (p_exceptionCaught_2_ instanceof IOException) info.cancel();
    }
    
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelRead(ChannelHandlerContext context, Packet<?> p_Packet, CallbackInfo callbackInfo)
    {
        EventNetworkPacketEvent l_Event = new EventNetworkPacketEvent(p_Packet);
        MinecraftForge.EVENT_BUS.post(l_Event);

        if (l_Event.isCanceled())

        {
            callbackInfo.cancel();
        }
    }
}
