package owo.caramell.devyclient.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owo.caramell.devyclient.StaticStrings;
import owo.caramell.devyclient.alteration.Loader.AlterationManager;
import owo.caramell.devyclient.client.DevyMainClient;

@Mixin(InGameHud.class)
public class IngameMixin {

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        // Changes: Draw watermark and call the Render event.
        if(DevyMainClient.instance.settings.showVersionIngame)
            context.drawText(MinecraftClient.getInstance().textRenderer, StaticStrings.version, 2, 2, -1, true);
        DevyMainClient.instance.hudManager.onRender(context);
    }

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void renderHBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci){
        // Changes: Redirect the Render Health Bar Function to the modified one.
        if(DevyMainClient.instance.settings.alterStatusBar){
            AlterationManager.statusBarUIAlteration.drawHealthBars(context, player, x, y, regeneratingHeartIndex, maxHealth, lastHealth, health, absorption, blinking);
            ci.cancel();
        }
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void renderABar(DrawContext context, PlayerEntity player, int i, int j, int k, int x, CallbackInfo ci){
        if(DevyMainClient.instance.settings.alterStatusBar){
            AlterationManager.statusBarUIAlteration.drawArmorBars(context, player, i, j, k, x);
            ci.cancel();
        }
    }

    @ModifyVariable(method = "renderStatusBars", at = @At("STORE"), ordinal = 7)
    private int q(int realValue){
        // Changes: Force the game to not move ANY bars up a line when more than 20 hearts are present
        if(DevyMainClient.instance.settings.alterStatusBar){
            return 0;
        }
        return realValue;
    }
}
