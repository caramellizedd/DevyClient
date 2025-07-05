package owo.caramell.devyclient.alteration;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import owo.caramell.devyclient.Configs.StatusBarColors;
import owo.caramell.devyclient.client.DevyMainClient;
import owo.caramell.devyclient.client.enums.PublicEnum;

public class IngameStatusBarUI {
    private final Random random = Random.create();
    private int yHPShake = this.random.nextInt(2);

    /**
     * drawHealthbars Function
     * @Credits: Mojang
     * @ModifiedBy: CRML Studios.
     * @Reason: Health UI Modifications.
     * @param context The main game DrawContext
     * @param player The Player Entity
     * @param x
     * @param y
     * @param regeneratingHeartIndex ?
     * @param maxHealth Max amount of Health
     * @param lastHealth The last amount of health
     * @param health The current amount of health
     * @param absorption The amount of absorption
     * @param blinking Is the health blinking?
     */
    // Health Bar Alteration
    public void drawHealthBars(DrawContext context, PlayerEntity player, int x, int y, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking){
        PublicEnum.HeartType heartType = PublicEnum.HeartType.fromPlayerState(player);
        boolean bl = player.getWorld().getLevelProperties().isHardcore();
        int i = MathHelper.ceil((double)maxHealth / 2.0);
        int j = MathHelper.ceil((double)absorption / 2.0);
        int k = i * 2;
        for (int l = i + j - 1; l >= 0; --l) {
            boolean bl4;
            int r;
            boolean bl2;
            int o = x + l * 2;
            int p = y;
            if (lastHealth + absorption <= 4) {
                p += yHPShake;
            }
            if (l < i && l == regeneratingHeartIndex) {
                p -= 2;
            }
            this.drawHeart2(context, PublicEnum.HeartType.CONTAINER, o, p, bl, blinking, false);
            int q = l * 2;
            boolean bl3 = bl2 = l >= i;
            if (bl2 && (r = q - k) < absorption) {
                boolean bl32 = r + 1 == absorption;
                this.drawHeart2(context, heartType == PublicEnum.HeartType.WITHERED ? heartType : PublicEnum.HeartType.ABSORBING, o, p, bl, false, bl32);
            }
            if (blinking && q < health) {
                bl4 = q + 1 == health;
                this.drawHeart2(context, heartType, o, p, bl, true, bl4);
            }
            if (q >= lastHealth) continue;
            bl4 = q + 1 == lastHealth;
            this.drawHeart2(context, heartType, o, p, bl, false, bl4);
        }
        if(blinking){
            this.drawHeartBlink(context, x, y, (int) player.getMaxHealth() + (int) player.getMaxAbsorption());
        }
    }
    private void drawHeart2(DrawContext context, PublicEnum.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half) {
        RenderSystem.enableBlend();
        if(blinking){
            context.fill(x,y,x+2,y+9, 0xFFFF01E6);
            return;
        }
        if(half){
            switch (type){
                case NORMAL -> context.fill(x,y,x+1,y+9, StatusBarColors.instance.HPColorNormal);
                case WITHERED -> context.fill(x,y,x+1,y+9, StatusBarColors.instance.HPColorWithered);
                case FROZEN -> context.fill(x,y,x+1,y+9, StatusBarColors.instance.HPColorFrozen);
                case POISONED -> context.fill(x,y,x+1,y+9, StatusBarColors.instance.HPColorPoisoned);
                case ABSORBING -> context.fill(x,y, x+1, y+9, StatusBarColors.instance.HPColorAbsorbing);
                default -> context.fill(x,y,x+1,y+9, StatusBarColors.instance.HPColorContainer);
            }
        }else{
            switch (type){
                case NORMAL -> context.fill(x,y,x+2,y+9, StatusBarColors.instance.HPColorNormal);
                case WITHERED -> context.fill(x,y,x+2,y+9, StatusBarColors.instance.HPColorWithered);
                case FROZEN -> context.fill(x,y,x+2,y+9, StatusBarColors.instance.HPColorFrozen);
                case POISONED -> context.fill(x,y,x+2,y+9, StatusBarColors.instance.HPColorPoisoned);
                case ABSORBING -> context.fill(x,y, x+2, y+9, StatusBarColors.instance.HPColorAbsorbing);
                default -> context.fill(x,y,x+2,y+9, StatusBarColors.instance.HPColorContainer);
            }
        }

        RenderSystem.disableBlend();
    }
    private void drawHeartBlink(DrawContext context, int x, int y, int width){
        context.drawBorder(x-1,y-1, width+2, 11, 0xFFFFFFFF);
    }

    public void drawArmorBars(DrawContext context, PlayerEntity player, int i, int j, int k, int x) {
        int l = player.getArmor();
        if (l > 0) {
            int m = i - (j - 1) * k - 10;

            for (int n = 0; n < 10; n++) {
                int o = x + n * 2;
                context.fill(o,m,o+2,m+9, StatusBarColors.instance.ArmorColorContainer);
                if (n * 2 + 1 < l) {
                    //context.drawGuiTexture(RenderLayer::getGuiTextured, ARMOR_FULL_TEXTURE, o, m, 9, 9);
                    context.fill(o,m,o+2,m+9, StatusBarColors.instance.ArmorColor);
                }

                if (n * 2 + 1 == l) {
                    //context.drawGuiTexture(RenderLayer::getGuiTextured, ARMOR_HALF_TEXTURE, o, m, 9, 9);
                    context.fill(o,m,o+1,m+9, StatusBarColors.instance.ArmorColor);
                }

                if (n * 2 + 1 > l) {
                    //context.drawGuiTexture(RenderLayer::getGuiTextured, ARMOR_EMPTY_TEXTURE, o, m, 9, 9);

                }
            }
        }
    }
}
