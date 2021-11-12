package gg.bridgesyndicate.bungee;

import org.junit.Test;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.UUID;

import static org.junit.Assert.*;

public class WarpCacheTest {

    @Test
    public void warpCacheTest() throws UnknownHostException {
        WarpCache warpCache = WarpCache.getInstance();
        warpCache.getLruMap().put(UUID.randomUUID(), (Inet4Address) Inet4Address.getByName("10.0.0.1"));
        assertEquals(1, warpCache.getLruMap().size());
        WarpCache warpCache2 = WarpCache.getInstance();
        assertEquals(1, warpCache2.getLruMap().size());
    }
}