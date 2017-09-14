/***********************************************************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
***********************************************************************************************************************/
package io.cloudslang.content.xml.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.cloudslang.content.xml.entities.inputs.ConvertJsonToXmlInputs;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.xml.utils.Constants.INDENT;
import static io.cloudslang.content.xml.utils.Constants.JSON_ATTRIBUTE_PREFIX;
import static io.cloudslang.content.xml.utils.Constants.NAMESPACE_DELIMITER;
import static io.cloudslang.content.xml.utils.Constants.NEW_LINE;
import static io.cloudslang.content.xml.utils.Constants.ONLY_ONE_ROOT_ELEMENT;
import static io.cloudslang.content.xml.utils.Constants.ROOT_TAG_NAME_IS_MISSING;
import static io.cloudslang.content.xml.utils.Constants.UTF_8_ENCODING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertJsonToXmlService {
    private Map<String, String> jsonArrayItemNames;
    private String jsonArrayItemName;
    private final Map<String, Namespace> namespaces;


    public ConvertJsonToXmlService() {
        namespaces = new HashMap<>();
        jsonArrayItemNames = new HashMap<>();
    }

    public String convertToXmlString(final ConvertJsonToXmlInputs inputs) {
        if (isBlank(inputs.getJson())) {
            return EMPTY;
        }
        final XMLOutputter xmlWriter = new XMLOutputter();
        xmlWriter.setFormat(getFormat(inputs.getPrettyPrint(), inputs.getShowXmlDeclaration()));
        if (inputs.getShowXmlDeclaration()) {
            return xmlWriter.outputString(convertJsonStringToXmlDocument(inputs.getJson(), inputs.getRootTagName()));
        }
        return getXmlFromElements(convertJsonStringToXmlElements(inputs.getJson(), inputs.getRootTagName()), xmlWriter);
    }

    private String getXmlFromElements(final List<Element> elements, final XMLOutputter xmlWriter) {
        final StringBuilder result = new StringBuilder();
        for (final Element element : elements) {
            result.append(xmlWriter.outputString(element)).append(NEW_LINE);
        }
        result.delete(result.length() - NEW_LINE.length(), result.length());
        return result.toString();
    }

    private Format getFormat(final boolean prettyPrint, final boolean showXmlDeclaration) {
        final Format format = prettyPrint ? Format.getPrettyFormat().setIndent(INDENT) : Format.getCompactFormat();
        return format.setOmitDeclaration(!showXmlDeclaration)
                .setEncoding(UTF_8_ENCODING)
                .setLineSeparator(NEW_LINE);
    }

    private List<Element> convertJsonStringToXmlElements(final String json, final String rootTagName) {
        final JsonElement jsonElement = new JsonParser().parse(json);
        if (isEmpty(rootTagName)) {
            return convertToXmlElementsJsonElement(jsonElement);
        }
        final Element root = convertToXmlElementJsonElementWithRootTag(jsonElement, rootTagName);
        return Collections.singletonList(root);
    }

    private Document convertJsonStringToXmlDocument(final String json, final String rootTagName) {
        final JsonElement jsonElement = new JsonParser().parse(json);
        final Document document = new Document();
        return document.setRootElement(addRootTagJsonElement(rootTagName, jsonElement));

    }

    private Element addRootTagJsonElement(final String rootTagName, final JsonElement jsonElement) {
        if (isEmpty(rootTagName)) {
            if (jsonElement.isJsonArray()) {
                // we don't know the root tag name
                throw new IllegalArgumentException(ROOT_TAG_NAME_IS_MISSING);
            } else {
                final List<Element> elements = convertToXmlElementsJsonObject(jsonElement.getAsJsonObject());
                if (elements.size() != 1) {
                    // the JSON object must have only one element
                    throw new IllegalArgumentException(ONLY_ONE_ROOT_ELEMENT);
                }
                return elements.get(0);
            }
        }
        return convertToXmlElementJsonElementWithRootTag(jsonElement, rootTagName);
    }

    private Element convertJsonArrayToXmlElement(final JsonArray jsonArray, final String arrayName) {
        final Element result = createElement(arrayName);
        for (JsonElement itemJson : jsonArray) {
            final Element elementToAdd = getXmlElementFromJsonElement(itemJson, jsonArrayItemName);
            if (elementToAdd != null) {
                result.addContent(elementToAdd);
            }
        }
        return result;
    }

    private List<Element> convertToXmlElementsJsonArray(final JsonArray jsonArray) {
        return convertToXmlElementsJsonArray(jsonArray, jsonArrayItemName);
    }

    private List<Element> convertToXmlElementsJsonArray(final JsonArray jsonArray, final String itemName) {
        final List<Element> result = new ArrayList<>();
        for (JsonElement itemJson : jsonArray) {
            final Element xmlItem = getXmlElementFromJsonElement(itemJson, itemName);
            if (xmlItem != null) {
                result.add(xmlItem);
            }
        }
        return result;
    }


    private List<Element> convertToXmlElementsJsonElement(final JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            return convertToXmlElementsJsonArray(jsonElement.getAsJsonArray());
        }
        return convertToXmlElementsJsonObject(jsonElement.getAsJsonObject());
    }

    private Element convertToXmlElementJsonElementWithRootTag(final JsonElement jsonElement, final String rootTagName) {
        if (jsonElement.isJsonArray()) {
            return convertJsonArrayToXmlElement(jsonElement.getAsJsonArray(), rootTagName);
        }
        return convertJsonObjectToXmlElement(jsonElement.getAsJsonObject(), rootTagName);
    }

    private List<Element> convertToXmlElementsJsonObject(final JsonObject jsonObject) {
        final List<Element> result = new ArrayList<>();
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            final String childTagName = entry.getKey();
            final JsonElement childJson = entry.getValue();
            //if it's null we don't care
            final Element elementToAdd = getXmlElementFromJsonElement(childJson, childTagName);
            if (elementToAdd != null) {
                result.add(elementToAdd);
            }
        }
        return result;
    }

    private Element getXmlElementFromJsonElement(final JsonElement childJson, final String childTagName) {
        if (childJson.isJsonPrimitive()) {
            final String childValue = getJsonPrimitiveValue(childJson.getAsJsonPrimitive());
            return createElement(childTagName).setText(childValue);
        } else if (childJson.isJsonObject()) {
            return convertJsonObjectToXmlElement(childJson.getAsJsonObject(), childTagName);
        } else if (childJson.isJsonArray()) {
            final String itemName = jsonArrayItemNames.containsKey(childTagName) ?
                    jsonArrayItemNames.get(childTagName) : jsonArrayItemName;
            final Element container = createElement(childTagName);
            final List<Element> elements = convertToXmlElementsJsonArray(childJson.getAsJsonArray(), itemName);
            for (final Element element : elements) {
                container.addContent(element);
            }
            return container;
        }
        return null;
    }

    private Element convertJsonObjectToXmlElement(final JsonObject jsonObject, final String tagName) {
        final Element result = createElement(tagName);
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String childTagName = entry.getKey();
            final JsonElement childJson = entry.getValue();
            //this child element is an XML attribute
            if (childTagName.startsWith(JSON_ATTRIBUTE_PREFIX)) {
                //if attribute value is not a primitive value we don't add it to xml element
                if (childJson.isJsonPrimitive()) {
                    //remove @ char from the attribute name
                    childTagName = childTagName.substring(JSON_ATTRIBUTE_PREFIX.length());
                    final String attributeValue = getJsonPrimitiveValue(childJson.getAsJsonPrimitive());
                    result.setAttribute(new Attribute(childTagName, attributeValue));
                }
            } else {
                //if it's null we don't care
                final Element elementToAdd = getXmlElementFromJsonElement(childJson, childTagName);
                if (elementToAdd != null) {
                    result.addContent(elementToAdd);
                }
            }
        }
        return result;
    }

    private Element createElement(final String tagName) {
        final String[] tagNames = tagName.split(NAMESPACE_DELIMITER);
        final Element result = tagNames.length == 1 ? new Element(tagName) : new Element(tagNames[1], namespaces.get(tagNames[0]));
        for (Namespace namespace : namespaces.values()) {
            result.addNamespaceDeclaration(namespace);
        }
        return result;
    }

    private String getJsonPrimitiveValue(final JsonPrimitive jsonPrimitive) {
        if (jsonPrimitive.isNumber()) {
            return jsonPrimitive.getAsNumber().toString();
        } else if (jsonPrimitive.isBoolean()) {
            return Boolean.toString(jsonPrimitive.getAsBoolean());
        }
        return jsonPrimitive.getAsString();
    }

    public void setJsonArrayItemNames(final Map<String, String> jsonArrayItemNames) {
        this.jsonArrayItemNames = jsonArrayItemNames;
    }

    public void setJsonArrayItemName(final String jsonArrayItemName) {
        this.jsonArrayItemName = jsonArrayItemName;
    }

    public void setNamespaces(final Map<String, String> namespacesString) {
        for (final Map.Entry<String, String> entry : namespacesString.entrySet()) {
            namespaces.put(entry.getValue(), Namespace.getNamespace(entry.getValue(), entry.getKey()));
        }
    }
}
