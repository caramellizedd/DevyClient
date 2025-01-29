package owo.caramell.devyclient.client;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.WorldSavePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import owo.caramell.devyclient.DevyClient;
import owo.caramell.devyclient.DiscordRPC.DiscordRPC;
import owo.caramell.devyclient.HUD.Loader.HUDManager;
import owo.caramell.devyclient.Settings;

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
        getDiscordRPC().start();
        //getDiscordRPC().update("Loading Client", "Running v0.1");
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

    public void tick() {
        if(MinecraftClient.getInstance().currentScreen != null){
            DevyMainClient.instance.getDiscordRPC().update("In " + MinecraftClient.getInstance().currentScreen.getTitle().getString(), "");
        }
        if(MinecraftClient.getInstance().world !=null && !MinecraftClient.getInstance().isPaused()){
            if(MinecraftClient.getInstance().isInSingleplayer())
                DevyMainClient.instance.getDiscordRPC().update("In Singleplayer", MinecraftClient.getInstance().getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString());
        }
    }
    /**
     * Debug Settings
     * These settings are for debugging purposes only
     * This settings WILL reset after reset so set the values here to make it consistent
     * This settings can be changed in-game using the Debug Hotkey
     *
     * NOTE: Please set ALL OF THESE to FALSE before publishing
     */
    public boolean alwaysShowGUIName = false;
}
