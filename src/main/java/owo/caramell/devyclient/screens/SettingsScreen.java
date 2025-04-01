package owo.caramell.devyclient.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import owo.caramell.devyclient.HUD.IRenderer;
import owo.caramell.devyclient.HUD.Loader.HUD;
import owo.caramell.devyclient.HUD.Loader.HUDDraggable;
import owo.caramell.devyclient.HUD.Loader.HUDManager;
import owo.caramell.devyclient.client.DevyMainClient;

import java.util.Random;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createGuiSprite;
import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createLightDarkVariants;

public class SettingsScreen extends LightweightGuiDescription {
    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
    HUDManager api;
    protected SettingsScreen(Screen prevScreen, HUDManager api) {
        //super(Text.of("Client Advanced Settings"));
        lastScreen = prevScreen;
        this.api = api;


        // TODO: For version backwards compatibility, check if user is using a version that has Minecraft's Vanilla Tab Navigation.
        //this.tabNav = TabNavigationWidget.builder(this.tabManager, this.width).tabs(new MainSettings(), new HUDSettings(), new AdvancedSettings()).build();
        //this.addDrawableChild(this.tabNav);
        WButton button = new WButton();
        button.setLabel(Text.of("Back"));
        button.setOnClick(()->{
            this.client.setScreen(lastScreen);
        });
        WTabPanel tabs = new WTabPanel();
        /**DirectionalLayoutWidget directionalLayoutWidget = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
         directionalLayoutWidget.add(ButtonWidget.builder(Text.of("Back"), button -> close()).build());
         this.layout.forEachChild(child -> {
         child.setNavigationOrder(1);
         this.addDrawableChild(child);
         });**/
        //this.tabNav.selectTab(0, false);
        //this.initTabNavigation();
        WGridPanel mainSettingsGrid = new WGridPanel();
        WScrollPanel mainPanel = new WScrollPanel(mainSettingsGrid);
        // setBackgroundPainter are specified because it kept using the Light Mode background instead of Dark Mode. workaround I guess.
        mainPanel.setBackgroundPainter(createLightDarkVariants(createGuiSprite(LibGuiCommon.id("widget/panel_light")), createGuiSprite(LibGuiCommon.id("widget/panel_dark"))));

        WGridPanel hudSettingsGrid = new WGridPanel();
        WScrollPanel hudPanel = new WScrollPanel(hudSettingsGrid);
        hudPanel.setBackgroundPainter(createLightDarkVariants(createGuiSprite(LibGuiCommon.id("widget/panel_light")), createGuiSprite(LibGuiCommon.id("widget/panel_dark"))));

        WGridPanel advSettingsGrid = new WGridPanel();
        WScrollPanel advPanel = new WScrollPanel(advSettingsGrid);
        advPanel.setBackgroundPainter(createLightDarkVariants(createGuiSprite(LibGuiCommon.id("widget/panel_light")), createGuiSprite(LibGuiCommon.id("widget/panel_dark"))));

        mainPanel.setSize(client.currentScreen.width - 100, client.currentScreen.height - 50);

        initMainTab(tabs, mainSettingsGrid, mainPanel);
        initHUDTab(tabs, hudSettingsGrid, hudPanel);
        initAdvancedTab(tabs, advSettingsGrid, advPanel);

        mainSettingsGrid.add(button,1,10,3,1);
        hudSettingsGrid.add(button, 1, 10, 3, 1);
        advSettingsGrid.add(button, 1, 10, 3, 1);
        setRootPanel(tabs);
        rootPanel.validate(this);
    }

    private Screen lastScreen;
    private MinecraftClient client = MinecraftClient.getInstance();
    private TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    //private TabNavigationWidget tabNav;
    //private final TabManager tabManager = new TabManager(this::addDrawableChild, child -> this.remove((Element)child));
    //private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    private void initMainTab(WTabPanel tabs, WGridPanel mainGrid, WScrollPanel mainPanel){
        mainGrid.add(new WLabel(Text.translatable("owo.caramell.healthuititle")), 1, 1);

        WToggleButton alterstatusbar = new WToggleButton(Text.translatable("owo.caramell.alterstatusbar"));
        alterstatusbar.setToggle(DevyMainClient.instance.settings.alterStatusBar);
        alterstatusbar.setOnToggle(on -> {
            DevyMainClient.instance.settings.alterStatusBar = on;
        });
        mainGrid.add(alterstatusbar, 1, 2);
        // TODO: Change the text with corresponding translatation key.

        mainGrid.add(new WLabel(Text.of("Warning: Depends on HP Display to be enabled!")),1,3);

        WToggleButton hpColored = new WToggleButton(Text.of("Colored HP Text"));
        hpColored.setToggle(DevyMainClient.instance.settings.hpColored);
        hpColored.setOnToggle(on -> {
            DevyMainClient.instance.settings.hpColored = on;
        });
        mainGrid.add(hpColored, 1, 4);

        mainGrid.add(new WLabel(Text.of("§7===§rVanilla UI§7===")), 1, 5);

        WToggleButton modifySplashText = new WToggleButton(Text.of("Change splash text to \"Welcome <PlayerName>\""));
        modifySplashText.setToggle(DevyMainClient.instance.settings.modifySplashText);
        modifySplashText.setOnToggle(on -> {
            DevyMainClient.instance.settings.modifySplashText = on;
        });
        mainGrid.add(modifySplashText, 1, 6);

        mainGrid.add(new WLabel(Text.of("§7===§rRendering§7===")), 1 , 7);

        WToggleButton fullbright = new WToggleButton(Text.of("Fullbright"));
        fullbright.setToggle(DevyMainClient.instance.settings.fullbright);
        fullbright.setOnToggle(on -> {
            DevyMainClient.instance.settings.modifySplashText = on;
            if(on) DevyMainClient.instance.settings.lastBrightnessValue = client.options.getGamma().getValue();
            client.options.getGamma().setValue(on ? 99D : DevyMainClient.instance.settings.lastBrightnessValue);
        });

        mainGrid.add(fullbright, 1, 8);

        mainGrid.add(new WLabel(Text.of("Warning: DO NOT CHANGE THE BRIGHTNESS IN VIDEO SETTINGS WHILE ENABLED!")), 1, 9);
        tabs.add(mainPanel, tab -> tab.title(Text.translatable("owo.caramell.mainsettingstitle")).icon(new ItemIcon(new ItemStack(Items.REDSTONE))).tooltip(Text.of("General Client Modifications.")));
    }
    private void initHUDTab(WTabPanel tabs, WGridPanel mainGrid, WScrollPanel mainPanel){
        int i = 2;
        mainGrid.add(new WLabel(Text.of("Enable or Disable HUDs using the checkboxes below")),1,1);
        for(IRenderer renderer : api.getRegisteredRenderers()){
            WToggleButton toggleButton = new WToggleButton(Text.of("- " + renderer.getName()));
            toggleButton.setToggle(renderer.isEnabled());
            toggleButton.setOnToggle(on ->{
                if(renderer instanceof HUDDraggable){
                    ((HUDDraggable) renderer).setEnabled(on);
                }
            });
            mainGrid.add(toggleButton, 1, i);
            i++;
        }
        tabs.add(mainPanel, tab -> tab.title(Text.of("HUDs")).icon(new ItemIcon(new ItemStack(Items.COMMAND_BLOCK))).tooltip(Text.of("Toggle On-Screen Elements")));
    }
    private void initAdvancedTab(WTabPanel tabs, WGridPanel mainGrid, WScrollPanel mainPanel){
        WToggleButton verboseLogToggle = new WToggleButton(Text.of("- Enable Verbose Log"));
        verboseLogToggle.setToggle(DevyMainClient.instance.settings.verboseLog);
        verboseLogToggle.setOnToggle(on -> {
            DevyMainClient.instance.settings.verboseLog = on;
        });
        mainGrid.add(verboseLogToggle, 1, 1);
        WButton sendNotifDebug = new WButton();
        sendNotifDebug.setLabel(Text.of("Test NotifAPI"));
        sendNotifDebug.setOnClick(() -> {
           DevyMainClient.instance.notifAPI.requestNotification("Notif Debug", new Random().nextInt());
        });
        mainGrid.add(sendNotifDebug, 1, 2, 5, 1);
        tabs.add(mainPanel, tab -> tab.title(Text.of("Advanced Options")).icon(new ItemIcon(new ItemStack(Items.REPEATING_COMMAND_BLOCK))).tooltip(Text.of("Contains useful settings for diagnostic or debugging.")));
    }
}
