package owo.caramell.devyclient.screens;

import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import owo.caramell.devyclient.Utils.GuiUtils;
import owo.caramell.devyclient.client.DevyMainClient;
import owo.caramell.devyclient.screens.debug.NotifTestScreen;

// SSelection - S(Settings)Selection
// I am so good at making names - Charamellized
public class ClientSSelectionScreen extends Screen {
    private Screen parent;
    public ClientSSelectionScreen(Screen lastScreen) {
        super(Text.of("Main Client Settings Screen"));
        parent = lastScreen;
    }

    @Override
    protected void init() {
        GridWidget gridWidget = new GridWidget();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        adder.add(ButtonWidget.builder(Text.of("Change HUD Positions"), button -> {
            this.client.setScreen(new HUDConfigScreen(DevyMainClient.instance.hudManager, this));
        }).width(212).build(), 2);
        adder.add(ButtonWidget.builder(Text.of("Client Settings"), button -> {
            this.client.setScreen(new SettingsScreen(this, DevyMainClient.instance.hudManager));
        }).width(212).build(), 2);
        adder.add(ButtonWidget.builder(Text.of("About this Client"), button -> {

        }).width(102).build());
        adder.add(ButtonWidget.builder(Text.of("Credits"), button -> {

        }).width(102).build());
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.4f);
        gridWidget.forEachChild(this::addDrawableChild);
        //this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.options"), button -> this.client.setScreen(new OptionsScreen(this, this.client.options))).dimensions(this.width / 2 - 100, 72 + 12, 98, 20).build());
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client.world == null) {
            this.renderPanoramaBackground(context, delta);
        }
        this.applyBlur(context);
        //context.drawText(client.textRenderer, "GUI Scale: " + client.getWindow().getScaleFactor(), 0, 0, -1, true);
        //this.renderDarkening(context);
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        /**
         * Debug Keybinds
         * You may remove them or keep them as an Easter Egg
         *
         * NOTE: If you're keeping them as Easter Egg, please mark them. Thank you :3
         */
        if(modifiers == 1) { // Easter Egg
            switch (keyCode){
                case 68: // D
                    MinecraftClient.getInstance().setScreen(new MusicPlayer());
                    break;
                case 70: // F
                    MinecraftClient.getInstance().setScreen(new NotifTestScreen());
                    break;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        GuiUtils.drawRoundBG(context.getMatrices(), this);
        context.drawBorder(10,10,width-20,height-20,0x55FFFFFF);
        context.fill(10,10,width-10,height-10, 0x77000000);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
