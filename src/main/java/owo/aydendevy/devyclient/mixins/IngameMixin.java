package owo.aydendevy.devyclient.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owo.aydendevy.devyclient.StaticStrings;
import owo.aydendevy.devyclient.client.DevyMainClient;

@Mixin(InGameHud.class)
public class IngameMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci){
        context.drawText(MinecraftClient.getInstance().textRenderer, StaticStrings.version, 2, 2, -1, true);
        DevyMainClient.instance.hudManager.onRender(context);
    }
}
