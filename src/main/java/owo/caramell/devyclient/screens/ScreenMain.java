package owo.caramell.devyclient.screens;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import owo.caramell.devyclient.client.DevyMainClient;

public class ScreenMain extends CottonClientScreen {
    Screen prevScreen;

    public ScreenMain(GuiDescription desc, Screen lastScreen) {
        super(desc);
        prevScreen = lastScreen;
    }

    @Override
    public void close() {
        DevyMainClient.instance.settings.saveAll();
        client.setScreen(prevScreen);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
