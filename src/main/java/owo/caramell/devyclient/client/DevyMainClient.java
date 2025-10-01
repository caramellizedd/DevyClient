package owo.caramell.devyclient.client;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.WorldSavePath;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import owo.caramell.devyclient.Configs.StatusBarColors;
import owo.caramell.devyclient.DevyClient;
import owo.caramell.devyclient.HUD.Loader.HUDManager;
import owo.caramell.devyclient.Configs.Settings;
import owo.caramell.devyclient.client.NotifsAPI.Notification;
import owo.caramell.devyclient.client.account.Account;
import owo.caramell.devyclient.client.account.AccountManager;

public class DevyMainClient implements ClientModInitializer {
    /**
     * DevyMainClient Class
     * Made by CRML Studios.
     * Licensed with GPL-3.0 License.
     *
     * This class is the main class for this Mod.
     * This is where most stuff happens.
     */
    public static final Logger logger = LoggerFactory.getLogger(DevyClient.class);
    public static DevyMainClient instance;
    public HUDManager hudManager;
    public Settings settings;
    public AccountManager accountManager;
    public StatusBarColors sBarColors;
    public boolean configLoaded = false;
    public boolean discordRPCFailed = false;
    public boolean isDALoggedIn = false;
    public Notification notifAPI;
    public Account account;
    // Controls
    private static KeyBinding fullbright;
    // Debug
    public int keyCode = 0;

    @Override
    public void onInitializeClient() {
        logger.info("Initializing Client...");
        instance = this;
        // Initialize controls
        fullbright = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "owo.caramell.keyfullbright",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "owo.caramell.controlscategory"
        ));

        logger.info("Initializing Settings...");
        settings = new Settings();
        sBarColors = new StatusBarColors();
        loadSettings();
        logger.info("Settings has been Initialized!");

        logger.info("Initializing NotifsAPI...");
        notifAPI = new Notification();
        logger.info("NotifsAPI has been Initialized!");

        logger.info("Initializing Account Manager...");
        accountManager = new AccountManager();
        logger.info("Account Manager has been Initialized!");


        logger.info("Main Client has been initialized!");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (fullbright.wasPressed()) {
                settings.fullbright = !settings.fullbright;
                if(settings.fullbright) {
                    DevyMainClient.instance.settings.lastBrightnessValue = client.options.getGamma().getValue();
                    client.options.getGamma().setValue(99D);
                }
                else{
                    client.options.getGamma().setValue(DevyMainClient.instance.settings.lastBrightnessValue);
                }
                settings.saveAll();
            }
        });
    }
    public void initCRMLAccount(String token){
        DevyMainClient.logger.info("Logging in...");
        account = new Account(token);
    }
    public void loadSettings(){
        DevyMainClient.instance.sBarColors.fixValues();
        DevyMainClient.instance.settings.loadAll();
        DevyMainClient.instance.sBarColors.loadAll();
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
    public boolean showKeyCode = false;
}
