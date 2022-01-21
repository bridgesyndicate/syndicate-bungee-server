package gg.bridgesyndicate.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ServerConnectListener implements Listener {

    private final PluginMain plugin;
    private final ProxyServer proxy;
    private final WarpCache  warpCache;

    public ServerConnectListener(PluginMain pluginMain) {
        this.plugin = pluginMain;
        this.proxy = pluginMain.getProxy();
        this.warpCache = WarpCache.getInstance();
    }

    @EventHandler
    public void onServerConnect( ServerConnectEvent e ) {
        UUID id = e.getPlayer().getUniqueId();
        if ( warpCache.hasEntryFor(id) && e.getTarget() == proxy.getServerInfo("lobby") ){
            e.setCancelled(true);
        }
    }
}
