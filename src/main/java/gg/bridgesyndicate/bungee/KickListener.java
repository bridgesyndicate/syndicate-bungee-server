package gg.bridgesyndicate.bungee;
// based on https://github.com/ryans1230/BungeeKickListener

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class KickListener implements Listener {
    private final PluginMain plugin;

    public KickListener(PluginMain pluginMain) {
        this.plugin = pluginMain;
    }

    @EventHandler
    public void onServerKickEvent(ServerKickEvent e) {
        String reason = BaseComponent.toLegacyText(e.getKickReasonComponent());
        String from = e.getKickedFrom().getName();
        System.out.println("player " + e.getPlayer().getUniqueId() + " kicked from " + from + " for reason: " + reason);
        ServerInfo lobby = plugin.getProxy().getServers().get("lobby");
        e.setCancelServer(lobby);
        e.setCancelled(true);
        e.getPlayer().sendMessage(
                new ComponentBuilder(ChatColor.RED + "Your connection to" + " " +
                        e.getKickedFrom().getName() + " was interrupted." + " " +
                        "The stated reason was " + reason + " " +
                        "You have been connected to: " +
                        e.getCancelServer().getName()).color(ChatColor.RED).create());
    }
}
