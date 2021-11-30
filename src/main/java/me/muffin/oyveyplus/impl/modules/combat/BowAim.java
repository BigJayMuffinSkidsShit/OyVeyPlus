package me.muffin.oyveyplus.impl.modules.combat;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.utils.EntityUtil;
import me.muffin.oyveyplus.api.utils.MathUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.Vec3d;

public class BowAim extends Module {
    public BowAim() {
        super("BowAim", "aims the bow automatically", Category.Combat);
    }

    @Override
    public void onUpdate() {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
            EntityPlayer player = null;
            float tickDis = 100f;
            for (EntityPlayer p : mc.world.playerEntities) {
                if (p instanceof EntityPlayerSP || OyVeyPlus.friendManager.isFriend(p.getName())) continue;
                float dis = p.getDistance(mc.player);
                if (dis < tickDis) {
                    tickDis = dis;
                    player = p;
                }
            }
            if (player != null) {
                Vec3d pos = EntityUtil.getInterpolatedPos(player, mc.getRenderPartialTicks());
                float[] angels = MathUtil.calcAngle(EntityUtil.getInterpolatedPos(mc.player, mc.getRenderPartialTicks()), pos);
                //angels[1] -=  calcPitch(tickDis);
                mc.player.rotationYaw = angels[0];
                mc.player.rotationPitch = angels[1];
            }
        }
    }
}
