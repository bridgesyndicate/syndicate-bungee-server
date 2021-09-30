package gg.bridgesyndicate.bungee;

import com.fasterxml.jackson.databind.util.LRUMap;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.UUID;

public class WarpCache {

    public LRUMap<UUID, Inet4Address> getLruMap() {
        return lruMap;
    }

    private final LRUMap<UUID, Inet4Address> lruMap;

    private static class Loader {
        static final WarpCache INSTANCE = new WarpCache();
    }

    WarpCache() {
        this.lruMap = new LRUMap<>(1024, 65535);
    }

    public static WarpCache getInstance() {
        return Loader.INSTANCE;
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println("Hello World!");
        WarpCache warpCache = WarpCache.getInstance();
        warpCache.getLruMap().put(UUID.randomUUID(), (Inet4Address) Inet4Address.getByName("10.0.0.1"));
        System.out.println(warpCache.getLruMap().size() + " : " + warpCache.getLruMap());
        WarpCache warpCache2 = WarpCache.getInstance();
        System.out.println(warpCache2.getLruMap().size() + " : " + warpCache2.getLruMap());
    }
}