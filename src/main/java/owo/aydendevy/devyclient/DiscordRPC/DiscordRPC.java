package owo.aydendevy.devyclient.DiscordRPC;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import owo.aydendevy.devyclient.client.DevyMainClient;

public class DiscordRPC {
    private boolean running = true;
    private long created = 0;
    // Initialize the DiscordRPC Service
    public void start(){
        this.created = System.currentTimeMillis();
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
            @Override
            public void apply(DiscordUser user) {
                //DevyClient.getInstance().logger.info("Welcome " + user.username + "#" + user.discriminator);
                DevyMainClient.logger.info("=====Discord user Information=====");
                DevyMainClient.logger.info("Username: " + user.username);
                if(Integer.parseInt(user.discriminator) != 0){
                    DevyMainClient.logger.info("Discriminator: " + user.discriminator);
                }
                DevyMainClient.logger.info("==================================");
                DevyMainClient.logger.info("DiscordRPC Initialized!");
            }
        }).build();
        net.arikia.dev.drpc.DiscordRPC.discordInitialize("1248928999833469009", handlers, true);
        new Thread("Discord RPC Event Thread"){
            @Override
            public void run() {
                while(running){
                    net.arikia.dev.drpc.DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();
    }

    public void shutdown(){
        running = false;
        // Does not shutdown discord, only the Rich Presence
        net.arikia.dev.drpc.DiscordRPC.discordShutdown();
    }
    // Update the Rich Presence with a text when hovering the icon
    public void update(String first, String Second, String Hover){
        DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(Second);
        b.setBigImage("large", Hover);
        b.setDetails(first);
        b.setStartTimestamps(created);
        net.arikia.dev.drpc.DiscordRPC.discordUpdatePresence(b.build());
    }
    // Update the Rich Presence without a text when hovering the icon
    public void update(String first, String Second){
        DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(Second);
        b.setBigImage("large", "");
        b.setDetails(first);
        b.setStartTimestamps(created);
        net.arikia.dev.drpc.DiscordRPC.discordUpdatePresence(b.build());
    }
}
