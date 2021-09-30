package gg.bridgesyndicate.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class WarpUser {
    private final PluginMain pluginMain;
    private final RabbitListener.WarpMessage warpMessage;
    private final WarpCache  warpCache;
    private final int MINECRAFT_PORT = 25565;

    public WarpUser(PluginMain pluginMain, RabbitListener.WarpMessage warpMessage) {
        this.pluginMain = pluginMain;
        this.warpMessage = warpMessage;
        this.warpCache = WarpCache.getInstance();
    }

    public void warp() {
        if (warpMessage.getHost() == null) {
            warpWithCacheEntry();
        } else {
            warpWithMessage();
        }
    }

    private void warpWithCacheEntry() {
        System.out.println("Warp " + warpMessage.getPlayer() + " with cache.");
        ServerInfo serverInfo;
        ProxyServer proxy = pluginMain.getProxy();
        Inet4Address inet4Address = warpCache.getLruMap().get(UUID.fromString(warpMessage.getPlayer()));
        if (inet4Address == null) {
            System.out.println("Cache is empty for " + warpMessage.getPlayer());
            return; // there is no cached entry
        }
        System.out.println("Inet4Address: " + inet4Address);
        String host = inet4Address.getHostAddress();
        System.out.println("Host: " + host);
        if ( (serverInfo = proxy.getServers().get(host)) == null){
            serverInfo = pluginMain.getProxy().constructServerInfo(
                    host, new InetSocketAddress(host, MINECRAFT_PORT),
                    "Welcome to " + host, false);
            proxy.getServers().put(host, serverInfo);
            System.out.println("server list: " + pluginMain.getProxy().getServers().toString());
        }
        // find and warp the user
        ProxiedPlayer player;
        if ( (player = proxy.getPlayer(UUID.fromString(warpMessage.getPlayer()))) != null){
            System.out.println("Found " + warpMessage.getPlayer() + " as " + player.getDisplayName() + " warping to "
                    + host);
            player.connect(serverInfo);
        }
    }

    private void warpWithMessage()  {
        System.out.println("Warp " + warpMessage.getPlayer() + " to " + warpMessage.getHostname());
        ServerInfo serverInfo;
        ProxyServer proxy = pluginMain.getProxy();
        if ( (serverInfo = proxy.getServers().get(warpMessage.getHostname())) == null){
            serverInfo = pluginMain.getProxy().constructServerInfo(
                    warpMessage.getHost(), new InetSocketAddress(warpMessage.getHost(), MINECRAFT_PORT),
                    "Welcome to " + warpMessage.getHostname(), false);
            proxy.getServers().put(warpMessage.getHostname(), serverInfo);
            System.out.println("server list: " + pluginMain.getProxy().getServers().toString());
        }
        // add the info to the cache
        try {
            warpCache.getLruMap()
                    .put(UUID.fromString(warpMessage.getPlayer()),
                            (Inet4Address) Inet4Address.getByName(warpMessage.getHost()));
        } catch (UnknownHostException e) {
            System.out.println("Cannot convert " + warpMessage.getHost() + " to InetAddress for addition to cache.");
            e.printStackTrace();
            return;
        }
        // find and warp the user
        ProxiedPlayer player;
        if ( (player = proxy.getPlayer(UUID.fromString(warpMessage.getPlayer()))) != null){
            System.out.println("Found " + warpMessage.getPlayer() + " as " + player.getDisplayName() + " warping to "
            + warpMessage.getHostname());
            player.connect(serverInfo);
        }
    }
}



