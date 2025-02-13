package owo.caramell.devyclient.DiscordRPC;

import owo.caramell.devyclient.Utils.ConfigUtils;
import owo.caramell.devyclient.client.DevyMainClient;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
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
        File dir = new File(ConfigUtils.getRootDir(), "lib");

        // Open the URL as a ZipInputStream
        URL url = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");
        HttpURLConnection downloadUrl = (HttpURLConnection) url.openConnection();
        downloadUrl.setConnectTimeout(5000);
        downloadUrl.setReadTimeout(5000);
        ZipInputStream zin = null;
        try{
            zin = new ZipInputStream(downloadUrl.getInputStream());
        }catch(Exception err){
            DevyMainClient.logger.error("Failed to download Discord Game SDK");
            err.printStackTrace();
            return null;
        }
        File libName = new File(dir, name+suffix);
        if(libName.exists()){
            DevyMainClient.logger.info("Library already downloaded!");
            return libName;
        }

        // Search for the right file inside the ZIP
        ZipEntry entry;
        while((entry = zin.getNextEntry())!=null)
        {
            if(entry.getName().equals(zipPath))
            {
                if(!dir.mkdir())
                    throw new IOException("Cannot create temporary directory");
                //dir.deleteOnExit();

                // Create a temporary file inside our directory (with a "normal" name)

                //libName.deleteOnExit();

                // Copy the file in the ZIP to our temporary file
                Files.copy(zin, libName.toPath());

                // We are done, so close the input stream
                zin.close();
                DevyMainClient.logger.info("Downloaded Successfully!");
                // Return our temporary file
                return libName;
            }
            // next entry
            zin.closeEntry();
        }
        DevyMainClient.logger.error("Unable to download GameSDK!");
        zin.close();
        // We couldn't find the library inside the ZIP
        return null;
    }
}
