package owo.caramell.devyclient.mixins;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owo.caramell.devyclient.client.DevyMainClient;

@Mixin(CottonClientScreen.class)
public class CottonScreenMixin {
    @Inject(method = "render", at = @At("RETURN"))
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        DevyMainClient.instance.notifAPI.render(context);
    }
}
