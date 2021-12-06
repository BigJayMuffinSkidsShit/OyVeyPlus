package me.muffin.oyveyplus.impl.modules.combat;

import me.muffin.oyveyplus.api.event.events.EventPacket;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.CrystalUtils;
import me.muffin.oyveyplus.api.utils.EntityUtil;
import me.muffin.oyveyplus.api.utils.InventoryUtil;
import me.muffin.oyveyplus.api.utils.MessageUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Comparator;

public class CrystalAura extends Module {
    public CrystalAura() {
        super("CrystalAura", Category.Combat);
    }

    public Setting<String> placeMode = this.register("PlcMode","MostDmg","Closest","Priority","MostDmg");
    public Setting<String> breakMode = this.register("BrkMode","Always","Always","Smart");
    public Setting<String> logicMode = this.register("LgcMode","PlaceBreak","PlaceBreak","BreakPlace");
    public Setting<Boolean> sync = this.register("Sync",true);

    public Setting<Double> placeRange = this.register("PlaceRange",4.2f,0,6,1);
    public Setting<Double> breakRange = this.register("BreakRange", 4.2f,0,6,1);
    public Setting<Double> targetRange = this.register("TargetRange",10,0,15,0);
    public Setting<Double> wallRange = this.register("WallsRange",3.5,0,5,1);

    public Setting<Double> minDmg = this.register("MinDmg",4,0,20,0);
    public Setting<Double> maxSelfDmg = this.register("MaxSDmg",4,0,20,0);

    public Setting<Boolean> place = this.register("Place",true);
    public Setting<String> placeHand = this.register("PlcHand","Offhand","MainHand","OffHand");
    public Setting<Double> placeDelay = this.register("PlcDelay",1,0,20,0);
    public Setting<Boolean> rotate = this.register("Rotations",false);

    public Setting<Boolean> _break = this.register("Break",true);
    public Setting<String> breakHand =  this.register("BrkHand","MainHand","Offhand","MainHand");
    public Setting<Double> breakDelay = this.register("BrkDelay",1,0,20,0);

    public Setting<String> switchMode = this.register("Switch","Silent","Normal","None");

    public Setting<Boolean> facePlace = this.register("FacePlace",true);
    public Setting<Double> facePlaceHealth = this.register("FPHealth",8,0,36,0);

    public Setting<Boolean> armourBreak = this.register("ArmourBreak",true);
    public Setting<Double> armourBreakHealth = this.register("ABHlth",20,0,100,0);

    public Setting<Boolean> secondCheck = this.register("SecondCheck",true);

    public Setting<Boolean> breakSwing = this.register("BreakSwing",false);
    public Setting<Boolean> placeSwing = this.register("PlaceSwing",true);
    public Setting<Boolean> packetSwing = this.register("PacketSwing",false);

    public ArrayList<ItemEndCrystal> placedCrystal = new ArrayList<>();
    public EntityPlayer target = null;

    private int placeTicks;
    private int breakTicks;

    public void onEnable() {
        placeTicks = 0;
        breakTicks = 0;
        placedCrystal.clear();

        MinecraftForge.EVENT_BUS.register(listener1);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(listener1);
        placedCrystal.clear();
    }

    public void onUpdate() {
        if (mc.player == null && mc.world == null) return;

        findNewTarget();

        if(target != null) {
            MessageUtil.instance.addMessage("[" +  target.getDisplayName().getFormattedText() + TextFormatting.GRAY + "]", true);
        } else {
            MessageUtil.instance.addMessage("",true);
        }

        doAutoCrystal();
    }

    @EventHandler
    private final Listener<EventPacket.Receive> listener1 = new Listener<>(event -> {
        if(event.getPacket() instanceof SPacketSoundEffect && sync.getValue()) {
            SPacketSoundEffect packet = event.getPacket();

            if(packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for(Entity entity : mc.world.loadedEntityList) {
                    if (!(entity instanceof EntityEnderCrystal) || !(entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) <= Double.longBitsToDouble(Double.doubleToLongBits(0.03533007623236061) ^ 0x7FE016C8A3F762CFL))) {
                        continue;
                    }

                    entity.setDead();
                }
            }
        }
    });

    private void doAutoCrystal() {
        switch (logicMode.getValue()) {
            case "PlaceBreak": {
                if(place.getValue() && target != null) {
                    placeCrystal();
                }
                if(_break.getValue()) {
                    breakCrystal();
                }
                placedCrystal.clear();
                break;
            }
            case "BreakPlace": {
                if(_break.getValue()) {
                    breakCrystal();
                }
                placedCrystal.clear();
                if(place.getValue() && target != null) {
                    placeCrystal();
                }
                break;
            }
        }
    }

    private void placeCrystal() {
        if(placeTicks++ <= placeDelay.getValue().intValue()) {
            return;
        }

        placeTicks = 0;

        if(!isValidItemsInHand()) {
            return;
        }

       // if(switchMode.getValEnum().equals(SwitchModes.None) && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) return BlockPos.ORIGIN;

        EnumHand hand = null;
        BlockPos placePos = null;
        double maxDamage = 0.5;

        for(BlockPos pos : CrystalUtils.getSphere((float) placeRange.getValue().intValue(), true, false)) {
            final double targetDMG = EntityUtil.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, target);
            final double selfDMG = EntityUtil.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, mc.player) + 2.0;

            if(CrystalUtils.canPlaceCrystal(pos, this.secondCheck.getValue(),false, false) && ((targetDMG >= this.minDmg.getValue().intValue() || (targetDMG >= target.getHealth() && target.getHealth() <= this.facePlaceHealth.getValue().intValue() || (target.getHealth() <= armourBreakHealth.getValue())) && ((EntityUtil.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, mc.player) + 2.0 < mc.player.getHealth() && selfDMG < targetDMG))))) {
                if(maxDamage > targetDMG) continue;

                if(target.isDead) continue;

                placePos = pos;
                maxDamage = targetDMG;
            }
        }

        if(maxDamage == 0.5) {
            return;
        }

        final int crystalSlot = InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL, false);
        final int oldSlot = mc.player.inventory.currentItem;

        switch ( String.valueOf(switchMode.getValue())) {
            case ("None"): {
                if(mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                    return;
                }
                break;
            }
            case ("Normal"): {
                if(mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                    if(crystalSlot == -1) {
                        return;
                    } else {
                        InventoryUtil.switchToHotbarSlot(crystalSlot, false);
                    }
                }
                break;
            }
            case ("Silent"): {
                if(mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
                    if(crystalSlot == -1) {
                        return;
                    } else {
                        InventoryUtil.switchToHotbarSlot(crystalSlot, true);
                    }
                }
                break;
            }
        }

        if(mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            if (mc.player.isHandActive()) {
                hand = mc.player.getActiveHand();
            }

            if (rotate.getValue()) {
                //rotate
            }

            final EnumFacing facing = EnumFacing.UP;
            boolean offhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, facing, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
            mc.playerController.updateController();

            if(placeSwing.getValue()) {
                swingItem(false);
            }

            if (switchMode.getValue().equals("Silent") && oldSlot != -1) {
                InventoryUtil.switchToHotbarSlot(oldSlot, true);
            }
        }
    }



    public void breakCrystal() {
        if(breakTicks++ <= breakDelay.getValue().intValue()) {
            return;
        }

        EntityEnderCrystal crystal = mc.world.loadedEntityList.stream()
                .filter(this::isValidCrystal)
                .map(entity -> (EntityEnderCrystal) entity)
                .min(Comparator.comparing(entityEnderCrystal -> mc.player.getDistance(entityEnderCrystal)))
                .orElse(null);

        if(crystal == null) {
            return;
        }

        final int swordSlot = InventoryUtil.findItemInventorySlot(Items.DIAMOND_SWORD, false);
        final int oldSlot = mc.player.inventory.currentItem;

        mc.playerController.attackEntity(mc.player, crystal);
        if(breakSwing.getValue()) {
            swingItem(true);
        }
        breakTicks = 0;
    }

    private boolean isValidItemsInHand() {
        if(mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            return true;
        } else if(mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return true;
        } else {
            return !switchMode.getValue().equals("None");
        }
    }

    public void findNewTarget() {
        target = (EntityPlayer) getNearTarget(mc.player);
    }

    public EntityLivingBase getNearTarget(EntityPlayer distanceTarget) {
        return mc.world.loadedEntityList.stream()
                .filter(this::isValidTarget)
                .map(entity -> (EntityLivingBase) entity)
                .min(Comparator.comparing(distanceTarget::getDistance))
                .orElse(null);
    }

    public boolean isValidTarget(Entity entity) {
        if (entity == null)
            return false;

        if (!(entity instanceof EntityLivingBase))
            return false;

        if (entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0f)
            return false;

        if (entity.getDistance(mc.player) > 20.0f)
            return false;

        if (entity instanceof EntityPlayer) {
            return entity != mc.player;
        }

        return false;
    }

    private boolean isValidCrystal(Entity entity) {
        if(!(entity instanceof EntityEnderCrystal)) {
            return false;
        }

        if (entity.getDistance(mc.player) > (!mc.player.canEntityBeSeen(entity) ? wallRange.getValue() : breakRange.getValue()))
            return false;

        switch (breakMode.getValue()) {
            case "Always":
                return true;
            case "Smart":
                if (target == null) {
                    return false;
                }

                float targetDMG = CrystalUtils.calculateDamage(mc.world, entity.posX + 0.5, entity.posY + 1.0, entity.posZ + 0.5, target, 0);
                float selfDMG = CrystalUtils.calculateDamage(mc.world, entity.posX + 0.5, entity.posY + 1.0, entity.posZ + 0.5, mc.player, 0);

                float minDMG = (float) this.minDmg.getValue().doubleValue();

                /// FacePlace
                if (target.getHealth() + target.getAbsorptionAmount() <= facePlaceHealth.getValue())
                    minDMG = 1f;

                if (targetDMG > minDMG && selfDMG < maxSelfDmg.getValue())
                    return true;

                break;
            default:
                break;
        }

        return false;
    }

    public void swingItem(boolean breakSwing) {
        {
            if (breakSwing) {
                block0:
                {
                    block3:
                    {
                        block1:
                        {
                            block2:
                            {
                                if (breakHand.getValue().equals("None")) break block0;

                                if (!packetSwing.getValue()) break block1;

                                if (!breakHand.getValue().equals("MainHand")) break block2;

                                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                                break block0;
                            }

                            if (!breakHand.getValue().equals("Offhand")) break block0;

                            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
                            break block0;
                        }

                        if (!breakHand.getValue().equals("MainHand")) break block3;

                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        break block0;
                    }

                    if (!breakHand.getValue().equals("Offhand")) break block0;

                    mc.player.swingArm(EnumHand.OFF_HAND);
                }
            } else {
                block0:
                {
                    block3:
                    {
                        block1:
                        {
                            block2:
                            {
                                if (placeHand.getValue().equals("None")) break block0;

                                if (!packetSwing.getValue()) break block1;

                                if (!placeHand.getValue().equals("MainHand")) break block2;

                                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                                break block0;
                            }

                            if (!placeHand.getValue().equals("Offhand")) break block0;

                            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
                            break block0;
                        }

                        if (!placeHand.getValue().equals("MainHand")) break block3;

                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        break block0;
                    }

                    if (!placeHand.getValue().equals("Offhand")) break block0;
                    mc.player.swingArm(EnumHand.OFF_HAND);
                }
            }
        }
    }
}
