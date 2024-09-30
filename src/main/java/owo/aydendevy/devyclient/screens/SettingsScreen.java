package owo.aydendevy.devyclient.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.data.Main;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import owo.aydendevy.devyclient.HUD.IRenderer;
import owo.aydendevy.devyclient.HUD.Loader.HUD;
import owo.aydendevy.devyclient.HUD.Loader.HUDManager;
import owo.aydendevy.devyclient.client.DevyMainClient;

import java.util.Optional;

import static net.minecraft.client.gui.screen.world.CreateWorldScreen.TAB_HEADER_BACKGROUND_TEXTURE;

public class SettingsScreen extends Screen {
    protected SettingsScreen(Screen prevScreen, HUDManager api) {
        super(Text.of("Client Advanced Settings"));
        lastScreen = prevScreen;
        this.api = api;
    }
    private Screen lastScreen;
    private TabNavigationWidget tabNav;
    private final TabManager tabManager = new TabManager(this::addDrawableChild, child -> this.remove((Element)child));
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    @Override
    protected void init() {
        // TODO: For version backwards compatibility, check if user is using a version that has Minecraft's Vanilla Tab Navigation.
        this.tabNav = TabNavigationWidget.builder(this.tabManager, this.width).tabs(new MainSettings(), new HUDSettings(), new AdvancedSettings()).build();
        this.addDrawableChild(this.tabNav);
        DirectionalLayoutWidget directionalLayoutWidget = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        directionalLayoutWidget.add(ButtonWidget.builder(Text.of("Back"), button -> close()).build());
        this.layout.forEachChild(child -> {
            child.setNavigationOrder(1);
            this.addDrawableChild(child);
        });
        this.tabNav.selectTab(0, false);
        this.initTabNavigation();
    }

    @Override
    protected void initTabNavigation() {
        if(tabNav == null) {
            DevyMainClient.logger.info("TAB NAVIGATION NOT FOUND");
            return;
        }
        this.tabNav.setWidth(this.width);
        this.tabNav.init();
        int bottom = tabNav.getNavigationFocus().getBottom();
        ScreenRect scrRect = new ScreenRect(0, bottom, this.width, this.height - this.layout.getFooterHeight() - bottom);
        this.tabManager.setTabArea(scrRect);
        this.layout.setHeaderHeight(bottom);
        this.layout.refreshPositions();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        RenderSystem.enableBlend();
        context.drawTexture(Screen.FOOTER_SEPARATOR_TEXTURE, 0, this.height - this.layout.getFooterHeight() - 2, 0.0f, 0.0f, this.width, 2, 32, 2);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        /**
         * Debug Keybinds
         * You may remove them or keep them as an Easter Egg
         *
         * NOTE: If you're keeping them as Easter Egg, please mark them. Thank you :3
         */
        if(modifiers == 1 && keyCode == 68){ // Easter Egg
            DevyMainClient.instance.alwaysShowGUIName = DevyMainClient.instance.alwaysShowGUIName ? false : true;
            MinecraftClient.getInstance().updateWindowTitle();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderDarkening(DrawContext context) {
        context.drawTexture(TAB_HEADER_BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, this.width, this.layout.getHeaderHeight(), 16, 16);
        this.renderDarkening(context, 0, this.layout.getHeaderHeight(), this.width, this.height - this.layout.getFooterHeight() - tabNav.getNavigationFocus().getBottom());
        for(int i = 0; i < 2; i++)
            this.renderDarkening(context, 0, this.layout.getHeight()-this.layout.getFooterHeight(), this.width, this.layout.getFooterHeight());
    }

    @Override
    public void close() {
        DevyMainClient.instance.settings.saveAll();
        this.client.setScreen(lastScreen);
    }
    HUDManager api;
    class MainSettings extends GridScreenTab {
        MainSettings(){
            super(Text.of("Main Settings"));
            GridWidget.Adder adder = this.grid.createAdder(1);
            adder.add(new TextWidget(Text.of("§7===§rHealth UI§7==="), textRenderer), grid.copyPositioner().marginBottom(10));
            adder.add(CheckboxWidget.builder(Text.of("Replace Vanilla Health UI with a Simpler one"), textRenderer).callback((checkbox, checked) -> {
                DevyMainClient.instance.settings.hpLine = checked;
            }).checked(DevyMainClient.instance.settings.hpLine).build());
            adder.add(new TextWidget(Text.of("Warning: Depends on HP Display to be enabled!"), textRenderer), grid.copyPositioner().marginTop(3).marginBottom(3).marginLeft(1));
            adder.add(CheckboxWidget.builder(Text.of("Colored HP Text"), textRenderer).callback((checkbox, checked) -> {
                DevyMainClient.instance.settings.hpColored = checked;
            }).checked(DevyMainClient.instance.settings.hpColored).build());
            adder.add(new TextWidget(Text.of("§7===§rVanilla UI§7==="), textRenderer), grid.copyPositioner().marginBottom(10).marginTop(10));
            adder.add(CheckboxWidget.builder(Text.of("Change splash text to \"Welcome <PlayerName>\""), textRenderer).callback((checkbox, checked) -> {
                DevyMainClient.instance.settings.modifySplashText = checked;
            }).checked(DevyMainClient.instance.settings.modifySplashText).build());
            adder.add(new TextWidget(Text.of("§7===§rRendering§7==="), textRenderer), grid.copyPositioner().marginBottom(10).marginTop(10));
            adder.add(CheckboxWidget.builder(Text.of("Fullbright"), textRenderer).callback((checkbox, checked) -> {
                DevyMainClient.instance.settings.fullbright = checked;
                if(checked) {
                    DevyMainClient.instance.settings.lastBrightnessValue = client.options.getGamma().getValue();
                    client.options.getGamma().setValue(99D);
                }
                else{
                    client.options.getGamma().setValue(DevyMainClient.instance.settings.lastBrightnessValue);
                }
            }).checked(DevyMainClient.instance.settings.fullbright).build());
            adder.add(new TextWidget(Text.of("Warning: DO NOT CHANGE THE BRIGHTNESS IN VIDEO SETTINGS WHILE ENABLED!"), textRenderer), grid.copyPositioner().marginTop(3).marginLeft(1));
            SimplePositioningWidget.setPos(this.grid, 0, 0, width, height, 0.15f, 0.15f);
        }
    }
    class HUDSettings extends GridScreenTab{
        HUDSettings(){
            super(Text.of("HUDs"));
            GridWidget.Adder adder = this.grid.createAdder(1);
            adder.add(new TextWidget(Text.of("Enable or Disable HUDs using the checkboxes below"), textRenderer), grid.copyPositioner().alignHorizontalCenter().marginBottom(10));
            for(IRenderer renderer : api.getRegisteredRenderers()){
                adder.add(CheckboxWidget.builder(Text.of("- " + renderer.getName()), textRenderer).checked(renderer.isEnabled()).callback(this::changeSettings).build(), grid.copyPositioner().marginBottom(2));
            }
        }
        private void changeSettings(ClickableWidget checkbox, boolean checked) {
            for(IRenderer renderer : api.getRegisteredRenderers()){
                if(checkbox.getMessage().equals(Text.of("- " + renderer.getName()))){
                    ((HUD)renderer).setEnabled(((CheckboxWidget) checkbox).isChecked());
                }
            }
        }
    }
    class AdvancedSettings extends GridScreenTab{
        AdvancedSettings(){
            super(Text.of("Advanced"));
            GridWidget.Adder adder = this.grid.createAdder(1);
            adder.add(CheckboxWidget.builder(Text.of("- Enable Verbose Log"), textRenderer).checked(DevyMainClient.instance.settings.verboseLog).callback((checkbox, checked) -> {
                DevyMainClient.instance.settings.verboseLog = checked;
            }).build(), grid.copyPositioner().marginBottom(2));
        }
    }
}
