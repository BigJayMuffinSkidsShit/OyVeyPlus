package me.muffin.oyveyplus.api.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;

public class EatingUtil {
    private static boolean eating;
    private static long eatingMs;
    static Minecraft mc = Minecraft.getMinecraft();
    //Eats the given item if u have it
    public static boolean eatItem(Item item, boolean sleepUntilDone) {


        InventoryUtil.switchItem(InventoryUtil.getSlot(item));

        if (mc.player.inventory.getCurrentItem().getItem() == item) {
            if (mc.currentScreen != null) {
                mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
            } else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            }
            eating = true;

            //Set eating to false after a timeout in case the event never got called
            if (!sleepUntilDone) {
                new Thread() {
                    public void run() {
                        eatingMs = System.currentTimeMillis();
                        long check = eatingMs;

                        if (eatingMs == check) {
                            eating = false;
                        }
                    }
                }.start();
            }

            //Sleep this thread if the sleepUntilDone is true
            else {

                eating = false;
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean eatItemBySlot(int slot, EnumHand hand, boolean sleepUntilDone) {


       // InventoryUtil.switchItem(slot);


            if (mc.currentScreen != null) {
                mc.playerController.processRightClick(mc.player, mc.world, hand);
            } else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            }
            eating = true;

            //Set eating to false after a timeout in case the event never got called
            if (!sleepUntilDone) {
                new Thread() {
                    public void run() {
                        eatingMs = System.currentTimeMillis();
                        long check = eatingMs;

                        if (eatingMs == check) {
                            eating = false;
                        }
                    }
                }.start();
            }

            //Sleep this thread if the sleepUntilDone is true
            else {

                eating = false;
            }

            return true;

    }
}
