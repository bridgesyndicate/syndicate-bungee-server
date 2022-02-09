package gg.bridgesyndicate.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class WarpUser {
    private final WarpMessage warpMessage;
    private final WarpCache  warpCache;
    private final ProxyServer proxy;
    private final PluginMain pluginMain;

    public WarpUser(PluginMain pluginMain, WarpMessage warpMessage) {
        this.pluginMain = pluginMain;
        this.proxy = pluginMain.getProxy();
        this.warpMessage = warpMessage;
        this.warpCache = WarpCache.getInstance();
    }

    private boolean hasWarpTarget() {
        return warpMessage.getHostname() != null;
    }

    private boolean isClearCacheMessage() {
        return warpMessage.getHostname().equals(pluginMain.LOBBY_HOSTNAME);
    }

    private void clearCacheForUser(UUID uuid) {
        System.out.println("clearCacheForUser: " + uuid);
        warpCache.getLruMap().remove(uuid);
    }

    private Inet4Address getCachedHostForUUID(String minecraft_uuid) {
        UUID uuid = UUID.fromString(minecraft_uuid);
        Inet4Address inet4Address = warpCache.getLruMap().get(uuid);
        if (inet4Address == null)
            System.out.println("Cache is empty for uuid: " + uuid);
        return inet4Address;
    }

    private ServerInfo getServerInfoForHost(Inet4Address target) {
        String hostname = target.getHostAddress();
        ServerInfo serverInfo = proxy.getServers().get(hostname);
        if (serverInfo == null) {
            System.out.println("Make new ServerInfo for " + hostname);
            int MINECRAFT_PORT = 25565;
            serverInfo = proxy.constructServerInfo(hostname,
                    new InetSocketAddress(hostname, MINECRAFT_PORT),
                    "Welcome to " + hostname, false);
            proxy.getServers().put(hostname, serverInfo);
        } else {
            System.out.println("existing ServerInfo for " + hostname);
        }
        return serverInfo;
    }

    private void warpPlayerToServer(ServerInfo serverInfo) {
        ProxiedPlayer player;
        if ( (player = proxy.getPlayer(UUID.fromString(warpMessage.getMinecraftUuid()))) != null){
            System.out.println("Found " + warpMessage.getMinecraftUuid() + " as " + player.getDisplayName() + " warping to "
                    + serverInfo.getName());
            player.connect(serverInfo);
        }
    }

    private void addWarpToCache(Inet4Address target) {
        if (target == null)
            System.out.println("ERROR we should never add a null target to the cache.");
        warpCache.getLruMap().put(UUID.fromString(warpMessage.getMinecraftUuid()), target);
    }

    public void warp() throws UnknownHostException {
        Inet4Address target;
        ServerInfo serverInfo;
        if (!hasWarpTarget()) {
            if ((target = getCachedHostForUUID(warpMessage.getMinecraftUuid())) == null) {
                System.out.println("No target for newly-joined user: " + warpMessage.getMinecraftUuid());
                return;
            } else {
                serverInfo = getServerInfoForHost(target);
            }
        } else {
            if (isClearCacheMessage()) {
                clearCacheForUser(UUID.fromString(warpMessage.getMinecraftUuid()));
                serverInfo = proxy.getServers().get(pluginMain.LOBBY_HOSTNAME);
                target = null; // assumes a clear cache message will never be cached.
            } else {
                target = (Inet4Address) Inet4Address.getByName(warpMessage.getHostname());
                serverInfo = getServerInfoForHost(target);
            }
        }
        warpPlayerToServer(serverInfo);
        if (warpMessage.isCached()) {
            addWarpToCache(target);
        }
    }
}
