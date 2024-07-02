package owo.aydendevy.devyclient;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import owo.aydendevy.devyclient.HUD.ScaledResolution;
import owo.aydendevy.devyclient.Utils.ConfigUtils;
import owo.aydendevy.devyclient.client.DevyMainClient;

import java.io.File;

public class DevyClient implements ModInitializer {
    @Override
    public void onInitialize() {
        DevyMainClient.logger.info("Testing classes...");
        DevyMainClient.logger.info("Initializing Settings...");
        ConfigUtils.init();
        DevyMainClient.logger.info("Client has been initialized!");
    }

    public static void test() {
        DevyMainClient.logger.info("Testing ScaledResolution...");
        ScaledResolution sr = new ScaledResolution(MinecraftClient.getInstance());
        DevyMainClient.logger.info("sr.getScaledWidth_double(); returns = " + sr.getScaledWidth_double());
        DevyMainClient.logger.info("sr.getScaledHeight(); returns = " + sr.getScaledHeight());
        DevyMainClient.logger.info("sr.getScaledWidth(); returns = " + sr.getScaledWidth());
        DevyMainClient.logger.info("sr.getScaleFactor(); returns = " + sr.getScaleFactor());
        DevyMainClient.logger.info("sr.getScaledHeight_double(); returns = " + sr.getScaledHeight_double());
    }
}
