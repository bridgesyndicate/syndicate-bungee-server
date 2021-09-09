package gg.bridgesyndicate.testbungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public final class Testbungee extends Plugin implements Listener {
    @Override
    public void onEnable() {
        ProxyServer proxy = this.getProxy();
        getLogger().info("Yay! It loads!");
        proxy.getPluginManager().registerListener(this, this);
        ServerInfo world = proxy.constructServerInfo("world", new InetSocketAddress("localhost", 25566), "fuck you",false);
        proxy.getServers().put("world", world);
        System.out.println("server list: " + proxy.getServers().toString());
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        System.out.println("onlogin: " + event.getPlayer().getUniqueId());
        System.out.println("onlogin: " + event.getPlayer().getName());
        System.out.println("onlogin: " + event.getPlayer().getDisplayName());

        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
            System.out.println(player.getDisplayName());
        }
        ProxyServer proxy = this.getProxy();
        proxy.getScheduler().schedule( this, new Runnable() {
            @Override
            public void run() {
                int seconds = (int) (System.currentTimeMillis() / 1000);
                String worldName = (seconds %2 == 0 ) ? "world" : "lobby";
                System.out.println("Sending to " + worldName);
                ServerInfo world = proxy.getServerInfo(worldName);
                proxy.getPlayer(event.getPlayer().getUniqueId()).connect(world);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
