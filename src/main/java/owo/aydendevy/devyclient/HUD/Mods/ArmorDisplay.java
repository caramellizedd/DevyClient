package owo.aydendevy.devyclient.HUD.Mods;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import owo.aydendevy.devyclient.HUD.Loader.HUDDraggable;
import owo.aydendevy.devyclient.HUD.ScreenPosition;

public class ArmorDisplay extends HUDDraggable {
    @Override
    public int getWidth() {
        return 64;
    }

    @Override
    public int getHeight() {
        return 84;
    }

    @Override
    public String getName() {
        return "Armor Display HUD";
    }

    @Override
    public void render(ScreenPosition pos, DrawContext drawContext) {
        if(mc.world != null && mc.player != null){
            if(mc.player.getMainHandStack() == ItemStack.EMPTY || !mc.player.getMainHandStack().isDamageable()){
                int slot = 0;
                for(ItemStack is : mc.player.getArmorItems()){
                    if(is == ItemStack.EMPTY) continue;
                    renderItemStack(pos, is, slot, drawContext);
                    slot++;
                }
                return;
            }
            renderItemStack(pos, mc.player.getMainHandStack(), 0, drawContext);
            int slot = 1;
            for(ItemStack is : mc.player.getArmorItems()){
                if(is == ItemStack.EMPTY) continue;
                renderItemStack(pos, is, slot, drawContext);
                slot++;
            }
        }
    }

    @Override
    public void renderDummy(ScreenPosition pos, DrawContext drawContext) {
        if(mc.world != null && mc.player != null){
            if(mc.player.getMainHandStack() == ItemStack.EMPTY || !mc.player.getMainHandStack().isDamageable()){
                int slot = 0;
                renderItemStack(pos, Items.IRON_AXE, 0, drawContext);
                slot++;
                for(ItemStack is : mc.player.getArmorItems()){
                    if(is == ItemStack.EMPTY) {
                        switch(slot){
                            case 1:
                                renderItemStack(pos, Items.DIAMOND_BOOTS, slot, drawContext);
                                slot++;
                                break;
                            case 2:
                                renderItemStack(pos, Items.DIAMOND_LEGGINGS, slot, drawContext);
                                slot++;
                                break;
                            case 3:
                                renderItemStack(pos, Items.DIAMOND_CHESTPLATE, slot, drawContext);
                                slot++;
                                break;
                            case 4:
                                renderItemStack(pos, Items.DIAMOND_HELMET, slot, drawContext);
                                slot++;
                                break;
                        }
                        continue;
                    }
                    renderItemStack(pos, is, slot, drawContext);
                    slot++;
                }
                return;
            }else{
                int slot = 0;
                renderItemStack(pos, mc.player.getMainHandStack(), 0, drawContext);
                slot++;
                for(ItemStack is : mc.player.getArmorItems()){
                    if(is == ItemStack.EMPTY) {
                        switch(slot){
                            case 1:
                                renderItemStack(pos, Items.DIAMOND_BOOTS, slot, drawContext);
                                slot++;
                                break;
                            case 2:
                                renderItemStack(pos, Items.DIAMOND_LEGGINGS, slot, drawContext);
                                slot++;
                                break;
                            case 3:
                                renderItemStack(pos, Items.DIAMOND_CHESTPLATE, slot, drawContext);
                                slot++;
                                break;
                            case 4:
                                renderItemStack(pos, Items.DIAMOND_HELMET, slot, drawContext);
                                slot++;
                                break;
                        }
                        continue;
                    }
                    renderItemStack(pos, is, slot, drawContext);
                    slot++;
                }
                return;
            }
        }
        renderItemStack(pos, Items.DIAMOND_AXE, 0, drawContext);
        renderItemStack(pos, Items.DIAMOND_BOOTS, 1, drawContext);
        renderItemStack(pos, Items.DIAMOND_LEGGINGS, 2, drawContext);
        renderItemStack(pos, Items.DIAMOND_CHESTPLATE, 3, drawContext);
        renderItemStack(pos, Items.DIAMOND_HELMET, 4, drawContext);
    }

    private void renderItemStack(ScreenPosition pos, Item item, int slot, DrawContext drawContext){
        ItemStack is = new ItemStack(item);
        int yAdd = (-16 * slot) + 66;
        if(is.isDamageable()) {
            double damage = ((is.getMaxDamage() - is.getDamage()) / (double) is.getMaxDamage()) * 100;
            // TODO: Change between percentage or pure numbers
            drawContext.drawText(mc.textRenderer, String.format("%.2f%%", damage),pos.getAbsoluteX() + 20, pos.getAbsoluteY() + yAdd + 5, -1, true);
            //drawContext.drawText(mc.textRenderer, is.getMaxDamage() + "/" + is.getDamage(),pos.getAbsoluteX() + 20, pos.getAbsoluteY() + yAdd + 5, -1, true);
        }
        drawContext.drawItem(is, pos.getAbsoluteX(), pos.getAbsoluteY() + yAdd);
    }
    private void renderItemStack(ScreenPosition pos, ItemStack is, int slot, DrawContext drawContext){
        int yAdd = (-16 * slot) + 66;
        if(is.isDamageable()) {
            double damage = ((is.getMaxDamage() - is.getDamage()) / (double) is.getMaxDamage()) * 100;
            // TODO: Change between percentage or pure numbers
            drawContext.drawText(mc.textRenderer, String.format("%.2f%%", damage),pos.getAbsoluteX() + 20, pos.getAbsoluteY() + yAdd + 5, -1, true);
            //drawContext.drawText(mc.textRenderer, is.getMaxDamage() + "/" + is.getDamage(),pos.getAbsoluteX() + 20, pos.getAbsoluteY() + yAdd + 5, -1, true);
        }
        drawContext.drawItem(is, pos.getAbsoluteX(), pos.getAbsoluteY() + yAdd);
    }
}
