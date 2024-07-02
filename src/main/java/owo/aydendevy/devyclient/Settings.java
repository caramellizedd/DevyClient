package owo.aydendevy.devyclient;

import owo.aydendevy.devyclient.Utils.ConfigAPI;
import owo.aydendevy.devyclient.Utils.ConfigUtils;

import java.io.File;
import java.io.IOException;

public class Settings {
    public Settings(){
        instance = this;
    }
    public static Settings instance;
    public boolean hpLine = false;
    public boolean hpColored = false;
    public boolean modifySplashText = false;
    public boolean fullbright = false;
    public boolean verboseLog = false;
    public double lastBrightnessValue = 0.0D;
    public void loadAll(){
        // TODO: Check if settings file exists, if not load default values
        if(config.getFile().exists()){
            verboseLog = false;
            verboseLog = (boolean)config.loadSavedValue("verboseLog");
            hpLine = (boolean)config.loadSavedValue("hpLine");
            hpColored = (boolean)config.loadSavedValue("hpColored");
            modifySplashText = (boolean)config.loadSavedValue("modifySplashText");
            fullbright = (boolean)config.loadSavedValue("fullbright");
            lastBrightnessValue = (double)config.loadSavedValue("lastBrightnessValue");
        }
        else loadDefaultValues();
    }
    public void saveAll(){
        config.set("hpLine", hpLine);
        config.set("hpColored", hpColored);
        config.set("modifySplashText", modifySplashText);
        config.set("fullbright", fullbright);
        config.set("verboseLog", verboseLog);
        config.set("lastBrightnessValue", lastBrightnessValue);

        try{
            config.save();
        }
        catch (Exception err){

        }
    }
    public ConfigUtils config = ConfigAPI.newConfiguration(new File(getJsonFolder(), "devyclient.cfg"));
    public void loadDefaultValues() {
        hpLine = false;
        hpColored = true;
        modifySplashText = true;
        fullbright = false;
        verboseLog = false;
        lastBrightnessValue = 0.0D;
        config.set("hpLine", hpLine);
        config.set("hpColored", hpColored);
        config.set("modifySplashText", modifySplashText);
        config.set("fullbright", fullbright);
        config.set("verboseLog", verboseLog);
        config.set("lastBrightnessValue", lastBrightnessValue);
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void refreshSettings(){
        instance.loadAll();
    }
    private File getJsonFolder() {
        File folder = new File(ConfigUtils.getConfigDir(), this.getClass().getSimpleName());
        folder.mkdirs();
        return folder;
    }
}
