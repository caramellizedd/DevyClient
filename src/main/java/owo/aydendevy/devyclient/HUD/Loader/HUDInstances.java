package owo.aydendevy.devyclient.HUD.Loader;

import owo.aydendevy.devyclient.HUD.Mods.ArmorDisplay;
import owo.aydendevy.devyclient.HUD.Mods.HPDisplay;
import owo.aydendevy.devyclient.Utils.ConfigAPI;
import owo.aydendevy.devyclient.Utils.ConfigUtils;

import java.io.File;

public class HUDInstances {
    public static ConfigUtils cfg = ConfigAPI.newConfiguration(new File(getJsonFolder(), "ModPos.json"));
    public static HPDisplay hpDisplay = new HPDisplay();
    public static ArmorDisplay armor = new ArmorDisplay();
    public static void init(HUDManager api){
        api.register(hpDisplay);
        api.register(armor);
    }
    private static File getJsonFolder() {
        ConfigUtils.getConfigDir().mkdirs();
        return ConfigUtils.getConfigDir();
    }
}
