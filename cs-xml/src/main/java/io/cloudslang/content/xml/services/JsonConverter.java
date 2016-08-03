package io.cloudslang.content.xml.services;

import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import java.io.IOException;

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
