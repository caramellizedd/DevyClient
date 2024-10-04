package owo.aydendevy.devyclient.DiscordRPC;

import net.minecraft.client.MinecraftClient;
import owo.aydendevy.devyclient.client.DevyMainClient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadNativeLibrary {
    public static File downloadDiscordLibrary() throws IOException {
        DevyMainClient.logger.info("Downloading GameSDK Library...");
        // Find out which name Discord's library has (.dll for Windows, .so for Linux)
        String name = "discord_game_sdk";
        String suffix;

        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

        if(osName.contains("windows"))
        {
            suffix = ".dll";
        }
        else if(osName.contains("linux"))
        {
            suffix = ".so";
        }
        else if(osName.contains("mac os"))
        {
            suffix = ".dylib";
        }
        else
        {
            throw new RuntimeException("cannot determine OS type: "+osName);
        }

		/*
		Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
		At this point we need the "x86_64" version, as this one is used in the ZIP.
		 */
        if(arch.equals("amd64"))
            arch = "x86_64";

        // Path of Discord's library inside the ZIP
        String zipPath = "lib/"+arch+"/"+name+suffix;
        File tempDir = new File(MinecraftClient.getInstance().runDirectory.getPath() + "/DevyClient-lib/", "java-"+name);
        File temp = new File(tempDir, name+suffix);
        // Open the URL as a ZipInputStream
        URL downloadUrl = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");
        ZipInputStream zin;
        if(!tempDir.exists()){
            zin = new ZipInputStream(downloadUrl.openStream());
            ZipEntry entry;
            while((entry = zin.getNextEntry())!=null)
            {
                if(entry.getName().equals(zipPath))
                {
                    // Create a new temporary directory
                    // We need to do this, because we may not change the filename on Windows

                    DevyMainClient.logger.info("Checking: " + tempDir.getPath());
                    if(!tempDir.exists()){
                        tempDir.mkdirs();
                    }

                    //tempDir.deleteOnExit();

                    // Create a temporary file inside our directory (with a "normal" name)

                    DevyMainClient.logger.info("Checking: " + temp.getPath());
                    //temp.deleteOnExit();
                    if(temp.exists()) {
                        zin.close();
                        DevyMainClient.logger.info("Library is already downloaded!");
                        return temp;
                    }

                    // Copy the file in the ZIP to our temporary file
                    Files.copy(zin, temp.toPath());

                    // We are done, so close the input stream
                    zin.close();
                    DevyMainClient.logger.info("Downloaded Successfully!");
                    // Return our temporary file
                    return temp;
                }
                // next entry
                zin.closeEntry();
            }
        }else{
            DevyMainClient.logger.info("Library is already downloaded!");
            return temp;
        }

        DevyMainClient.logger.info(zipPath);

        // Search for the right file inside the ZIP
        DevyMainClient.logger.error("Unable to download GameSDK!");
        //zin.close();
        // We couldn't find the library inside the ZIP
        return null;
    }
}
