package owo.aydendevy.devyclient.client;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import owo.aydendevy.devyclient.DevyClient;
import owo.aydendevy.devyclient.DiscordRPC.DiscordRPC;
import owo.aydendevy.devyclient.HUD.Loader.HUDInstances;
import owo.aydendevy.devyclient.HUD.Loader.HUDManager;
import owo.aydendevy.devyclient.Settings;
import owo.aydendevy.devyclient.StaticStrings;
import owo.aydendevy.devyclient.screens.ClientSSelectionScreen;

public class DevyMainClient implements ClientModInitializer {
    public static final Logger logger = LoggerFactory.getLogger(DevyClient.class);
    private DiscordRPC DiscordEvent = new DiscordRPC();
    public static DevyMainClient instance;
    public HUDManager hudManager;
    public Settings settings;
    public boolean configLoaded = false;

    @Override
    public void onInitializeClient() {
        logger.info("Initializing Client...");
        instance = this;
        logger.info("Initializing Disocrd RPC...");
        DiscordEvent.start();
        getDiscordRPC().update("DevyClient Revised - Testing Phase","Main Menu");
        logger.info("Discord RPC has been Initialized!");

        logger.info("Initializing Settings...");
        settings = new Settings();
        DevyMainClient.instance.settings.loadAll();
        logger.info("Settings has been Initialized!");

        logger.info("Main Client has been initialized!");
    }
    public DiscordRPC getDiscordRPC(){
        return DiscordEvent;
    }
}
