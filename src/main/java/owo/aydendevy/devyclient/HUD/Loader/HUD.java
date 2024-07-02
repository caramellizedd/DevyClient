package owo.aydendevy.devyclient.HUD.Loader;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import owo.aydendevy.devyclient.DevyClient;
import owo.aydendevy.devyclient.client.DevyMainClient;

public class HUD {
    public boolean isEnabled = true;

    protected final MinecraftClient mc;
    protected final TextRenderer font;
    protected final DevyMainClient client;
    public HUD(){
        this.mc = MinecraftClient.getInstance();
        this.font = this.mc.textRenderer;
        this.client = DevyMainClient.instance;

        setEnabled(isEnabled);
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        if(isEnabled){
            //EventManager.register(this);
        }else{
            //EventManager.unregister(this);
        }
    }
    public boolean isEnabled(){
        return isEnabled;
    }
}
