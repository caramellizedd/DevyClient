package owo.caramell.devyclient.client.NotifsAPI;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import owo.caramell.devyclient.DevyClient;
import owo.caramell.devyclient.Utils.Animator.Animation;
import owo.caramell.devyclient.Utils.Animator.Easing;
import owo.caramell.devyclient.client.DevyMainClient;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class Notification {
    public List<notifs> notifsList = new ArrayList<>();

    public void render(DrawContext context){
        try{
            for(notifs ntf : notifsList) {
                if(ntf == null) break;
                ntf.render(context);
            }
        }
        catch (ConcurrentModificationException ignored){
            DevyMainClient.logger.error("ConcurrentModificationException");
            DevyMainClient.logger.error("You may ignore this error if it didn't crash.");
        }
    }

    private int getPosType(){
        return DevyMainClient.instance.settings.notifPosType;
    }
    public void requestNotification(String title, Object content){
        try{
            notifsList.add(new notifs(title, String.valueOf(content)));
        }catch (ConcurrentModificationException cme){
            // try again lmao
            requestNotification(title, content);
        }
    }

    public class notifs{
        private String title;
        private String content;
        private boolean anim1 = false;
        private boolean isDimissed = false;
        Animation anim = new Animation(1000l, -2001, -2000, Easing.EASE_IN_OUT_CIRC);
        Animation anim2 = null;
        Animation progressBar = new Animation(30l, 243, 243, Easing.LINEAR);
        private int lastY = (MinecraftClient.getInstance().getWindow().getScaledHeight() - 32);
        notifs(String title, String content){
            this.title = title;
            this.content = content;
        }
        public void render(DrawContext context){
            int y1 = 42 * (notifsList.indexOf(this) + 1);
            int y = (MinecraftClient.getInstance().getWindow().getScaledHeight() - 32) - y1 + 28;
            int height = 30;
            int x = 8 + (int)anim.getValue();
            if(lastY != y && anim1){
                anim2 = new Animation(300l, lastY, y, Easing.EASE_OUT_CIRC);
                lastY = y;
            }
            if(!anim1) {
                startAnimation();
                anim2 = new Animation(300l, y+0.1F, y, Easing.EASE_OUT_CIRC);
                lastY = y;
            }
            context.fill( x - 3, (int)anim2.getValue() - 3, (x + 240), (int)anim2.getValue() + height + 6, 0xAA000000);
            context.drawBorder( x - 3, (int)anim2.getValue() - 3, 243, height + 9, 0xAACCCCCC);
            context.drawHorizontalLine(x-3, ((int)progressBar.getValue() - 3),(((int)anim2.getValue() - 5) + height + 9),0xFF00FF00);
            context.drawText(MinecraftClient.getInstance().textRenderer, "[ " + title + " ]", x+3,2 + (int)anim2.getValue(), -1, true);
            context.drawText(MinecraftClient.getInstance().textRenderer, content, x-1,MinecraftClient.getInstance().textRenderer.fontHeight + 6 + (int)anim2.getValue(), -1, true);
        }
        public void startAnimation(){
            anim1 = true;
            new Thread(() -> {
                animateIn();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(isDimissed) return;
                animateOut();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                anim1 = false;
                tryRemoveSelf();
            }).start();
        }
        private void animateIn(){
            progressBar = new Animation(3000l, 243, 8, Easing.LINEAR);
            anim = new Animation(500l, -500, 2, Easing.EASE_IN_OUT_CIRC);
        }
        private void animateOut(){
            anim = new Animation(500l, 2, -200, Easing.EASE_IN_OUT_CIRC);
        }
        private void tryRemoveSelf(){
            try{
                notifsList.remove(this);
            }catch (ConcurrentModificationException cme){
                try {
                    Thread.sleep(new Random().nextInt(20));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                tryRemoveSelf();
            }
        }
    }
}
