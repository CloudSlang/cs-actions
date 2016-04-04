package io.cloudslang.content.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by giloan on 3/26/2016.
 */
public class ResourceLoader {

    public static Document loadAsDocument(String resourceFileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = getDocumentBuilder();
        return builder.parse(ResourceLoader.class.getClassLoader().getResource(resourceFileName).getPath());
    }

    public static String loadAsString(String resourceFileName) throws IOException, URISyntaxException {
        byte[] encoded = Files.readAllBytes(Paths.get(ResourceLoader.class.getClassLoader().getResource(resourceFileName).toURI()));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return builderFactory.newDocumentBuilder();
    }
}
