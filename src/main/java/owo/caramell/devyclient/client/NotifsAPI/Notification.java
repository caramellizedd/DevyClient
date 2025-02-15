package owo.caramell.devyclient.client.NotifsAPI;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import owo.caramell.devyclient.Utils.Animator.Animation;
import owo.caramell.devyclient.Utils.Animator.Easing;

public class Notification {
    Animation anim = null;
    private String notifContent = "";
    private boolean showingNotif = false;
    public int notifQueue = 0;

    public void showNotification(Text content){
        new Thread(() -> {
            notifQueue++;
            while(showingNotif){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            startAnimation();
            notifContent = content.getLiteralString();
            showingNotif = true;
        }).start();
    }

    public void render(DrawContext context){
        if(anim == null) return;
        context.drawText(MinecraftClient.getInstance().textRenderer, "(" + notifQueue + ") " + notifContent, (int)anim.getValue(),2, -1, true);
    }
    public void startAnimation(){
        new Thread(() -> {
            anim = new Animation(1000l, -200, 2, Easing.EASE_IN_OUT_CIRC);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            anim = new Animation(1000l, 2, -200, Easing.EASE_IN_OUT_CIRC);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            notifQueue--;
            showingNotif = false;
        }).start();
    }
}
