package gg.bridgesyndicate.bungee;

import com.amazonaws.http.HttpMethodName;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestHttpGet {
    public static void main(String[] args) throws URISyntaxException, IOException {
//        UUID uuid = UUID.randomUUID();
        boolean development = false;
        String uuid = "c5ca7535-2946-4cba-8863-511cc739c0c0";
        SyndicateWebServiceHttpClient syndicateWebServiceHttpClient =
                new SyndicateWebServiceHttpClient("/auth/user/by-minecraft-uuid/" + uuid, HttpMethodName.GET, development);
        int httpStatus = syndicateWebServiceHttpClient.get();
        System.out.println("status was " + httpStatus);
        System.out.println(syndicateWebServiceHttpClient.getReturnValueString());
//        KickCode kickCode = new KickCode();
//        kickCode.setKickCode("foo");
//        String json = kickCode.serialize();
//        System.out.println(json);
//        KickCode kickCode2 = KickCode.deserialize(json);
//        System.out.println((kickCode2.serialize()));
    }
}
