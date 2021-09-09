package gg.bridgesyndicate.testbungee;

import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.http.HttpMethodName;
import java.net.URI;
import java.net.URISyntaxException;


public class SyndicateWebServiceRequest {

    private final String resourcePath;
    private Request<Void> request;

    public Request<Void> getRequest() {
        return request;
    }

    public SyndicateWebServiceRequest(SyndicateAWSCredentials credentials, String resourcePath) throws URISyntaxException {
        this.resourcePath = addEnvironmentToResource(resourcePath);
        request = createRequest();
        signRequest(request, credentials);
    }

    private Request<Void> createRequest() throws URISyntaxException {
        request = new DefaultRequest<>("execute-api");
        request.setHttpMethod(HttpMethodName.GET);
        request.setEndpoint(new URI(getEndpoint()));
        request.setResourcePath(this.resourcePath);
        return(request);
    }

    private void signRequest(Request<Void> request, SyndicateAWSCredentials credentials) {
        AWS4Signer signer = new AWS4Signer();
        signer.setRegionName(credentials.getRegion());
        signer.setServiceName(request.getServiceName());
        signer.sign(request, credentials.getCredentials());
    }

    private String addEnvironmentToResource(String resourcePath) {
        String environment = "/Prod";
        return(environment + resourcePath);
    }

    public String getEndpoint() {
        String host = "knopfnsxoh.execute-api.us-west-2.amazonaws.com";
        String protocol = "https";
        return(protocol + "://" + host);
    }

    public URI getURI() {
        String url = getEndpoint() + resourcePath;
        return (URI.create(url));
    }
}
