package owo.caramell.devyclient.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.session.Session;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owo.caramell.devyclient.client.DevyMainClient;

public class TitleScreen {
    /**
     * TitleScreen - Helper Class for modifying the Title Screen.
     * Open the main title screen class in the Mixins Package.
     */
    public static void render(DrawContext context, int mouseX, int mouseY, float delta){
        renderAccountOverlay(context, mouseX, mouseY, delta);
    }
    public static void renderAccountOverlay(DrawContext context, int mouseX, int mouseY, float delta){
        TextRenderer textrndr = MinecraftClient.getInstance().textRenderer;
        Session currentSession = MinecraftClient.getInstance().getSession();
        DevyMainClient client = DevyMainClient.instance;

        int x = 10;
        int y = 30;

        boolean isHover = (mouseX >= 10 && mouseX <= 110) && (mouseY >= 30 && mouseY <= 70);

        if(isHover){
            context.fill(x,y,110,70,0xBB000000);
            context.drawBorder(x,y, 100, 40, 0xFFFF0000);
        }
        else{
            context.fill(x,y,110,70,0x99000000);
            context.drawBorder(x,y, 100, 40, -1);
        }
        context.drawTextWithShadow(textrndr, Text.translatable("owo.caramell.loggedinas"), x+5, y+5, -1);
        context.drawTextWithShadow(textrndr, currentSession.getUsername(), x+5, y+15, -1);
        context.drawTextWithShadow(textrndr, client.isDALoggedIn ? Text.translatable("owo.caramell.acc") : Text.translatable("owo.caramell.noacc"), x+5, y+28, -1);
    }
    private static void onAccOverlayPressed(){
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        MinecraftClient.getInstance().setScreen(new AccountScreen());
    }
    public static void mouseClicked(double mouseX, double mouseY, int button){
        boolean isHover = (mouseX >= 10 && mouseX <= 110) && (mouseY >= 30 && mouseY <= 70);
        if(isHover)
            onAccOverlayPressed();
    }
}
