package owo.aydendevy.devyclient.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import owo.aydendevy.devyclient.HUD.IRenderer;
import owo.aydendevy.devyclient.HUD.Loader.HUD;
import owo.aydendevy.devyclient.HUD.Loader.HUDManager;
import owo.aydendevy.devyclient.client.DevyMainClient;

public class ModToggleScreen extends Screen {
    HUDManager api;
    /**
     * Menu Deprecated!
     * Please use the SettingsScreen instead
     * TODO: Remove this from source.
     * **/
    protected ModToggleScreen(Screen lastScreen, HUDManager api) {
        super(Text.of("Toggle Mods Screen"));
        this.api = api;
        lScreen=lastScreen;
    }
    private Screen lScreen;
    @Override
    protected void init() {
        GridWidget gridWidget = new GridWidget();
        GridWidget.Adder adder = gridWidget.createAdder(1);
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        //adder.add(ButtonWidget.builder(Text.of("Placeholder"), button -> {}).width(204).build(), 2);
        for(IRenderer renderer : api.getRegisteredRenderers()){
            adder.add(CheckboxWidget.builder(Text.of("- " + renderer.getName()), this.textRenderer).checked(renderer.isEnabled()).callback(this::changeSettings).build());
        }
        adder.add(ButtonWidget.builder(Text.of("Back"), button -> this.client.setScreen(lScreen)).width(204).build(), 2, gridWidget.copyPositioner().marginTop(10));
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.15f, 0.15f);
        gridWidget.forEachChild(this::addDrawableChild);
        x = adder.getGridWidget().getX();
        y = adder.getGridWidget().getY();
        width1 = adder.getGridWidget().getWidth();
        height1 = adder.getGridWidget().getHeight();
    }
    int x,y,width1,height1;

    private void changeSettings(ClickableWidget checkbox, boolean checked) {
        for(IRenderer renderer : api.getRegisteredRenderers()){
            if(checkbox.getMessage().equals(Text.of("- " + renderer.getName()))){
                ((HUD)renderer).setEnabled(((CheckboxWidget) checkbox).isChecked());
            }
        }
    }

    @Override
    public void close() {
        this.client.setScreen(lScreen);
    }
}
