package io.cloudslang.content.jclouds.entities.inputs;

/**
 * Created by persdana on 7/13/2015.
 */
public class ListRegionsInputs extends CommonInputs {
    public static final String DELIMITER = "delimiter";
    private String delimiter;

    public ListRegionsInputs(String provider, String identity, String credential, String endpoint, String proxyHost, String proxyPort, String delimiter) {
        super(provider, identity, credential, endpoint, proxyHost, proxyPort);
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
