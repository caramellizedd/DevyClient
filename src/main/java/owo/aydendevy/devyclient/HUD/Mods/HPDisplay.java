package owo.aydendevy.devyclient.HUD.Mods;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import owo.aydendevy.devyclient.HUD.Loader.HUDDraggable;
import owo.aydendevy.devyclient.HUD.ScreenPosition;
import owo.aydendevy.devyclient.client.DevyMainClient;

public class HPDisplay extends HUDDraggable {
    @Override
    public String getName() {
        return "HP Display";
    }

    @Override
    public int getWidth() {
        if(MinecraftClient.getInstance().player !=null){
            return mc.textRenderer.getWidth("HP: " + (int)MinecraftClient.getInstance().player.getHealth() +  "/" + (int)MinecraftClient.getInstance().player.getMaxHealth());
        }
        return mc.textRenderer.getWidth("HP: 00/00");
    }

    @Override
    public int getHeight() {
        return mc.textRenderer.fontHeight;
    }

    @Override
    public void render(ScreenPosition pos, DrawContext drawContext) {
        // Args: OrderedText text, float x, float y, int color, int outlineColor, Matrix4f matrix, VertexConsumerProvider vertexConsumers, int light
        //mc.textRenderer.drawWithOutline(Text.of("DEEZ").asOrderedText(), pos.getAbsoluteX(), pos.getAbsoluteY(), -1, -1, null, null, 1);
        if(MinecraftClient.getInstance().player !=null){
            ClientPlayerEntity victim = MinecraftClient.getInstance().player;
            if(DevyMainClient.instance.settings.hpColored){
                if(victim.getHealth() >= 12) {
                    drawContext.drawText(mc.textRenderer, Text.of("§aHP: " + (int)victim.getHealth() + "/" + (int)victim.getMaxHealth()), pos.getAbsoluteX(), pos.getAbsoluteY(), -1, true);
                } else if(victim.getHealth() >= 6)
                    drawContext.drawText(mc.textRenderer, Text.of("§eHP: " + (int)victim.getHealth() + "/" + (int)victim.getMaxHealth()), pos.getAbsoluteX(), pos.getAbsoluteY(), -1, true);
                else
                    drawContext.drawText(mc.textRenderer, Text.of("§cHP: " + (int)victim.getHealth() + "/" + (int)victim.getMaxHealth()), pos.getAbsoluteX(), pos.getAbsoluteY(), -1, true);
            }else{
                drawContext.drawText(mc.textRenderer, Text.of("HP: " + (int)victim.getHealth() + "/" + (int)victim.getMaxHealth()), pos.getAbsoluteX(), pos.getAbsoluteY(), -1, true);
            }
        }else{
            drawContext.drawText(this.mc.textRenderer, Text.of("§eHP: 20/20"), pos.getAbsoluteX(),pos.getAbsoluteY(), -1 , true);
        }
    }

    @Override
    public void renderDummy(ScreenPosition pos, DrawContext drawContext) {
        super.renderDummy(pos, drawContext);
    }
}
