package gg.bridgesyndicate.bungee;

import com.fasterxml.jackson.databind.util.LRUMap;
import java.net.Inet4Address;
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
}