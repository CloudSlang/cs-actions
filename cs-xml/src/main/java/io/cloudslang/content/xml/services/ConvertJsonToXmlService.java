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
    private Map<String, String> jsonArrayItemNames;
    private String jsonArrayItemName;
    private Map<String, Namespace> namespaces;


    public ConvertJsonToXmlService() {
        namespaces = new HashMap<>();
        jsonArrayItemNames = new HashMap<>();
    }

    public String convertToXmlString(String json, Boolean prettyPrint, Boolean showXmlDeclaration, String rootTagName) {
        if (StringUtils.isBlank(json)) {
            return EMPTY_STRING;
        }
        XMLOutputter xmlWriter = new XMLOutputter();
        xmlWriter.setFormat(getFormat(prettyPrint, showXmlDeclaration));
        if (showXmlDeclaration) {
            return xmlWriter.outputString(convertJsonStringToXmlDocument(json, rootTagName));
        }
        return getXmlFromElements(convertJsonStringToXmlElements(json, rootTagName), xmlWriter);
    }

    private String getXmlFromElements(List<Element> elements, XMLOutputter xmlWriter) {
        StringBuilder result = new StringBuilder();
        for (Element element : elements) {
            result.append(xmlWriter.outputString(element)).append(NEW_LINE);
        }
        result.delete(result.length() - NEW_LINE.length(), result.length());
        return result.toString();
    }

    private Format getFormat(Boolean prettyPrint, Boolean showXmlDeclaration) {
        Format format = prettyPrint ? Format.getPrettyFormat().setIndent(INDENT) : Format.getCompactFormat();
        return format.setOmitDeclaration(!showXmlDeclaration)
                .setEncoding(UTF_8_ENCODING)
                .setLineSeparator(NEW_LINE);
    }

    private List<Element> convertJsonStringToXmlElements(String json, String rootTagName) {
        JsonElement jsonElement = new JsonParser().parse(json);
        if (StringUtils.isEmpty(rootTagName)) {
            return convertToXmlElementsJsonElement(jsonElement);
        }
        Element root = convertToXmlElementJsonElementWithRootTag(jsonElement, rootTagName);
        return Collections.singletonList(root);
    }

    private Document convertJsonStringToXmlDocument(String json, String rootTagName) {
        JsonElement jsonElement = new JsonParser().parse(json);
        Document document = new Document();
        return document.setRootElement(addRootTagJsonElement(rootTagName, jsonElement));

    }

    private Element addRootTagJsonElement(String rootTagName, JsonElement jsonElement) {
        if (StringUtils.isEmpty(rootTagName)) {
            if (jsonElement.isJsonArray()) {
                // we don't know the root tag name
                throw new IllegalArgumentException(ROOT_TAG_NAME_IS_MISSING);
            } else {
                List<Element> elements = convertToXmlElementsJsonObject(jsonElement.getAsJsonObject());
                if (elements.size() != 1) {
                    // the JSON object must have only one element
                    throw new IllegalArgumentException(ONLY_ONE_ROOT_ELEMENT);
                }
                return elements.get(0);
            }
        }
        return convertToXmlElementJsonElementWithRootTag(jsonElement, rootTagName);
    }

    private Element convertJsonArrayToXmlElement(JsonArray jsonArray, String arrayName) {
        Element result = createElement(arrayName);
        for (JsonElement itemJson : jsonArray) {
            Element elementToAdd = getXmlElementFromJsonElement(itemJson, jsonArrayItemName);
            if (elementToAdd != null) {
                result.addContent(elementToAdd);
            }
        }
        return result;
    }

    private List<Element> convertToXmlElementsJsonArray(JsonArray jsonArray) {
        return convertToXmlElementsJsonArray(jsonArray, jsonArrayItemName);
    }

    private List<Element> convertToXmlElementsJsonArray(JsonArray jsonArray, String itemName) {
        List<Element> result = new ArrayList<>();
        for (JsonElement itemJson : jsonArray) {
            Element xmlItem = getXmlElementFromJsonElement(itemJson, itemName);
            if (xmlItem != null) {
                result.add(xmlItem);
            }
        }
        return result;
    }


    private List<Element> convertToXmlElementsJsonElement(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            return convertToXmlElementsJsonArray(jsonElement.getAsJsonArray());
        }
        return convertToXmlElementsJsonObject(jsonElement.getAsJsonObject());
    }

    private Element convertToXmlElementJsonElementWithRootTag(JsonElement jsonElement, String rootTagName) {
        if (jsonElement.isJsonArray()) {
            return convertJsonArrayToXmlElement(jsonElement.getAsJsonArray(), rootTagName);
        }
        return convertJsonObjectToXmlElement(jsonElement.getAsJsonObject(), rootTagName);
    }

    private List<Element> convertToXmlElementsJsonObject(JsonObject jsonObject) {
        List<Element> result = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String childTagName = entry.getKey();
            JsonElement childJson = entry.getValue();
            //if it's null we don't care
            Element elementToAdd = getXmlElementFromJsonElement(childJson, childTagName);
            if (elementToAdd != null) {
                result.add(elementToAdd);
            }
        }
        return result;
    }

    private Element getXmlElementFromJsonElement(JsonElement childJson, String childTagName) {
        if (childJson.isJsonPrimitive()) {
            String childValue = getJsonPrimitiveValue(childJson.getAsJsonPrimitive());
            return createElement(childTagName).setText(childValue);
        } else if (childJson.isJsonObject()) {
            return convertJsonObjectToXmlElement(childJson.getAsJsonObject(), childTagName);
        } else if (childJson.isJsonArray()) {
            String itemName = jsonArrayItemNames.containsKey(childTagName) ?
                    jsonArrayItemNames.get(childTagName) : jsonArrayItemName;
            Element container = createElement(childTagName);
            List<Element> elements = convertToXmlElementsJsonArray(childJson.getAsJsonArray(), itemName);
            for (Element element : elements) {
                container.addContent(element);
            }
            return container;
        }
        return null;
    }

    private Element convertJsonObjectToXmlElement(JsonObject jsonObject, String tagName) {
        Element result = createElement(tagName);
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
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
                Element elementToAdd = getXmlElementFromJsonElement(childJson, childTagName);
                if (elementToAdd != null) {
                    result.addContent(elementToAdd);
                }
            }
        }
        return result;
    }

    private Element createElement(String tagName) {
        String[] tagNames = tagName.split(NAMESPACE_DELIMITER);
        Element result = tagNames.length == 1 ? new Element(tagName) : new Element(tagNames[1], namespaces.get(tagNames[0]));
        for (Namespace namespace : namespaces.values()) {
            result.addNamespaceDeclaration(namespace);
        }
        return result;
    }

    private String getJsonPrimitiveValue(JsonPrimitive jsonPrimitive) {
        if (jsonPrimitive.isNumber()) {
            return jsonPrimitive.getAsNumber().toString();
        } else if (jsonPrimitive.isBoolean()) {
            return Boolean.toString(jsonPrimitive.getAsBoolean());
        }
        return jsonPrimitive.getAsString();
    }

    public void setJsonArrayItemNames(Map<String, String> jsonArrayItemNames) {
        this.jsonArrayItemNames = jsonArrayItemNames;
    }

    public void setJsonArrayItemName(String jsonArrayItemName) {
        this.jsonArrayItemName = jsonArrayItemName;
    }

    public void setNamespaces(Map<String, String> namespacesString) {
        for (Map.Entry<String, String> entry : namespacesString.entrySet()) {
            namespaces.put(entry.getValue(), Namespace.getNamespace(entry.getValue(), entry.getKey()));
        }
    }
}
