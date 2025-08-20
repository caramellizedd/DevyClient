package owo.caramell.devyclient.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owo.caramell.devyclient.StaticStrings;
import owo.caramell.devyclient.client.DevyMainClient;
import owo.caramell.devyclient.screens.ClientSSelectionScreen;

import java.util.Random;

@Mixin(TitleScreen.class)
public class MainMenuMixin extends Screen {
    protected MainMenuMixin() {
        super(Text.of("OwO"));
    }
    @Shadow private SplashTextRenderer splashText;
    boolean splashLoaded = true;
    @Inject(method = "init", at = @At("HEAD"))
    private void TitleWatermark(CallbackInfo ci){
        if(DevyMainClient.instance.settings.modifySplashText){
            splashText = new SplashTextRenderer("Welcome " + this.client.getSession().getUsername());
            splashLoaded = false;
        }
        else if(!DevyMainClient.instance.settings.modifySplashText && !splashLoaded){
            splashLoaded = true;
            splashText = this.client.getSplashTextLoader().get();
        }

        addDrawableChild(new TextWidget(10,10,this.textRenderer.getWidth("Running DevyClient 9.9-DEV"), this.textRenderer.fontHeight, Text.of(StaticStrings.version), this.textRenderer));
        if(StaticStrings.version.contains("DEV"))
            addDrawableChild(new TextWidget(10,20,this.textRenderer.getWidth("This version is UNSTABLE. Please report any crashes or bugs in github issues."), this.textRenderer.fontHeight, Text.of("This version is UNSTABLE. Please report any crashes or bugs in github issues."), this.textRenderer));
        // The sigma button - Don't remove this until you find a way to draw icons on buttons :3
        addDrawableChild(ButtonWidget.builder(Text.of("Σ"), button -> this.client.setScreen(new ClientSSelectionScreen(this))).dimensions(this.width / 2 - 124, this.height / 4 + 48, 20, 20).build());
        //DevyMainClient.instance.notifAPI.showNotification(Text.literal("Lorem ipsum sit dolor amet. uwaaa"));

    }
    @Inject(method = "render", at = @At("RETURN"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        owo.caramell.devyclient.screens.TitleScreen.render(context, mouseX, mouseY, delta);
    }
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
        owo.caramell.devyclient.screens.TitleScreen.mouseClicked(mouseX, mouseY, button);
    }
}
