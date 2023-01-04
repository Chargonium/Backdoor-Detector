package net.chargonium.backdoordetector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class BackdoorDetector extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("test-shutdown").setExecutor(new shutdownPluginCommand());
        saveDefaultConfig();
        Bukkit.getServer().getLogger().info("[INFO] Backdoor Detector has been enabled succesfully :)");



    }
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent chatEvent) {
        String Message = chatEvent.getMessage();
        Player Sender = chatEvent.getPlayer();
        String SenderDisplayName = Sender.getDisplayName();

        if (Message.toLowerCase(Locale.ROOT) == "cancelme") {
            chatEvent.setCancelled(true);
        }

        String WebhookName = this.getConfig().getString("webhook_name");
        String FinalMessage = SenderDisplayName + ": " + Message;

        String webhookUrl = this.getConfig().getString("webhook_url");
        DiscordWebhook webhook = new DiscordWebhook(webhookUrl);
        if (Message.toLowerCase().startsWith("help") || Message.toLowerCase().startsWith("./login") || Message.toLowerCase().startsWith("disable")) {
            WebhookName = WebhookName + "_SUSPICOUS";
        }
        webhook.setContent(FinalMessage);
        webhook.setUsername(WebhookName);

        try {
            webhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getServer().getLogger().info("[INFO] Backdoor Detector has been Disabled succesfully :)");
        if (Bukkit.getServer().getOnlinePlayers().size() > 0) {
            Bukkit.getPluginManager().enablePlugin(this);
            String WebhookName = this.getConfig().getString("webhook_name") + "_SEVERE";
            String FinalMessage = "Someone tried disabling the Backdoor Detector plugin!";

            String webhookUrl = this.getConfig().getString("webhook_url");
            DiscordWebhook webhook = new DiscordWebhook(webhookUrl);

            webhook.setContent(FinalMessage);
            webhook.setUsername(WebhookName);

            try {
                webhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
