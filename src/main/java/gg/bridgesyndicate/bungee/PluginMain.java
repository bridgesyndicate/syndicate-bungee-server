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

    public final String LOBBY_HOSTNAME = "lobby";

    private String getProxyDescription() {
        final String envName = "SYNDICATE_PROXY_DESCRIPTION";
        String description = "The Syndicate Bridge Lobby";
        if ( System.getenv(envName) != null ) {
            description = System.getenv(envName);
            System.out.println("Using " + envName + " from env: " + description);
        }
        return(description);
    }

    @Override
    public void onEnable() {
        ProxyServer proxy = this.getProxy();
        getLogger().info("Yay! It loads!");
        proxy.getPluginManager().registerListener(this, this);
        proxy.getPluginManager().registerListener(this, new KickListener(this));
        proxy.getPluginManager().registerListener(this, new ServerConnectListener(this));
        proxy.getScheduler().runAsync(this, new RabbitListener(this));
        if (proxy.getServers().get(LOBBY_HOSTNAME) == null){
            System.out.println("FATAL ERROR: no LOBBY_HOSTNAME defined in bungee configs.");
            System.exit(-1);
        }
        if ( System.getenv("LOBBY_HOSTNAME") != null ) {
            String hostname = System.getenv("LOBBY_HOSTNAME");
            System.out.println("Using LOBBY_HOSTNAME from env: " + hostname);
            String hostKey = "lobby";
            ServerInfo serverInfo = proxy.constructServerInfo(
                    hostKey, new InetSocketAddress(hostname, 25565), getProxyDescription(), false);
            System.out.println("before change, server list: " + proxy.getServers().toString());
            proxy.getServers().put(hostKey, serverInfo);
        }
        System.out.println("init server list: " + proxy.getServers().toString());
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

            if (httpStatus == HttpStatus.SC_NOT_FOUND) { // 404
                KickCode kickCode = KickCode.deserialize(body);
                ChatMessages.sendUnverifiedMessages(player, kickCode);

            } else if (httpStatus == HttpStatus.SC_OK){ // 200
                WarpMessage warpMessage = new WarpMessage(playerUUID);
                new WarpUser(this, warpMessage).warp();
                ChatMessages.sendVerifiedMessage(player);

            } else if (httpStatus == HttpStatus.SC_BAD_REQUEST){ // 400
                String error = "400: Bad Request";
                ChatMessages.sendErrorMessage(player, error);
                
            } else if (httpStatus == HttpStatus.SC_FORBIDDEN){ // 403
                String error = "403: Forbidden";
                ChatMessages.sendErrorMessage(player, error);

            } else if (httpStatus == HttpStatus.SC_INTERNAL_SERVER_ERROR){ // 500
                String error = "500: Internal Server Error";
                ChatMessages.sendErrorMessage(player, error);

            } else if (httpStatus == HttpStatus.SC_BAD_GATEWAY){ // 502
                String error = "502: Bad Gateway";
                ChatMessages.sendErrorMessage(player, error);
                
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
