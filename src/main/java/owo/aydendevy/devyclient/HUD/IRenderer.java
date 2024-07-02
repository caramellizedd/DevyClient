package owo.aydendevy.devyclient.HUD;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;

public interface IRenderer{
    int getWidth();
    int getHeight();
    String getName();
    void render(ScreenPosition pos, DrawContext drawContext);
    default void renderDummy(ScreenPosition pos, DrawContext drawContext) {
        render(pos, drawContext);
    }
    public void save(ScreenPosition pos);
    public ScreenPosition load();

    public default boolean isEnabled() {
        return true;
    }
}
