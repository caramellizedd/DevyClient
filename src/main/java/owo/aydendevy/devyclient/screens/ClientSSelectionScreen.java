package owo.aydendevy.devyclient.screens;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.*;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import owo.aydendevy.devyclient.Utils.GuiUtils;
import owo.aydendevy.devyclient.client.DevyMainClient;

import java.io.IOException;
import java.util.function.Supplier;

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
    protected void applyBlur(float delta) {
        GlStateManager._enableScissorTest();
        if(client.getWindow().getScaleFactor() == 2){
            GlStateManager._scissorBox(20,20, width*2-40, height*2-41);
        }else if(client.getWindow().getScaleFactor() == 1){
            GlStateManager._scissorBox(10,10, width-20, height-21);
        }
        this.client.gameRenderer.renderBlur(delta);
        this.client.getFramebuffer().beginWrite(false);
        GlStateManager._disableScissorTest();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client.world == null) {
            this.renderPanoramaBackground(context, delta);
        }
        this.applyBlur(delta);
        //context.drawText(client.textRenderer, "GUI Scale: " + client.getWindow().getScaleFactor(), 0, 0, -1, true);
        //this.renderDarkening(context);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        GuiUtils.drawRoundBG(context.getMatrices(), this);
        context.drawBorder(10,10,width-20,height-20,0x55FFFFFF);
        context.fill(10,10,width-10,height-10, 0x77000000);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
