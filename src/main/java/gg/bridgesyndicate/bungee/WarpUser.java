package gg.bridgesyndicate.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.util.UUID;

public class WarpUser {
    private final PluginMain pluginMain;
    private final RabbitListener.WarpMessage warpMessage;

    public WarpUser(PluginMain pluginMain, RabbitListener.WarpMessage warpMessage) {
        this.pluginMain = pluginMain;
        this.warpMessage = warpMessage;
    }

    public void warp() {
        System.out.println("Warp " + warpMessage.getPlayer() + " to " + warpMessage.getHostname());
        ServerInfo serverInfo;
        ProxyServer proxy = pluginMain.getProxy();
        if ( (serverInfo = proxy.getServers().get(warpMessage.getHostname())) == null){
            serverInfo = pluginMain.getProxy().constructServerInfo(
                    warpMessage.getHost(), new InetSocketAddress(warpMessage.getHost(), warpMessage.getPort()),
                    "Welcome to " + warpMessage.getHostname(), false);
            proxy.getServers().put(warpMessage.getHostname(), serverInfo);
            System.out.println("server list: " + pluginMain.getProxy().getServers().toString());
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



