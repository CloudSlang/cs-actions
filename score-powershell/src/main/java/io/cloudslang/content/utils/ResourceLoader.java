package io.cloudslang.content.utils;

import org.apache.commons.io.IOUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * Created by giloan on 3/26/2016.
 */
public class ResourceLoader {

    /**
     * Loads the contents of a project resource file in a string.
     *
     * @param resourceFileName The name of the resource file.
     * @return A string value representing the entire content of the resource.
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String loadAsString(String resourceFileName) throws IOException, URISyntaxException {
        InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(resourceFileName);
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(is, stringWriter, StandardCharsets.UTF_8);
        return stringWriter.toString();
    }

    public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        builderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return builderFactory.newDocumentBuilder();
    }
}
