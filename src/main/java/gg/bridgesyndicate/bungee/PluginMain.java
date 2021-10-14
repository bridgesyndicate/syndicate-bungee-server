package gg.bridgesyndicate.bungee;

import com.amazonaws.http.HttpMethodName;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.UUID;

public final class PluginMain extends Plugin implements Listener {
    @Override
    public void onEnable() {
        ProxyServer proxy = this.getProxy();
        getLogger().info("Yay! It loads!");
        proxy.getPluginManager().registerListener(this, this);
        proxy.getPluginManager().registerListener(this, new KickListener(this));
        proxy.getScheduler().runAsync(this, new RabbitListener(this));

        if ( System.getenv("LOBBY_HOSTNAME") != null ) {
            String hostname = System.getenv("LOBBY_HOSTNAME");
            System.out.println("Using LOBBY_HOSTNAME from env: " + hostname);
            String hostKey = "lobby";
            ServerInfo serverInfo = proxy.constructServerInfo(
                    hostKey, new InetSocketAddress(hostname, 25565),
                    "Welcome to the lobby", false);
            proxy.getServers().put(hostKey, serverInfo);
            System.out.println("init server list: " + proxy.getServers().toString());
        }
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        System.out.println("onlogin, uuid: " + playerUUID + ", " + "name: " +
                player.getName() + " displayName: " + player.getDisplayName());

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

            if (httpStatus == HttpStatus.SC_NOT_FOUND) {
                KickCode kickCode = KickCode.deserialize(body);
                player.sendMessage(new ComponentBuilder("You are not registered").color(ChatColor.RED).create());
                player.sendMessage(new ComponentBuilder("Your code is " + kickCode.kickCode).color(ChatColor.RED).create());
                TextComponent message = new TextComponent("Click me to copy your code");
                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.bridgesyndicate.gg/?" +
                        kickCode.kickCode));
                player.sendMessage(message);
            } else if (httpStatus == HttpStatus.SC_OK){
                player.sendMessage(new ComponentBuilder("Welcome registered player").color(ChatColor.WHITE).create());
                RabbitListener.WarpMessage warpMessage = new RabbitListener.WarpMessage(playerUUID.toString());
                WarpUser warpUser = new WarpUser(this, warpMessage);
                warpUser.warp();
            } else {
                System.out.println("status was " + httpStatus);
                player.sendMessage(new ComponentBuilder("Error. Some status other than 404 or 200.").color(ChatColor.WHITE).create());
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
