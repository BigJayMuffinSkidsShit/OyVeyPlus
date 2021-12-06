package me.muffin.oyveyplus.impl.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.BlockUtil;
import me.muffin.oyveyplus.api.utils.InventoryUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class Burrow
        extends Module {

    private final Setting<Boolean> rotate = this.register("Rotate", false);
    private final Setting<Double> offset = this.register("Offset", 1.1F, -10.0F, 10.0F,1);
    private final Setting<Boolean> sneak = this.register("Sneak", false);

    private final Setting<Boolean> obsidian = this.register("Obsidian", true);
    private final Setting<Boolean> echest = this.register("Ender chest", true);

    private BlockPos originalPos;
    private int oldSlot = -1;

    public Burrow() {
        super("Burrow", "ez", Category.World);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // Save our original pos
        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        // If we can't place in our actual post then toggle and return
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) ||
                intersectsWithEntity(this.originalPos)) {
            toggle();
            return;
        }

        // Save our item slot
        oldSlot = mc.player.inventory.currentItem;
    }

    @Override
    public void onUpdate() {
        // If we don't have obsidian in hotbar toggle and return
        int blockSlot = -1;

        if(this.obsidian.getValue()) {
            blockSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        }

        if(this.echest.getValue() && blockSlot == -1) {
            blockSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        }

        if (blockSlot == -1) {
            mc.player.sendMessage((ITextComponent) new CPacketChatMessage("[" + ChatFormatting.RED + "Burrow" + ChatFormatting.RESET + "] Can't find block in hotbar!"));
            toggle();
            return;
        }

        // Change to obsidian slot
        InventoryUtil.switchToHotbarSlot(blockSlot, false);

        // Fake jump
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));

        // Sneak option.
        boolean sneaking = mc.player.isSneaking();
        if (sneak.getValue()) {
            if (sneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
        }

        // Place block
        BlockUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);

        // Rubberband
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));

        // SwitchBack
        InventoryUtil.switchToHotbarSlot(oldSlot, false);

        // Stop sneak if the option was enabled.
        if (sneak.getValue()) {
            if (sneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
        // AutoDisable
        toggle();
    }

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }
}

