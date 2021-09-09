package gg.bridgesyndicate.testbungee;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

public class SyndicateWebServiceHttpClient {
    private static SyndicateWebServiceRequest syndicateWebServiceRequest;
    private String returnValueString = "";

    public SyndicateWebServiceHttpClient(String resourcePath) throws URISyntaxException {
        SyndicateAWSCredentials credentials = new SyndicateAWSCredentials();
        syndicateWebServiceRequest = new SyndicateWebServiceRequest(credentials, resourcePath);
    }

    public int get() throws IOException {
        return(doGet());
    }

    public String getReturnValueString() {
        return returnValueString;
    }

    private int doGet() throws IOException {
        Map<String, String> headerMap = syndicateWebServiceRequest.getRequest().getHeaders();
        Iterator<String> iterator = headerMap.keySet().iterator();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(syndicateWebServiceRequest.getURI());
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = headerMap.get(key);
                httpGet.addHeader(key, value);
            }
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    returnValueString = EntityUtils.toString(entity);
                }
                return (response.getStatusLine().getStatusCode());
            }
        }
    }
}

/*

Add -Djava.util.logging.config.file=logging.properties to your JVM for debugging of sigV4.

    handlers= java.util.logging.ConsoleHandler
    .level = ALL
    java.util.logging.FileHandler.pattern = %h/java%u.log
    java.util.logging.FileHandler.limit = 50000
    java.util.logging.FileHandler.count = 1
    java.util.logging.FileHandler.formatter = java.util.logging.XMLFormatter
    java.util.logging.ConsoleHandler.level = ALL
    java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter

 */