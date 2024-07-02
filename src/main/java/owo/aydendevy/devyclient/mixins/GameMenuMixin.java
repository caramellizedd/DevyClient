package owo.aydendevy.devyclient.mixins;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owo.aydendevy.devyclient.StaticStrings;
import owo.aydendevy.devyclient.screens.ClientSSelectionScreen;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin extends Screen {
    protected GameMenuMixin() {
        super(Text.of("UwU"));
    }
    @Inject(method = "init", at = @At("HEAD"))
    private void drawConfigButton(CallbackInfo ci){
        //test(); - Test ScaledResolution (Confirmed working!)
        //addDrawableChild(new TextWidget(10,10,this.textRenderer.getWidth("Running DevyClient 9.9-DEV"), this.textRenderer.fontHeight, Text.of(StaticStrings.version), this.textRenderer));
        // The sigma button - Don't remove this until you find a way to draw icons on buttons :3
        addDrawableChild(ButtonWidget.builder(Text.of("Σ"), button -> this.client.setScreen(new ClientSSelectionScreen(this))).dimensions(this.width / 2 - 125, this.height / 4 + 8, 20, 20).build());
    }
}
