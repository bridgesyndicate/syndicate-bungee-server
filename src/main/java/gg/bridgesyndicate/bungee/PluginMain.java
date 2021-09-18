package gg.bridgesyndicate.bungee;

import com.amazonaws.http.HttpMethodName;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

public final class PluginMain extends Plugin implements Listener {
    @Override
    public void onEnable() {
        ProxyServer proxy = this.getProxy();
        getLogger().info("Yay! It loads!");
        proxy.getPluginManager().registerListener(this, this);
        this.getProxy().getScheduler().runAsync(this, new RabbitListener(this));
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        System.out.println("onlogin, uuid: " + playerUUID + ", " + "name: " +
                player.getName() + "displayName: " + player.getDisplayName());

        SyndicateWebServiceHttpClient syndicateWebServiceHttpClient;
        boolean development = false;

        if ( System.getenv("SYNDICATE_ENV") != null && System.getenv("SYNDICATE_ENV").equals("development")) {
            System.out.println("SYNDICATE_ENV is development, using local endpoints.");
            development = true;
        }

        try {
            syndicateWebServiceHttpClient = new
                    SyndicateWebServiceHttpClient("/auth/user/by-minecraft-uuid/" + playerUUID,
                    HttpMethodName.GET, development);
            int httpStatus = syndicateWebServiceHttpClient.get();
            String body = syndicateWebServiceHttpClient.getReturnValueString();

            System.out.println("status was " + httpStatus);
            System.out.println(body);

            if (httpStatus != 200) {
                KickCode kickCode = KickCode.deserialize(body);
                player.sendMessage(new ComponentBuilder("You are not registered").color(ChatColor.RED).create());
                player.sendMessage(new ComponentBuilder("Your code is " + kickCode.kickCode).color(ChatColor.RED).create());
//                TextComponent message = new TextComponent("Click me");
//                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, body));
//                player.sendMessage(message);
//                player.disconnect(new TextComponent("https://www.hero.net/"));
            } else {
                player.sendMessage(new ComponentBuilder("Welcome registered player").color(ChatColor.WHITE).create());
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
