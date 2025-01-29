package owo.caramell.devyclient.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owo.caramell.devyclient.StaticStrings;
import owo.caramell.devyclient.client.DevyMainClient;
import owo.caramell.devyclient.client.enums.PublicEnum;

@Mixin(InGameHud.class)
public class IngameMixin {

    private final Random random = Random.create();

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        context.drawText(MinecraftClient.getInstance().textRenderer, StaticStrings.version, 2, 2, -1, true);
        DevyMainClient.instance.hudManager.onRender(context);
    }
    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void renderHBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci){
        if(DevyMainClient.instance.settings.hpLine){
            PublicEnum.HeartType heartType = PublicEnum.HeartType.fromPlayerState(player);
            boolean bl = player.getWorld().getLevelProperties().isHardcore();
            int i = MathHelper.ceil((double)maxHealth / 2.0);
            int j = MathHelper.ceil((double)absorption / 2.0);
            int k = i * 2;
            for (int l = i + j - 1; l >= 0; --l) {
                boolean bl4;
                int r;
                boolean bl2;
                int n = l % 50;
                int o = x + n * 2;
                int p = y;
                if (lastHealth + absorption <= 4) {
                    p += this.random.nextInt(2);
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
                this.drawHeartBlink(context, x, y);
            }
            ci.cancel();
        }
    }
    @Unique
    private void drawHeart2(DrawContext context, PublicEnum.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half) {
        RenderSystem.enableBlend();
        if(blinking){
            context.fill(x,y,x+2,y+9, 0xFFFF01E6);
            return;
        }
        if(half){
            switch (type){
                case NORMAL -> context.fill(x,y,x+1,y+9, 0xFFFFFF00);
                case WITHERED -> context.fill(x,y,x+1,y+9, 0xFF000000);
                case FROZEN -> context.fill(x,y,x+1,y+9, 0xFFB2EDFF);
                case POISONED -> context.fill(x,y,x+1,y+9, 0xFF45FF01);
                case ABSORBING -> context.fill(x,y, x+1, y+9, 0xFFFFB700);
                default -> context.fill(x,y,x+1,y+9, 0xFFFF0000);
            }
        }else{
            switch (type){
                case NORMAL -> context.fill(x,y,x+2,y+9, 0xFFFFFF00);
                case WITHERED -> context.fill(x,y,x+2,y+9, 0xFF000000);
                case FROZEN -> context.fill(x,y,x+2,y+9, 0xFFB2EDFF);
                case POISONED -> context.fill(x,y,x+2,y+9, 0xFF45FF01);
                case ABSORBING -> context.fill(x,y, x+2, y+9, 0xFFFFB700);
                default -> context.fill(x,y,x+2,y+9, 0xFFFF0000);
            }
        }

        RenderSystem.disableBlend();
    }
    @Unique
    private void drawHeartBlink(DrawContext context, int x, int y){
        context.drawBorder(x-1,y-1, 22, 11, 0xFFFFFFFF);
    }
}
