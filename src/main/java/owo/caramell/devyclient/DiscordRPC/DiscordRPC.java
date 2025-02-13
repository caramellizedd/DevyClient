package owo.caramell.devyclient.DiscordRPC;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import owo.caramell.devyclient.client.DevyMainClient;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class DiscordRPC {
    // Migrate to Discord Game SDK - Charamellized
    public boolean running = false;
    private long created = 0;
    public Core core;
    Activity activity;
    // Initialize the DiscordRPC Service
    public boolean start(){
        this.created = System.currentTimeMillis();
        // Credits to JnCrMx for creating the Discord Game SDK Library and for this Example!
        try{
            File discordLibrary = DownloadNativeLibrary.downloadDiscordLibrary();
            if(discordLibrary == null){
                DevyMainClient.logger.error("Discord SDK Library Not found.");
                return false;
            }
            if(!discordLibrary.exists()) {
                DevyMainClient.logger.error("Discord SDK Library Not found.");
                return false;
            }
            // Initialize the Core
            Core.init(discordLibrary);
            try(CreateParams params = new CreateParams())
            {
                params.setClientID(1248928999833469009L);
                params.setFlags(CreateParams.getDefaultFlags());
                // Create the Core
                core = new Core(params);
                // Create the Activity
                activity = new Activity();

                activity.setDetails("Starting Client");
                activity.setState("Running v0.1");

                // Setting a start time causes an "elapsed" field to appear
                activity.timestamps().setStart(Instant.now());

                // Make a "cool" image show up
                activity.assets().setLargeImage("large");

                // Setting a join secret and a party ID causes an "Ask to Join" button to appear
                //activity.party().setID("Party!");
                //activity.secrets().setJoinSecret("Join!");

                // Finally, update the current activity to our activity
                core.activityManager().updateActivity(activity);

                // Run callbacks forever
                running = true;
            }
            return true;
        }catch (IOException err){
            return false;
        }
    }

    public void shutdown(){
        running = false;
        // Does not shutdown discord, only the Rich Presence

    }
    // Update the Rich Presence with a text when hovering the icon
    public void update(String first, String second, String hover){
        if(DevyMainClient.instance.discordRPCFailed) return;
        activity.setDetails(first);
        activity.setState(second);
        activity.assets().setLargeImage("large");
        activity.assets().setLargeText(hover);
        core.activityManager().updateActivity(activity);
    }
    // Update the Rich Presence without a text when hovering the icon
    public void update(String first, String second){
        if(DevyMainClient.instance.discordRPCFailed) return;
        activity.setDetails(first);
        activity.setState(second);
        activity.assets().setLargeImage("large");
        activity.assets().setLargeText("");
        core.activityManager().updateActivity(activity);
    }
}
