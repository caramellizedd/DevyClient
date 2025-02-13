package owo.caramell.devyclient.client.NotifsAPI;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import owo.caramell.devyclient.Utils.Animator.Animation;
import owo.caramell.devyclient.Utils.Animator.Easing;

public class Notification {
    Animation anim = null;
    public void showNotification(Text content){
        anim = new Animation(1000l, -200, 2, Easing.EASE_IN_OUT_CIRC);
        notifContent = content;
    }
    private Text notifContent = Text.literal("");
    public void render(DrawContext context){
        if(anim == null) return;
        context.drawText(MinecraftClient.getInstance().textRenderer, notifContent, (int)anim.getValue(),2, -1, true);
    }
}
