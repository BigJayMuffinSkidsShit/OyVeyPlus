package me.muffin.oyveyplus.impl.modules.combat;

import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.DamageUtil;
import me.muffin.oyveyplus.api.utils.EntityUtil;
import me.muffin.oyveyplus.api.utils.MathUtil;
import me.muffin.oyveyplus.api.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class KillAura extends Module {
    public KillAura() {
        super("Kill Aura", "kills ppl", Category.Combat);
    }

    public static Entity target;
    private final Timer timer = new Timer();
    public Setting<Double> range = register("Range", 6, 0, 7,2);
    public Setting<Boolean> delay = register("HitDelay", Boolean.TRUE);
    public Setting<Boolean> rotate = register("Rotate", Boolean.FALSE);
    public Setting<Boolean> onlySharp = register("SwordOnly", Boolean.TRUE);
    public Setting<Double> raytrace = register("Raytrace", 6, 1, 7, 2);
    public Setting<Boolean> players = register("Players", Boolean.TRUE);
    public Setting<Boolean> mobs = register("Mobs", Boolean.FALSE);
    public Setting<Boolean> animals = register("Animals", Boolean.FALSE);
    public Setting<Boolean> vehicles = register("Entities", Boolean.FALSE);
    public Setting<Boolean> projectiles = register("Projectiles", Boolean.FALSE);
    public Setting<Boolean> packet = register("Packet", Boolean.FALSE);

    public void onTick() {
        if (!rotate.getValue())
            doKillaura();
    }

    public void onUpdate() {
        doKillaura();
    }

    private void doKillaura() {
        if (onlySharp.getValue() && !EntityUtil.holdingWeapon(mc.player)) {
            target = null;
            return;
        }
        int wait = !delay.getValue() ? 0 : DamageUtil.getCooldownByWeapon(mc.player);
        if (!timer.passedMs(wait))
            return;
        target = getTarget();
        if (target == null)
            return;
        EntityUtil.attackEntity(target, packet.getValue(), true);
        timer.reset();
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = range.getValue().floatValue();
        double maxHealth = 36.0D;
        for (EntityPlayer entity : mc.world.playerEntities) {
            if (((!players.getValue() || !(entity instanceof EntityPlayer)) && (!animals.getValue() || !EntityUtil.isPassive(entity)) && (!mobs.getValue() || !EntityUtil.isMobAggressive(entity)) && (!vehicles.getValue() || !EntityUtil.isVehicle(entity)) && (!projectiles.getValue() || !EntityUtil.isProjectile(entity))) || (entity instanceof net.minecraft.entity.EntityLivingBase &&
                    EntityUtil.isntValid(entity, distance)))
                continue;
            if (!mc.player.canEntityBeSeen(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && mc.player.getDistanceSq(entity) > MathUtil.square(raytrace.getValue().floatValue()))
                continue;
            if (target == null) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (entity instanceof EntityPlayer && DamageUtil.isArmorLow(entity, 18)) {
                target = entity;
                break;
            }
            if (mc.player.getDistanceSq(entity) < distance) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
            if (EntityUtil.getHealth(entity) < maxHealth) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
        }
        return target;
    }

    public String getDisplayInfo() {
        if (target instanceof EntityPlayer)
            return target.getName();
        return null;
    }


}
