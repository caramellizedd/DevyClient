package owo.aydendevy.devyclient.Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigAPI {
    public static ConfigUtils loadExistingConfiguration(File file) throws IOException {
        String jsonStr = FileUtils.readFileToString(file, Charsets.UTF_8);
        Gson json = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> map = json.fromJson(jsonStr, type);
        return new ConfigUtils(file, map);
    }
    public String readFile(File file) throws IOException {
        return FileUtils.readFileToString(file, Charsets.UTF_8);
    }
    public static ConfigUtils newConfiguration(File file) {
        return new ConfigUtils(file);
    }
}
