package gg.bridgesyndicate.bungee;

import org.junit.Test;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class WarpUserTest {
    @Test
    public void warpUserTest() throws UnknownHostException {
        final String myIP = "172.18.0.2";
        Inet4Address target = (Inet4Address) Inet4Address.getByName(myIP);
        assertEquals(myIP, target.getHostAddress());
    }
}