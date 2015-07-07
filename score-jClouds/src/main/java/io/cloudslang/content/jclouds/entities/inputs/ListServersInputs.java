package io.cloudslang.content.jclouds.entities.inputs;

/**
 * Created by persdana on 6/23/2015.
 */
public class ListServersInputs extends ServerIdentificationInputs {
    public static final String DELIMITER = "delimiter";

    private String delimiter;

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

}
