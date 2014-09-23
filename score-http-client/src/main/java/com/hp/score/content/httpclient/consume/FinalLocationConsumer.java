package com.hp.score.content.httpclient.consume;

import com.hp.score.content.httpclient.ScoreHttpClient;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class FinalLocationConsumer {
    private URI uri;
    private HttpHost targetHost;
    private List<URI> redirectLocations;

    public FinalLocationConsumer setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public FinalLocationConsumer setTargetHost(HttpHost targetHost) {
        this.targetHost = targetHost;
        return this;
    }

    public FinalLocationConsumer setRedirectLocations(List<URI> redirectLocations) {
        this.redirectLocations = redirectLocations;
        return this;
    }

    public void consume(Map<String, String> returnResult) {
        URI location;
        try {
            location = URIUtils.resolve(uri, targetHost, redirectLocations);
        } catch (URISyntaxException e) {
            //this is not a fatal error
            throw new IllegalArgumentException("could not determine '"+ ScoreHttpClient.FINAL_LOCATION
                    +"': " + e.getMessage(), e);
        }
        returnResult.put(ScoreHttpClient.FINAL_LOCATION, location.toASCIIString());
    }
}