package owo.caramell.devyclient.client.NotifsAPI;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import owo.caramell.devyclient.Utils.Animator.Animation;
import owo.caramell.devyclient.Utils.Animator.Easing;
import owo.caramell.devyclient.client.DevyMainClient;

import java.util.*;

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

    // will be used later
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
        private final String title;
        private final String content;
        private boolean anim1 = false;
        private boolean isDimissed = false;
        Animation anim = new Animation(1000L, -2001, -2000, Easing.EASE_IN_OUT_CIRC);
        Animation anim2 = null;
        Animation progressBar = new Animation(30L, 243, 243, Easing.LINEAR);
        private float lastY = (MinecraftClient.getInstance().getWindow().getScaledHeight() - 32);
        public int heightExtra = 0;
        public int height = 0;
        notifs(String title, String content){
            this.title = title;
            this.content = content;
            // Get the last notifs height offset
        }
        public void render(DrawContext context){
            // TODO: Fix the notification having the wrong y values.
            int textLines = 0;
            TextRenderer textRndr = MinecraftClient.getInstance().textRenderer;
            List<OrderedText> list = textRndr.wrapLines(StringVisitable.plain(content), 230);
            for(OrderedText unused : list)
                textLines++;
            // Border and background square size
            int y1 = 42 * (notifsList.indexOf(this) + 1); // Height offset
            heightExtra = textRndr.fontHeight * (textLines == 0 ? 1 : textLines-1);
            // I genuinely want to kms, I'm gonna work on this later
            //int lastHeight = (getPreviousNotifObject(notifsList, this) != null ? getPreviousNotifObject(notifsList, this).height : 0);
            //float y = (MinecraftClient.getInstance().getWindow().getScaledHeight() - 32) - lastHeight - y1 + 28 - heightExtra;
            float y = (MinecraftClient.getInstance().getWindow().getScaledHeight() - 32) - y1 + 28 - heightExtra;
            height = 30 + heightExtra;
            int x = 8 + (int)anim.getValue();
            // Scaled Y because Minecraft goes fuckery mode doing 2D Y coordinates
            double yScaled = y * MinecraftClient.getInstance().getWindow().getScaleFactor();

            // Mouse hover checker
            //boolean isHover = (mouseX >= 10 && mouseX <= 110) && (mouseY >= 30 && mouseY <= 70);
            Mouse mouse = MinecraftClient.getInstance().mouse;
            boolean isHover = (mouse.getX() >= x && mouse.getX() <= x+(243*MinecraftClient.getInstance().getWindow().getScaleFactor())+2) &&
                    (mouse.getY() >= yScaled && mouse.getY() <= yScaled + (height * MinecraftClient.getInstance().getWindow().getScaleFactor()) + 12);

            // Start opening animation.
            if(lastY != y && anim1){
                anim2 = new Animation(300L, lastY, y, Easing.EASE_OUT_CIRC);
                lastY = y;
            }

            if(!anim1) {
                startAnimation();
                anim2 = new Animation(300L, y+0.1F, y, Easing.EASE_OUT_CIRC); // Setting y+0.1F to just y breaks the animation
                lastY = y;
            }
            // Render the notification square and text.
            context.fill( x - 3, (int)anim2.getValue() - 3, (x + 240), (int)anim2.getValue() + height + 6, 0xAA000000);
            // Render the progress bar
            if(progressBar.getValue() >= 10 && anim.isDone())
                context.drawHorizontalLine(x-2, ((int)progressBar.getValue() - 3),(((int)anim2.getValue() - 5) + height + 9),0xFF00FF00);
            // Render the notification border
            context.drawBorder( x - 3, (int)anim2.getValue() - 3, 243, height + 9, isHover ? 0xAAFF0000 : 0xAACCCCCC);
            context.drawText(textRndr, "[ " + title + " | " + heightExtra + " | " + y + " | " + y1 + " | " + notifsList.indexOf(this) + "]", x+3,2 + (int)anim2.getValue(), -1, true);
            int index = 1;
            for(OrderedText text : list){
                context.drawText(textRndr, text, x+3,(MinecraftClient.getInstance().textRenderer.fontHeight*index) + 6 + (int)anim2.getValue(), -1, true);
                index++;
            }
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
            anim = new Animation(500L, -500, 2, Easing.EASE_IN_OUT_CIRC);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            progressBar = new Animation(3000L, 243, 8, Easing.LINEAR);
        }
        private void animateOut(){
            anim = new Animation(500L, 2, -200, Easing.EASE_IN_OUT_CIRC);
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
        public static <T> T getPreviousNotifObject(List<T> list, T currentObject) {
            if (list == null || list.isEmpty()) {
                return null; // Handle empty or null list
            }

            int currentIndex = list.indexOf(currentObject);

            if (currentIndex == -1) {
                return null; // Current object not found in the list
            }

            int previousIndex = currentIndex - 1;

            if (previousIndex >= 0) {
                return list.get(previousIndex);
            } else {
                return null; // Current object is the first element, no previous element
            }
        }

    }
    public enum NotifType{
        Big,
        Medium,
        Small
    }
}
