package io.cloudslang.content.xml.services;

/**
 * Created by persdana on 12/3/2014.
 */
public interface JsonConverter {

    /**
     * Convert XML string (XML document) to JSON string
     *
     * @param xml the XML string
     * @return the JSON string
     */
    public String convertToJsonString(String xml) throws Exception;
}
