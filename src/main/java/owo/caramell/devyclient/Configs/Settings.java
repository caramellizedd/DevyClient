package owo.caramell.devyclient.Configs;

import owo.caramell.devyclient.Utils.ConfigAPI;
import owo.caramell.devyclient.Utils.ConfigUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Settings {
    public Settings(){
        instance = this;
    }
    public static Settings instance;

    public int notifPosType = 0;

    public boolean alterStatusBar = false;
    public boolean hpColored = false;
    public boolean modifySplashText = false;
    public boolean fullbright = false;
    public boolean verboseLog = false;
    public boolean showVersionIngame = true;

    public double lastBrightnessValue = 0.0D;
    public void loadAll(){
        // TODO: Probably find a better way to do this somehow.
        if(config.getFile().exists()){
            convertOldValues();
            checkAllValues();
            verboseLog = false;
            verboseLog = (boolean)config.loadSavedValue("verboseLog");
            alterStatusBar = (boolean)config.loadSavedValue("alterStatusBar");
            hpColored = (boolean)config.loadSavedValue("hpColored");
            modifySplashText = (boolean)config.loadSavedValue("modifySplashText");
            fullbright = (boolean)config.loadSavedValue("fullbright");
            lastBrightnessValue = (double)config.loadSavedValue("lastBrightnessValue");
            notifPosType = (int)config.loadSavedValue("notifPosType", true);
            showVersionIngame = (boolean)config.loadSavedValue("showVersionIngame");
        }
        else loadDefaultValues();
    }
    public void convertOldValues(){
        if(config.checkIfValueExists("hpLine")){
            alterStatusBar = (boolean)config.loadSavedValue("hpLine");
            config.remove("hpLine");
            config.set("alterStatusBar", alterStatusBar);
        }
    }
    public void saveAll(){
        config.set("alterStatusBar", alterStatusBar);
        config.set("hpColored", hpColored);
        config.set("modifySplashText", modifySplashText);
        config.set("fullbright", fullbright);
        config.set("verboseLog", verboseLog);
        config.set("lastBrightnessValue", lastBrightnessValue);
        config.set("notifPosType", notifPosType);

        try{
            config.save();
        }
        catch (Exception err){

        }
    }
    public ConfigUtils config = ConfigAPI.newConfiguration(new File(getJsonFolder(), "devyclient.cfg"));
    public void loadDefaultValues() {
        alterStatusBar = false;
        hpColored = true;
        modifySplashText = true;
        fullbright = false;
        verboseLog = false;
        lastBrightnessValue = 0.0D;
        notifPosType = 0;
        showVersionIngame = true;
        config.set("alterStatusBar", alterStatusBar);
        config.set("hpColored", hpColored);
        config.set("modifySplashText", modifySplashText);
        config.set("fullbright", fullbright);
        config.set("verboseLog", verboseLog);
        config.set("lastBrightnessValue", lastBrightnessValue);
        config.set("notifPosType", notifPosType);
        config.set("showVersionIngame", showVersionIngame);
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void checkAllValues(){
        if(!config.checkIfValueExists("verboseLog")) {
            verboseLog = false;
            config.set("verboseLog", verboseLog);
        }
        if(!config.checkIfValueExists("alterStatusBar")) {
            alterStatusBar = false;
            config.set("alterStatusBar", alterStatusBar);
        }
        if(!config.checkIfValueExists("hpColored")) {
            hpColored = true;
            config.set("hpColored", hpColored);
        }
        if(!config.checkIfValueExists("modifySplashText")) {
            modifySplashText = true;
            config.set("modifySplashText", modifySplashText);
        }
        if(!config.checkIfValueExists("fullbright")) {
            fullbright = false;
            config.set("fullbright", fullbright);
        }
        if(!config.checkIfValueExists("lastBrightnessValue")) {
            lastBrightnessValue = 0.0D;
            config.set("lastBrightnessValue", lastBrightnessValue);
        }
        if(!config.checkIfValueExists("notifPosType")) {
            notifPosType = 0;
            config.set("notifPosType", notifPosType);
        }
        if(!config.checkIfValueExists("showVersionIngame")){
            showVersionIngame = true;
            config.set("showVersionIngame", showVersionIngame);
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
