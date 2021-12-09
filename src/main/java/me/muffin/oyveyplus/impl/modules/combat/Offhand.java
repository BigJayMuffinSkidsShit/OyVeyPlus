package me.muffin.oyveyplus.impl.modules.combat;

import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.client.event.ColorHandlerEvent;

public class Offhand extends Module {
    private ItemStack itemStack;

    public Offhand() {
        super("Offhand", Category.Combat);
    }

    public Setting<String> item = this.register("Offhand","Crystal","Crystal","Totem", "Crystal");
    public Setting<Double> totemHealth = this.register("Totem Health",13,0,36,0);
    public Setting<Boolean> delay = this.register("Delay",false);

    private boolean switching = false;
    private int last_slot;

    public void onUpdate() {
        if (mc.player.getHealth() + mc.player.getAbsorptionAmount()  > totemHealth.getValue()) {
            if (item.getValue().equals("Crystal")) {
                swapItems(getItemSlot(Items.END_CRYSTAL),0);
                return;
            }
            if (item.getValue().equals("Totem")) {
                swapItems(getItemSlot(Items.TOTEM_OF_UNDYING), delay.getValue() ? 1 : 0);
            }
        }
    }

    public void swapItems(int slot, int step) {
        if (slot == -1) return;
        if (step == 0) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        }
        if (step == 1) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            switching = true;
            last_slot = slot;
        }
        if (step == 2) {
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            switching = false;
        }

        mc.playerController.updateController();
    }

    private int getItemSlot(Item input) {
        if (input == mc.player.getHeldItemOffhand().getItem()) return -1;
        for(int i = 36; i >= 0; i--) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if(item == input) {
                if (i < 9) {
                    if (input == Items.GOLDEN_APPLE) {
                        return -1;
                    }
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }

}
