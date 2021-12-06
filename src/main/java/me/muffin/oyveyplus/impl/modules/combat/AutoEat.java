package me.muffin.oyveyplus.impl.modules.combat;

import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.EatingUtil;
import me.muffin.oyveyplus.api.utils.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class AutoEat extends Module {

    public Setting<String> hand1 = this.register("Hand","OFF_HAND","OFF_HAND","MAIN_HAND");
    public Setting<Double> minHealth = this.register("minHealth", 35,0,36, 1);
    private static boolean offhanded = false;
    public AutoEat() {
        super("AutoEat", "autototem", Category.Combat);
        this.delay=20;
    }

    @Override
    public void Update() {
        if (mc.player == null) {
            return;
        }
        if(mc.player.getHealth() + mc.player.getAbsorptionAmount()<= minHealth.getValue())
        {
        EnumHand hand2=EnumHand.MAIN_HAND;

        if(String.valueOf(hand1.getValue()).contains("OFF_HAND"))
        {
            hand2=EnumHand.OFF_HAND;
        }
        if(String.valueOf(hand1.getValue()).contains("MAIN_HAND"))
        {
            hand2=EnumHand.MAIN_HAND;

        }

            if(hand2==EnumHand.OFF_HAND)
            {
                if (mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {

                    if (mc.player.getHeldItemOffhand().getItem() != Items.AIR) {
                        int space = 0;
                        space = InventoryUtil.getSlot(Items.GOLDEN_APPLE);
                        InventoryUtil.clickSlot(InventoryUtil.getSlot(Items.GOLDEN_APPLE));
                        InventoryUtil.clickSlot(45);
                        InventoryUtil.clickSlot(space);
                    } else {
                        InventoryUtil.clickSlot(InventoryUtil.getSlot(Items.GOLDEN_APPLE));
                        InventoryUtil.clickSlot(45);
                    }
                    offhanded=true;
                }
                EatingUtil.eatItemBySlot(45, hand2, false  );
            }else {
                if (mc.player.getHeldItemMainhand().getItem() != Items.GOLDEN_APPLE) {

                    if (mc.player.getHeldItemMainhand().getItem() != Items.AIR) {
                        int space = 0;
                        if(offhanded)
                        {
                            offhanded=false;
                            space = 45;
                            InventoryUtil.clickSlot(45);
                        }else {
                            space = InventoryUtil.getSlot(Items.GOLDEN_APPLE);
                            InventoryUtil.clickSlot(InventoryUtil.getSlot(Items.GOLDEN_APPLE));
                        }

                        InventoryUtil.clickSlot(mc.player.inventory.currentItem);
                        InventoryUtil.clickSlot(space);
                    } else {
                        if(offhanded)
                        {
                            offhanded=false;
                            InventoryUtil.clickSlot(45);
                        }else {
                            InventoryUtil.clickSlot(InventoryUtil.getSlot(Items.GOLDEN_APPLE));
                        }
                        //InventoryUtil.clickSlot(InventoryUtil.getSlot(Items.GOLDEN_APPLE));
                        InventoryUtil.clickSlot(mc.player.inventory.currentItem);
                    }

                }
                if(!mc.player.capabilities.isCreativeMode)
                {
                    EatingUtil.eatItemBySlot(mc.player.inventory.currentItem, hand2, false  );
                }

            }


           // EatingUtil.eatItemBySlot(45, hand2, false  );

       }else{
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        }

        super.Update();
    }
}
