package owo.aydendevy.devyclient.mixins;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owo.aydendevy.devyclient.HUD.Loader.HUDInstances;
import owo.aydendevy.devyclient.HUD.Loader.HUDManager;
import owo.aydendevy.devyclient.client.DevyMainClient;

@Mixin(MinecraftClient.class)
public class MixinCraft {
    /**
     * @author Mojang, Modified by Charamellized
     * @reason Client Name Addition lol
     */
    @Overwrite
    private String getWindowTitle() {
        MinecraftClient client = MinecraftClient.getInstance();
        StringBuilder stringBuilder = new StringBuilder("Minecraft");
        if (MinecraftClient.getModStatus().isModded()) {
            stringBuilder.append("*");
        }
        stringBuilder.append(" ");
        stringBuilder.append(SharedConstants.getGameVersion().getName());
        ClientPlayNetworkHandler clientPlayNetworkHandler = client.getNetworkHandler();
        if (clientPlayNetworkHandler != null && clientPlayNetworkHandler.getConnection().isOpen()) {
            stringBuilder.append(" - ");
            ServerInfo serverInfo = client.getCurrentServerEntry();
            if (client.getServer() != null && !client.getServer().isRemote()) {
                stringBuilder.append(I18n.translate("title.singleplayer", new Object[0]));
            } else if (serverInfo != null && serverInfo.isRealm()) {
                stringBuilder.append(I18n.translate("title.multiplayer.realms", new Object[0]));
            } else if (client.getServer() != null || serverInfo != null && serverInfo.isLocal()) {
                stringBuilder.append(I18n.translate("title.multiplayer.lan", new Object[0]));
            } else {
                stringBuilder.append(I18n.translate("title.multiplayer.other", new Object[0]));
            }
        }
        stringBuilder.append(" | DevyClient Revised 0.1-DEV");
        return stringBuilder.toString();
    }
    @Inject(method = "<init>", at = @At("RETURN"))
    private void mcFinishInit(CallbackInfo ci){
        DevyMainClient.logger.info("Initializing HUD Manager...");
        HUDInstances.init(DevyMainClient.instance.hudManager = new HUDManager());
        DevyMainClient.logger.info("HUD Manager has been Initialized!");
    }
    @Inject(method = "stop", at = @At("HEAD"))
    private void MCClose(CallbackInfo ci){
        DevyMainClient.logger.info("Stopping Minecraft and Saving...");
        DevyMainClient.instance.settings.saveAll();
    }
}
