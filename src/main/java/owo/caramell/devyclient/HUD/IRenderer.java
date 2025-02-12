package owo.caramell.devyclient.HUD;

import net.minecraft.client.gui.DrawContext;

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
