/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minebot;

import static minebot.MineBot.isRightClick;
import static minebot.MineBot.sneak;
import static minebot.MineBot.whatAreYouLookingAt;
import minebot.util.ManagerTick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;

/**
 *
 * @author leijurv
 */
public class FoodManager extends ManagerTick {
    @Override
    protected boolean onTick0() {
        EntityPlayerSP p = Minecraft.theMinecraft.thePlayer;
        FoodStats fs = p.getFoodStats();
        if (!fs.needFood()) {
            return false;
        }
        int foodNeeded = 20 - fs.getFoodLevel();
        boolean anything = foodNeeded >= 3 && Minecraft.theMinecraft.thePlayer.getHealth() < 20;//if this is true, we'll just eat anything to get our health up
        ItemStack[] inv = p.inventory.mainInventory;
        byte slotForFood = -1;
        int worst = 10000;
        for (byte i = 0; i < 9; i++) {
            ItemStack item = inv[i];
            if (inv[i] == null) {
                continue;
            }
            if (item.getItem() instanceof ItemFood) {
                int healing = ((ItemFood) (item.getItem())).getHealAmount(item);
                //System.out.println(item + " " + healing);
                if (healing <= foodNeeded) {
                    slotForFood = i;
                }
                if (anything && healing > foodNeeded && healing < worst) {
                    slotForFood = i;
                }
            }
        }
        if (slotForFood != -1) {
            MineBot.clearMovement();
            p.inventory.currentItem = slotForFood;
            sneak = true;
            if (whatAreYouLookingAt() == null) {
                isRightClick = true;
            } else {
                if (p.isSneaking()) {
                    isRightClick = true;
                }
            }
            return true;
        }
        return false;
    }
    @Override
    protected void onCancel() {
    }
    @Override
    protected void onStart() {
    }
    @Override
    protected boolean onEnabled(boolean enabled) {
        return true;
    }
}
