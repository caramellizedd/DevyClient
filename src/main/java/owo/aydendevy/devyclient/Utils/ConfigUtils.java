package owo.aydendevy.devyclient.Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import owo.aydendevy.devyclient.HUD.ScreenPosition;
import owo.aydendevy.devyclient.client.DevyMainClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConfigUtils {
    private File file;
    public Map<String, Object> jsonData;
    private static File ROOT = new File("DevyClient");
    private static File ConfigDirectory = new File(ROOT, "Configuration");
    public static boolean init(){
        if(!ConfigDirectory.exists()) if(ConfigDirectory.mkdirs()) return true;
        return false;
    }
    public static File getConfigDir(){
        return ConfigDirectory;
    }
    public ConfigUtils(File file, Map<String, Object> jsonData) {
        this.file = file;
        this.jsonData = jsonData;
    }

    public ConfigUtils(File file) {
        this.file = file;
        this.jsonData = new HashMap<String, Object>();
    }

    public Object get(String key) {
        if(DevyMainClient.instance.settings.verboseLog)
            DevyMainClient.logger.info("[DevyIO/JSONRead] Reading key: " + key);
        return jsonData.get(key);
    }
    public Object loadSavedValue(String key) {
        if(DevyMainClient.instance.settings.verboseLog)
            DevyMainClient.logger.info("[DevyIO/JSONRead] Reading key: " + key);
        try {
            String str = FileUtils.readFileToString(file, Charsets.UTF_8);
            Gson json = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> map = json.fromJson(str, type);
            if(map.get(key) == null){
                DevyMainClient.logger.error("Failed loading config name: " + key);
                DevyMainClient.logger.error("Value not found or is using older config revision!");
                DevyMainClient.logger.error("Inserting Default Fallback Value...");
                try{
                    Field method = DevyMainClient.instance.settings.getClass().getDeclaredField(key);
                    if(method.getType().toString().equals("boolean")){
                        return false;
                    }else if(method.getType().toString().equals("double")){
                        return 0.0D;
                    }
                }
                catch (Exception er){
                    DevyMainClient.logger.error(er.toString());
                }
            }
            return map.get(key);
        }
        catch (Exception err){

        }
        return null;
    }public Object loadSavedValue2(String key) {
        if(DevyMainClient.instance.settings.verboseLog)
            DevyMainClient.logger.info("[DevyIO/JSONRead] Reading key: " + key);
        try {
            String str = FileUtils.readFileToString(file, Charsets.UTF_8);
            Gson json = new Gson();
            Type type = new TypeToken<Map<String, ScreenPosition>>(){}.getType();
            Map<String, ScreenPosition> map = json.fromJson(str, type);
            return map.get(key);
        }
        catch (Exception err){

        }
        return null;
    }
    public File getFile(){
        return file;
    }

    public void set(String key, Object value) {
        if(DevyMainClient.instance.settings.verboseLog)
            DevyMainClient.logger.info("[DevyIO/JSONWrite] Writing key: " + key + " | Value: " + value);
        jsonData.put(key, value);
    }

    public void save() throws IOException {
        Gson json = new Gson();
        Type typeObject = new TypeToken<HashMap>() {}.getType();
        String gsonData = json.toJson(jsonData, typeObject);
        if(!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(gsonData);
        fileWriter.flush();
        fileWriter.close();
    }
}
