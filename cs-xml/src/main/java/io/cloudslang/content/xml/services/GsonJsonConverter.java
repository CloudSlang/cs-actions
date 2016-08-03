package io.cloudslang.content.xml.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.opsware.pas.content.commons.util.XMLSecurityCommons;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by persdana on 11/17/2014.
 */
public class GsonJsonConverter implements JsonConverter {
    private static final String JSON_ATTRIBUTE_PREFIX = "@";
    private static final String DEFAULT_DELIMITER = ",";
    private static final String PREFIX_DELIMITER = ":";
    private static final String DEFAULT_TEXT_PROP_NAME = "_text";

    private StringBuilder namespacesPrefixes;
    private StringBuilder namespacesUris;
    private String parsingFeatures;

    private boolean addRootElement = true;
    private boolean prettyPrint = true;
    private boolean includeAttributes = true;
    private String textPropName = DEFAULT_TEXT_PROP_NAME;

    public GsonJsonConverter() {
        namespacesPrefixes = new StringBuilder();
        namespacesUris = new StringBuilder();
    }

    public String convertToJsonString(String xml) throws JDOMException, IOException, SAXException {
        String result = "";
        try {
            Reader stringReader = new StringReader(xml);
            InputSource inputSource = new InputSource(stringReader);
            SAXBuilder builder = new SAXBuilder();
            XMLSecurityCommons.setFeatures(builder, parsingFeatures);
            Document doc = builder.build(inputSource);
            Element root = doc.getRootElement();

            List<Element> xmlElements = Arrays.asList(root);
            JsonObject jsonObject = convertToJsonObject(xmlElements);

            //add root element
            JsonObject rootJson;
            if (addRootElement) {
                rootJson = new JsonObject();
                rootJson.add(root.getName(), jsonObject);
            } else {
                rootJson = jsonObject;
            }

            //pretty print
            if (prettyPrint) {
                Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                result = gson.toJson(rootJson);
            } else {
                result = rootJson.toString();
            }

        } catch (Exception e) {
            throw e;
        }
        return result;

    }

    private JsonObject convertToJsonObject(List<Element> xmlElements) {
        JsonObject result = new JsonObject();
        for (Element xmlElement : xmlElements) {
            List namespaces = xmlElement.getAdditionalNamespaces();
            List attributes = xmlElement.getAttributes();
            List<Element> children = xmlElement.getChildren();

            addNamespaces(namespaces);

            if (includeAttributes)
                addAttributes(result, attributes);

            //add jsonArrays
            String arrayName = containsArrays(children);
            while (arrayName != null) {
                //add array
                List<Element> elements = getElementsByName(arrayName, children);
                JsonArray jsonArray = convertToJsonArray(elements);
                result.add(arrayName, jsonArray);

                //eliminate what was added
                eliminateElementsByName(arrayName, children);

                //verify if it contains another array
                arrayName = containsArrays(children);
            }

            //add jsonObjects and jsonPrimitives
            for (Object o : children.toArray()) {
                if (o instanceof Element) {
                    Element element = (Element) o;
                    if (isPrimitiveElement(element)) {
                        JsonPrimitive jsonPrimitive = new JsonPrimitive(element.getValue());
                        result.add(getElementFullName(element), jsonPrimitive);
                    } else {
                        JsonElement jsonObject = convertToJsonObject(Arrays.asList(element));
                        result.add(getElementFullName(element), jsonObject);
                    }
                }
            }

            //add text prop
            addTextProp(result, xmlElement.getText());
        }

        return result;
    }

    private JsonArray convertToJsonArray(List<Element> xmlElements) {
        JsonArray result = new JsonArray();

        for (Element xmlElement : xmlElements) {
            List namespaces = xmlElement.getAdditionalNamespaces();
            List attributes = xmlElement.getAttributes();
            List<Element> children = xmlElement.getChildren();

            JsonElement jsonElement = new JsonObject();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            addNamespaces(namespaces);

            if (includeAttributes)
                addAttributes(jsonObject, attributes);

            String arrayName = containsArrays(children);
            while (arrayName != null) {
                //add array
                List<Element> elements = getElementsByName(arrayName, children);
                JsonArray jsonArray = convertToJsonArray(elements);
                jsonObject.add(arrayName, jsonArray);

                //eliminate what was added
                eliminateElementsByName(arrayName, children);

                //verify if it contains another array
                arrayName = containsArrays(children);
            }

            for (Object o : children.toArray()) {
                if (o instanceof Element) {
                    Element element = (Element) o;
                    //jsonObject.addProperty(element.getName(), element.getValue());
                    if (isPrimitiveElement(element)) {     //
                        JsonPrimitive jsonPrimitive = new JsonPrimitive(element.getValue());
                        jsonObject.add(getElementFullName(element), jsonPrimitive);
                    } else {
                        JsonElement jsonObject2 = convertToJsonObject(Arrays.asList(element));
                        jsonObject.add(getElementFullName(element), jsonObject2);
                    }
                }
            }

            //add text prop
            addTextProp(jsonObject, xmlElement.getText());

            result.add(jsonElement);
        }

        return result;
    }

    private void addTextProp(JsonObject jsonObject, String text) {
        if (text != null && !text.isEmpty()) {
            if (text.matches(".*[a-zA-Z0-9].*")) {
                jsonObject.addProperty(textPropName, text);
            }
        }
    }

    private void addNamespaces(List namespaces) {
        for (Object o : namespaces.toArray()) {
            if (o instanceof Namespace) {
                Namespace namespace = (Namespace) o;
                if (namespacesUris.length() > 0) {
                    namespacesPrefixes.append(DEFAULT_DELIMITER + namespace.getPrefix());
                    namespacesUris.append(DEFAULT_DELIMITER + namespace.getURI());
                } else {
                    namespacesPrefixes.append(namespace.getPrefix());
                    namespacesUris.append(namespace.getURI());
                }
            }
        }
    }

    private void addAttributes(JsonObject jsonObject, List namespaces) {
        for (Object o : namespaces.toArray()) {
            if (o instanceof Attribute) {
                Attribute attribute = (Attribute) o;
                jsonObject.addProperty(JSON_ATTRIBUTE_PREFIX + attribute.getName(), attribute.getValue());
            }
        }
    }

    private String getElementFullName(Element element) {
        String name = "";
        name += element.getNamespacePrefix();
        if (!name.isEmpty())
            name += PREFIX_DELIMITER;
        name += element.getName();

        return name;
    }

    private boolean isPrimitiveElement(Element e) {

        return e.getChildren().isEmpty() && e.getAttributes().isEmpty(); //if it doesn't have child and doesn't have attributes it's primitive.
    }

    /**
     * @param elements - a list of xml Elements
     * @return the name of the element that was found twice for the first time.
     */
    private String containsArrays(List<Element> elements) {
        List<String> names = new LinkedList();

        for (Element e : elements) {
            String name = getElementFullName(e);
            if (names.contains(name)) {
                return name;
            }
            names.add(name);
        }

        return null;
    }

    private List<Element> getElementsByName(String arrayName, List<Element> elementsToSearch) {
        List<Element> elements = new LinkedList<Element>();
        for (Element e : elementsToSearch) {
            if (getElementFullName(e).equals(arrayName)) {
                elements.add(e);
            }
        }
        return elements;
    }

    private void eliminateElementsByName(String arrayName, List<Element> children) {
        for (Iterator<Element> i = children.iterator(); i.hasNext(); ) {
            Element e = i.next();
            if (getElementFullName(e).equals(arrayName)) {
                i.remove();
            }
        }
    }

    public StringBuilder getNamespacesUris() {
        return namespacesUris;
    }

    public StringBuilder getNamespacesPrefixes() {
        return namespacesPrefixes;
    }

    public void setAddRootElement(boolean addRootElement) {
        this.addRootElement = addRootElement;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public void setIncludeAttributes(boolean includeAttributes) {
        this.includeAttributes = includeAttributes;
    }

    public void setTextPropName(String textPropName) {
        this.textPropName = textPropName;
    }

    public void setParsingFeatures(String parsingFeatures) {
        this.parsingFeatures = parsingFeatures;
    }
}
