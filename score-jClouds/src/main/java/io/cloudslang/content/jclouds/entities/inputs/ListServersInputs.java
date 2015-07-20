package io.cloudslang.content.jclouds.entities.inputs;

/**
 * Created by persdana on 6/23/2015.
 */
public class ListServersInputs extends ListRegionsInputs {
    public static final String REGION = "region";

    private String region;

    public ListServersInputs(String provider, String identity, String credential, String endpoint, String proxyHost, String proxyPort, String region, String delimiter) {
        super(provider, identity, credential, endpoint, proxyHost, proxyPort, delimiter);
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
