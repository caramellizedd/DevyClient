package owo.caramell.devyclient.Configs;

import owo.caramell.devyclient.Utils.ConfigAPI;
import owo.caramell.devyclient.Utils.ConfigUtils;
import owo.caramell.devyclient.client.DevyMainClient;

import java.io.File;
import java.io.IOException;

public class StatusBarColors {
    public StatusBarColors(){
        instance = this;
    }
    public static StatusBarColors instance;
    public int HPColorContainer = 0xFFFF0000;
    public int HPColorNormal = 0xFFFFFF00;
    public int HPColorWithered = 0xFF000000;
    public int HPColorFrozen = 0xFFB2EDFF;
    public int HPColorPoisoned = 0xFF45FF01;
    public int HPColorAbsorbing = 0xFFFFB700;
    public void loadAll(){
        // TODO: Check if settings file exists, if not load default values
        if(config.getFile().exists()){
            try{
                HPColorContainer = Double.valueOf((double)config.loadSavedValue("HPColorContainer")).intValue();
                HPColorNormal = Double.valueOf((double)config.loadSavedValue("HPColorNormal")).intValue();
                HPColorWithered = Double.valueOf((double)config.loadSavedValue("HPColorWithered")).intValue();
                HPColorFrozen = Double.valueOf((double)config.loadSavedValue("HPColorFrozen")).intValue();
                HPColorPoisoned = Double.valueOf((double)config.loadSavedValue("HPColorPoisoned")).intValue();
                HPColorAbsorbing = Double.valueOf((double)config.loadSavedValue("HPColorAbsorbing")).intValue();
            }catch (Exception err){
                err.printStackTrace();
                if(err instanceof NullPointerException){
                    DevyMainClient.logger.error("An invalid or OUTDATED settings was found!");
                    DevyMainClient.logger.error("Attempting to fix...");
                    fixValues();
                }
            }
        }
        else loadDefaultValues();
    }
    public void saveAll(){
        config.set("HPColorContainer", HPColorContainer);
        config.set("HPColorNormal", HPColorNormal);
        config.set("HPColorWithered", HPColorWithered);
        config.set("HPColorFrozen", HPColorFrozen);
        config.set("HPColorPoisoned", HPColorPoisoned);
        config.set("HPColorAbsorbing", HPColorAbsorbing);
        try{
            config.save();
        }
        catch (Exception err){

        }
    }
    public ConfigUtils config = ConfigAPI.newConfiguration(new File(getJsonFolder(), "sbarcolors.cfg"));
    public void loadDefaultValues() {
        HPColorWithered = 0xFF000000;
        HPColorFrozen = 0xFFB2EDFF;
        HPColorPoisoned = 0xFF45FF01;
        HPColorAbsorbing = 0xFFFFB700;
        HPColorContainer = (int)config.loadSavedValue("HPColorContainer");
        HPColorNormal = (int)config.loadSavedValue("HPColorNormal");
        HPColorWithered = (int)config.loadSavedValue("HPColorWithered");
        HPColorFrozen = (int)config.loadSavedValue("HPColorFrozen");
        HPColorPoisoned = (int)config.loadSavedValue("HPColorPoisoned");
        HPColorAbsorbing = (int)config.loadSavedValue("HPColorAbsorbing");
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void fixValues(){
        // TODO: Change to switch-case statement.
        if(!config.checkIfValueExists("HPColorContainer")){
            HPColorContainer = 0xFFFF0000;
        }
        if(!config.checkIfValueExists("HPColorNormal")){
            HPColorNormal = 0xFFFFFF00;
        }
        if(!config.checkIfValueExists("HPColorWithered")){
            HPColorWithered = 0xFF000000;
        }
        if(!config.checkIfValueExists("HPColorFrozen")){
            HPColorFrozen = 0xFFB2EDFF;
        }
        if(!config.checkIfValueExists("HPColorPoisoned")){
            HPColorPoisoned = 0xFF45FF01;
        }
        if(!config.checkIfValueExists("HPColorAbsorbing")){
            HPColorAbsorbing = 0xFFFFB700;
        }
        saveAll();
    }
    public static void refreshSettings(){
        instance.loadAll();
    }
    private File getJsonFolder() {
        File folder = new File(ConfigUtils.getConfigDir(), Settings.instance.getClass().getSimpleName());
        folder.mkdirs();
        return folder;
    }
}
