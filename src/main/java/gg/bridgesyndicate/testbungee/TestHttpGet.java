package gg.bridgesyndicate.testbungee;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

public class TestHttpGet {
    public static void main(String[] args) throws URISyntaxException, IOException {
//        UUID uuid = UUID.randomUUID();
        String uuid = "c5ca7535-2946-4cba-8863-511cc739c0c0";
        SyndicateWebServiceHttpClient syndicateWebServiceHttpClient =
                new SyndicateWebServiceHttpClient("/auth/user/by-minecraft-uuid/" + uuid);
        int httpStatus = syndicateWebServiceHttpClient.get();
        System.out.println("status was " + httpStatus);
        System.out.println(syndicateWebServiceHttpClient.getReturnValueString());
    }
}
