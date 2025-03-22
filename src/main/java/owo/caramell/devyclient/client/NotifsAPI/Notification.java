package owo.caramell.devyclient.client.NotifsAPI;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import owo.caramell.devyclient.DevyClient;
import owo.caramell.devyclient.Utils.Animator.Animation;
import owo.caramell.devyclient.Utils.Animator.Easing;
import owo.caramell.devyclient.client.DevyMainClient;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    private String notifContent = "";
    public List<notifs> notifsList = new ArrayList<>();

    public void render(DrawContext context){
        for(notifs ntf : notifsList)
            ntf.render(context);
    }

    private int getPosType(){
        return DevyMainClient.instance.settings.notifPosType;
    }
    public void requestNotification(String title, Object content){
        notifsList.add(new notifs(title, String.valueOf(content)));
    }

    public class notifs{
        private String title;
        private String content;
        private boolean anim1 = false;
        Animation anim = new Animation(1000l, 2, 2, Easing.EASE_IN_OUT_CIRC);
        Animation anim2 = null;
        private int lastY = 0;
        notifs(String title, String content){
            this.title = title;
            this.content = content;
            lastY = 12 * notifsList.indexOf(this);
            anim2 = new Animation(300l, lastY, lastY, Easing.EASE_IN_OUT_CIRC);
        }
        public void render(DrawContext context){
            int y = 12 * notifsList.indexOf(this);
            if(lastY != y){
                anim2 = new Animation(300l, lastY, y, Easing.EASE_OUT_CIRC);
                lastY = y;
            }
            if(!anim1) startAnimation();
            context.drawText(MinecraftClient.getInstance().textRenderer, "(" + title + ") " + content, (int)anim.getValue(),2 + (int)anim2.getValue(), -1, true);
        }
        public void startAnimation(){
            anim1 = true;
            new Thread(() -> {
                anim = new Animation(1000l, -200, 2, Easing.EASE_IN_OUT_CIRC);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                anim = new Animation(500l, 2, -200, Easing.EASE_IN_OUT_CIRC);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                anim1 = false;
                notifsList.remove(this);
            }).start();
        }
    }
}
