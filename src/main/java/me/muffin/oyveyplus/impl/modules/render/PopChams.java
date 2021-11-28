package me.muffin.oyveyplus.impl.modules.render;

import com.mojang.authlib.GameProfile;
import me.muffin.oyveyplus.api.event.events.EventPacket;
import me.muffin.oyveyplus.api.event.events.PacketEvent;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.NordTessellator;
import me.muffin.oyveyplus.api.utils.PopChamsUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class PopChams extends Module {
    public  final Setting<Boolean> self = register("Self", false);
    public  final Setting<Double> rL = register("RedLine", 255, 0, 255,0);
    public  final Setting<Double> gL = register("GreenLine", 26, 0, 255,0);
    public  final Setting<Double> bL = register("BlueLine", 42, 0, 255,0);
    public final Setting<Double> aL = register("AlphaLine", 42, 0, 255,0);

    public  final Setting<Double> rF = register("RedFill", 255, 0, 255,0);
    public final Setting<Double> gF = register("GreenFill", 26, 0, 255,0);
    public final Setting<Double> bF = register("BlueFill", 42, 0, 255,0);
    public final Setting<Double> aF = register("AlphaFill", 42, 0, 255,0);

    public  final Setting<Double> fadestart = register("FadeStart", 200, 0, 3000,0);
    public  final Setting<Double> fadetime = register("FadeStart", .5, .0,2d,0);
    public  final Setting<Boolean> onlyOneEsp = register("OnlyOneEsp", true);
    public  final Setting<Boolean> rainbow = register("Rainbow", false);
    public static PopChams instance = new PopChams();
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public PopChams() {
        super("PopChams", "Renders when some1 pops", Category.Render);
    }

    public PopChams getInstance() {
        return instance;
    }

    public void setInstance(PopChams instance) {
        this.instance = instance;
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity(PopChams.mc.world) != null && (self.getValue() || packet.getEntity(PopChams.mc.world).getEntityId() != PopChams.mc.player.getEntityId())) {
                final GameProfile profile = new GameProfile(mc.player.getUniqueID(), "");
                (this.player = new EntityOtherPlayerMP(mc.world, profile)).copyLocationAndAnglesFrom(packet.getEntity(mc.world));
                this.playerModel = new ModelPlayer(0.0f, false);
                this.startTime = System.currentTimeMillis();
                playerModel.bipedHead.showModel = true;
                playerModel.bipedBody.showModel = true;
                playerModel.bipedLeftArmwear.showModel = true;
                playerModel.bipedLeftLegwear.showModel = true;
                playerModel.bipedRightArmwear.showModel = true;
                playerModel.bipedRightLegwear.showModel = true;

                alphaFill = aF.getValue();
                alphaLine = aL.getValue();
                if (!onlyOneEsp.getValue()) {
                    PopChamsUtil p = new PopChamsUtil(player, playerModel, startTime, alphaFill, alphaLine);
                }

            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (onlyOneEsp.getValue()) {
            if (player == null || mc.world == null || mc.player == null) {
                return;
            }
            GL11.glLineWidth(1.0f);
            Color lineColorS = new Color(rL.getValue().intValue(), bL.getValue().intValue(), gL.getValue().intValue(), aL.getValue().intValue());
            Color fillColorS = new Color(rF.getValue().intValue(), bF.getValue().intValue(), gF.getValue().intValue(), aF.getValue().intValue());
            int lineA = lineColorS.getAlpha();
            int fillA = (fillColorS).getAlpha();
            final long time = System.currentTimeMillis() - this.startTime - ((Number) fadestart.getValue()).longValue();
            if (System.currentTimeMillis() - this.startTime > ((Number) fadestart.getValue()).longValue()) {
                double normal = this.normalize((double) time, 0.0, ((Number) fadetime.getValue()).doubleValue());
                normal = MathHelper.clamp(normal, 0.0, 1.0);
                normal = -normal + 1.0;
                lineA *= (int) normal;
                fillA *= (int) normal;
            }
            Color lineColor = newAlpha(lineColorS, lineA);
            Color fillColor = newAlpha(fillColorS, fillA);
            if (this.player != null && this.playerModel != null) {
                NordTessellator.prepareGL();
                GL11.glPushAttrib(1048575);
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                if (alphaFill > 1) alphaFill -= fadetime.getValue();
                Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int) alphaFill);

                if (alphaLine > 1) alphaLine -= fadetime.getValue();
                Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int) alphaLine);
                glColor(fillFinal);
                GL11.glPolygonMode(1032, 6914);
                renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float) this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                glColor(outlineFinal);
                GL11.glPolygonMode(1032, 6913);
                renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float) this.player.ticksExisted, player.rotationYawHead, this.player.rotationPitch, 1);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                NordTessellator.releaseGL();
            }
        }
    }

    double normalize(final double value, final double min, final double max) {
        return (value - min) / (max - min);
    }

    public static void renderEntity(final EntityLivingBase entity, final ModelBase modelBase, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (mc.getRenderManager() == null) {
            return;
        }
        final float partialTicks = mc.getRenderPartialTicks();
        final double x = entity.posX - mc.getRenderManager().viewerPosX;
        double y = entity.posY - mc.getRenderManager().viewerPosY;
        final double z = entity.posZ - mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        final float interpolateRotation = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        final float interpolateRotation2 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        final float rotationInterp = interpolateRotation2 - interpolateRotation;
        final float renderPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        renderLivingAt(x, y, z);
        final float f8 = handleRotationFloat(entity, partialTicks);
        prepareRotations(entity);
        final float f9 = prepareScale(entity, scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9, entity);
        modelBase.render(entity, limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9);
        GlStateManager.popMatrix();
    }

    public static void prepareTranslate(final EntityLivingBase entityIn, final double x, final double y, final double z) {
        renderLivingAt(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ);
    }

    public static void renderLivingAt(final double x, final double y, final double z) {
        GlStateManager.translate((float)x, (float)y, (float)z);
    }

    public static float prepareScale(final EntityLivingBase entity, final float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        final double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        final double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale(scale + widthX, scale * entity.height, scale + widthZ);
        final float f = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return f;
    }

    public static void prepareRotations(final EntityLivingBase entityLivingBase) {
        GlStateManager.rotate(180.0f - entityLivingBase.rotationYaw, 0.0f, 1.0f, 0.0f);
    }

    public static float interpolateRotation(final float prevYawOffset, final float yawOffset, final float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }

    public static Color newAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    public static float handleRotationFloat(final EntityLivingBase livingBase, final float partialTicks) {
        return 0.0f;
    }
}

