package io.cloudslang.content.xml.services;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.*;

import static io.cloudslang.content.xml.utils.Constants.*;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertJsonToXmlService {
    private XMLOutputter xmlWriter;
    private Map<String, String> jsonArrayItemNames;
    private String jsonArrayItemName;
    private Map<String, Namespace> namespaces;


    public ConvertJsonToXmlService() {
        xmlWriter = new XMLOutputter();
        namespaces = new HashMap<>();
        jsonArrayItemNames = new HashMap<>();
    }

    public String convertToXmlString(String json, Boolean prettyPrint, Boolean showXmlDeclaration, String rootTagName, String jsonArrayItemName) {
        if (StringUtils.isBlank(json)) {
            return EMPTY_STRING;
        }
        this.jsonArrayItemName = jsonArrayItemName;
        Format format = prettyPrint ? Format.getPrettyFormat().setIndent(INDENT) : Format.getCompactFormat();
        format.setOmitDeclaration(!showXmlDeclaration);
        format.setEncoding(UTF_8_ENCODING);
        xmlWriter.setFormat(format);

        if (showXmlDeclaration) {
            return xmlWriter.outputString(convertToXmlDocument(json, rootTagName));
        }

        List<Element> elements = convertToXmlElements(json, rootTagName);
        StringBuilder result = new StringBuilder();
        for (Element element : elements) {
            result.append(xmlWriter.outputString(element));
            result.append(NEW_LINE);
        }
        result.delete(result.length() - NEW_LINE.length(), result.length());
        return result.toString();
    }

    /**
     * Convert the JSON string to a list of XML elements.
     *
     * @param json JSON object or JSON array
     * @return a list of XML elements
     */
    private List<Element> convertToXmlElements(String json, String rootTagName) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json);
        if (jsonElement.isJsonArray()) {
            if (StringUtils.isEmpty(rootTagName)) {
                // if it's JSON array and rootTagName is empty it returns a list of elements with the name jsonArrayItemName
                return convertToXmlElements(jsonElement.getAsJsonArray(), jsonArrayItemName);
            }

            Element root = convertToXmlElements(jsonElement.getAsJsonArray(), rootTagName, jsonArrayItemName);
            return Collections.singletonList(root);
        } else {
            if (StringUtils.isEmpty(rootTagName)) {
                // if it's JSON object and rootTagName is empty it return a list of contains elements
                return convertToXmlElement(jsonElement.getAsJsonObject());
            }

            Element root = convertToXmlElement(jsonElement.getAsJsonObject(), rootTagName);
            return Collections.singletonList(root);
        }
    }

    /**
     * Convert the JSON string to a XML document.
     * If json is a JSON array it will create a root node with the name {@code rootJsonArrayName}
     *
     * @param json JSON string
     * @return the XML document
     */
    private Document convertToXmlDocument(String json, String rootTagName) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json);
        Element root;
        if (jsonElement.isJsonArray()) {
            if (StringUtils.isEmpty(rootTagName)) {
                // we don't know the root tag name
                throw new IllegalArgumentException(PropsLoader.EXCEPTIONS.getProperty(ROOT_TAG_NAME_IS_EMPTY));
            }
            root = convertToXmlElements(jsonElement.getAsJsonArray(), rootTagName, jsonArrayItemName);
        } else {
            if (StringUtils.isEmpty(rootTagName)) {
                List<Element> elements = convertToXmlElement(jsonElement.getAsJsonObject());
                if (elements.size() != 1) {
                    // the JSON object must have only one element
                    throw new IllegalArgumentException(PropsLoader.EXCEPTIONS.getProperty(ROOT_TAG_NAME_IS_EMPTY));
                }

                root = elements.get(0);
            } else {
                root = convertToXmlElement(jsonElement.getAsJsonObject(), rootTagName);
            }
        }

        Document document = new Document();
        document.setRootElement(root);
        return document;
    }

    /* Utility functions */

    /**
     * Convert a JSON array to a container XML element with the name arrayName.
     *
     * @param jsonArray JSON array
     * @param arrayName the array tag name
     * @param itemName  the item tag name
     * @return the XML element
     */
    private Element convertToXmlElements(JsonArray jsonArray, String arrayName, String itemName) {
        Element result = createElement(arrayName);
        for (JsonElement itemJson : jsonArray) {
            //if it's null we don't care
            if (itemJson.isJsonObject()) {
                result.addContent(convertToXmlElement(itemJson.getAsJsonObject(), itemName));
            } else if (itemJson.isJsonPrimitive()) {
                String itemValue = getJsonPrimitiveValue(itemJson.getAsJsonPrimitive());
                result.addContent(createElement(itemName).setText(itemValue));
            } else if (itemJson.isJsonArray()) {
                Element arrayElement = createElement(jsonArrayItemName);
                List<Element> elements = convertToXmlElements(itemJson.getAsJsonArray(), jsonArrayItemName);
                for (Element element : elements) {
                    arrayElement.addContent(element);
                }
                result.addContent(arrayElement);
            }
        }
        return result;
    }

    /**
     * Explode the JSON array to its elements and convert them to XML elements.
     *
     * @param jsonArray JSON array
     * @param itemName  the item tag name
     * @return the list of XML elements
     */
    private List<Element> convertToXmlElements(JsonArray jsonArray, String itemName) {
        List<Element> result = new ArrayList<>();
        for (JsonElement itemJson : jsonArray) {
            //if it's null we don't care
            if (itemJson.isJsonObject()) {
                result.add(convertToXmlElement(itemJson.getAsJsonObject(), itemName));
            } else if (itemJson.isJsonPrimitive()) {
                String itemValue = getJsonPrimitiveValue(itemJson.getAsJsonPrimitive());
                result.add(createElement(itemName).setText(itemValue));
            } else if (itemJson.isJsonArray()) {
                Element arrayElement = createElement(jsonArrayItemName);
                List<Element> elements = convertToXmlElements(itemJson.getAsJsonArray(), jsonArrayItemName);
                for (Element element : elements) {
                    arrayElement.addContent(element);
                }
                result.add(arrayElement);
            }
        }
        return result;
    }

    /**
     * Explode the JSON object to its components and convert them to XML elements.
     *
     * @param jsonObject JSON object
     * @return the list of XML elements
     */
    private List<Element> convertToXmlElement(JsonObject jsonObject) {
        List<Element> result = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            String childTagName = entry.getKey();
            JsonElement childJson = entry.getValue();
            //if it's null we don't care
            if (childJson.isJsonPrimitive()) {
                String childValue = getJsonPrimitiveValue(childJson.getAsJsonPrimitive());
                result.add(createElement(childTagName).setText(childValue));
            } else if (childJson.isJsonObject()) {
                result.add(convertToXmlElement(childJson.getAsJsonObject(), childTagName));
            } else if (childJson.isJsonArray()) {
                String itemName;
                if (jsonArrayItemNames.containsKey(childTagName)) {
                    itemName = jsonArrayItemNames.get(childTagName);
                } else {
                    itemName = jsonArrayItemName;
                }
                Element container = createElement(childTagName);
                List<Element> elements = convertToXmlElements(childJson.getAsJsonArray(), itemName);
                for (Element element : elements) {
                    container.addContent(element);
                }
                result.add(container);
            }
        }
        return result;
    }

    /**
     * Convert the JSON object to a single XML element with the given tagName.
     *
     * @param jsonObject JSON object
     * @param tagName    XML element name
     * @return the XML element
     */
    private Element convertToXmlElement(JsonObject jsonObject, String tagName) {
        Element result = createElement(tagName);
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            String childTagName = entry.getKey();
            JsonElement childJson = entry.getValue();
            //this child element is an XML attribute
            if (childTagName.startsWith(JSON_ATTRIBUTE_PREFIX)) {
                //if attribute value is not a primitive value we don't add it to xml element
                if (childJson.isJsonPrimitive()) {
                    //remove @ char from the attribute name
                    childTagName = childTagName.substring(JSON_ATTRIBUTE_PREFIX.length());
                    String attributeValue = getJsonPrimitiveValue(childJson.getAsJsonPrimitive());
                    result.setAttribute(new Attribute(childTagName, attributeValue));
                }
            } else {
                //if it's null we don't care
                if (childJson.isJsonPrimitive()) {
                    String childValue = getJsonPrimitiveValue(childJson.getAsJsonPrimitive());
                    result.addContent(createElement(childTagName).setText(childValue));
                } else if (childJson.isJsonObject()) {
                    result.addContent(convertToXmlElement(childJson.getAsJsonObject(), childTagName));
                } else if (childJson.isJsonArray()) {
                    String itemName = jsonArrayItemNames.containsKey(childTagName)?
                            jsonArrayItemNames.get(childTagName) : jsonArrayItemName;

                    Element container = createElement(childTagName);
                    List<Element> elements = convertToXmlElements(childJson.getAsJsonArray(), itemName);
                    for (Element element : elements) {
                        container.addContent(element);
                    }
                    result.addContent(container);
                }
            }
        }
        return result;
    }

    /**
     * Create an XML element with the given tag name. It will add all the namespaces declared.
     *
     * @param tagName the tag name (can contain namespace prefix)
     * @return the XML element
     */
    private Element createElement(String tagName) {
        String[] v = tagName.split(NAMESPACE_DELIMITER);
        Element result;
        if (v.length == 1) {
            result = new Element(tagName);
        } else {
            result = new Element(v[1], namespaces.get(v[0]));
        }
        for (Namespace namespace : namespaces.values()) {
            result.addNamespaceDeclaration(namespace);
        }
        return result;
    }

    /**
     * @param jsonPrimitive the JSON primitive (String, Number, Boolean)
     * @return the string value of the JSON primitive
     */
    private String getJsonPrimitiveValue(JsonPrimitive jsonPrimitive) {
        if (jsonPrimitive.isNumber()) {
            return jsonPrimitive.getAsNumber().toString();
        } else if (jsonPrimitive.isBoolean()) {
            return Boolean.toString(jsonPrimitive.getAsBoolean());
        }
        return jsonPrimitive.getAsString();
    }

    /**
     * Set the map with array items name.
     *
     * @param jsonArrayItemNames the map with the names of array items based on the array name
     */
    public void setJsonArrayItemNames(Map<String, String> jsonArrayItemNames) {
        this.jsonArrayItemNames = jsonArrayItemNames;
    }

    public void setNamespaces(Map<String, String> namespacesString) {
        for (Map.Entry<String, String> entry : namespacesString.entrySet()) {
            namespaces.put(entry.getValue(), Namespace.getNamespace(entry.getValue(), entry.getKey()));
        }
    }

}
