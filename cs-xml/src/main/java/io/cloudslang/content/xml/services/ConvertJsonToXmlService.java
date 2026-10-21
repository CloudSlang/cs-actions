/*
 * Copyright 2022-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.xml.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.cloudslang.content.xml.entities.inputs.ConvertJsonToXmlInputs;
import io.cloudslang.content.xml.utils.DocumentUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.xml.utils.Constants.JSON_ATTRIBUTE_PREFIX;
import static io.cloudslang.content.xml.utils.Constants.NAMESPACE_DELIMITER;
import static io.cloudslang.content.xml.utils.Constants.NEW_LINE;
import static io.cloudslang.content.xml.utils.Constants.ONLY_ONE_ROOT_ELEMENT;
import static io.cloudslang.content.xml.utils.Constants.ROOT_TAG_NAME_IS_MISSING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertJsonToXmlService {
    private Map<String, String> jsonArrayItemNames;
    private String jsonArrayItemName;
    private final Map<String, String> namespaces;
    private Document document;
    private boolean prettyPrint;


    public ConvertJsonToXmlService() {
        namespaces = new HashMap<>();
        jsonArrayItemNames = new HashMap<>();
    }

    public String convertToXmlString(final ConvertJsonToXmlInputs inputs) {
        if (isBlank(inputs.getJson())) {
            return EMPTY;
        }
        document = createDocument();
        prettyPrint = inputs.getPrettyPrint();
        if (inputs.getShowXmlDeclaration()) {
            document.appendChild(addRootTagJsonElement(inputs.getRootTagName(), new JsonParser().parse(inputs.getJson())));
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE
                    + serialize(document.getDocumentElement()) + NEW_LINE;
        }
        return getXmlFromElements(convertJsonStringToXmlElements(inputs.getJson(), inputs.getRootTagName()));
    }

    private String getXmlFromElements(final List<Element> elements) {
        final List<String> result = new ArrayList<>(elements.size());
        for (final Element element : elements) {
            result.add(serialize(element));
        }
        return String.join(NEW_LINE, result);
    }

    private String serialize(final Element element) {
        try {
            return DocumentUtils.elementToString(element, prettyPrint);
        } catch (TransformerException e) {
            throw new IllegalStateException("Unable to serialize converted XML", e);
        }
    }

    private Document createDocument() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Unable to initialize XML document builder", e);
        }
    }

    private List<Element> convertJsonStringToXmlElements(final String json, final String rootTagName) {
        final JsonElement jsonElement = new JsonParser().parse(json);
        if (isEmpty(rootTagName)) {
            return convertToXmlElementsJsonElement(jsonElement);
        }
        final Element root = convertToXmlElementJsonElementWithRootTag(jsonElement, rootTagName);
        return Collections.singletonList(root);
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
                result.appendChild(elementToAdd);
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
            validateXmlCharacters(childValue);
            final Element element = createElement(childTagName);
            final String text = prettyPrint ? childValue.trim() : childValue.trim().replaceAll("[ \\t\\r\\n]+", " ");
            element.setTextContent(text);
            return element;
        } else if (childJson.isJsonObject()) {
            return convertJsonObjectToXmlElement(childJson.getAsJsonObject(), childTagName);
        } else if (childJson.isJsonArray()) {
            final String itemName = jsonArrayItemNames.containsKey(childTagName) ?
                    jsonArrayItemNames.get(childTagName) : jsonArrayItemName;
            final Element container = createElement(childTagName);
            final List<Element> elements = convertToXmlElementsJsonArray(childJson.getAsJsonArray(), itemName);
            for (final Element element : elements) {
                container.appendChild(element);
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
                    validateXmlCharacters(attributeValue);
                    result.setAttributeNode(document.createAttributeNS(null, childTagName));
                    result.setAttribute(childTagName, attributeValue);
                }
            } else {
                //if it's null we don't care
                final Element elementToAdd = getXmlElementFromJsonElement(childJson, childTagName);
                if (elementToAdd != null) {
                    result.appendChild(elementToAdd);
                }
            }
        }
        return result;
    }

    private Element createElement(final String tagName) {
        final String[] tagNames = tagName.split(NAMESPACE_DELIMITER);
        final String namespace = tagNames.length == 1 ? null : namespaces.get(tagNames[0]);
        final String name = tagNames.length == 1 ? tagName
                : namespace == null || namespace.isEmpty() ? tagNames[1] : tagNames[0] + NAMESPACE_DELIMITER + tagNames[1];
        final Element result = document.createElementNS(namespace, name);
        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
            final String attribute = entry.getKey().isEmpty() ? XMLConstants.XMLNS_ATTRIBUTE
                    : XMLConstants.XMLNS_ATTRIBUTE + NAMESPACE_DELIMITER + entry.getKey();
            result.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, attribute, entry.getValue());
        }
        return result;
    }

    private void validateXmlCharacters(final String value) {
        if (!value.codePoints().allMatch(character -> character == 0x9 || character == 0xA || character == 0xD
                || character >= 0x20 && character <= 0xD7FF
                || character >= 0xE000 && character <= 0xFFFD
                || character >= 0x10000 && character <= 0x10FFFF)) {
            throw new IllegalArgumentException("Value contains characters that are not allowed in XML 1.0");
        }
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
        final Document namespaceValidator = createDocument();
        for (final Map.Entry<String, String> entry : namespacesString.entrySet()) {
            final String prefix = entry.getValue();
            final String uri = entry.getKey();
            if (!prefix.isEmpty() && uri.isEmpty()
                    || XMLConstants.XMLNS_ATTRIBUTE.equals(prefix)
                    || XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(uri)
                    || XMLConstants.XML_NS_PREFIX.equals(prefix) != XMLConstants.XML_NS_URI.equals(uri)) {
                throw new IllegalArgumentException("Invalid XML namespace binding: " + prefix + "=" + uri);
            }
            validateXmlCharacters(uri);
            if (!prefix.isEmpty()) {
                namespaceValidator.createElementNS(uri, prefix + NAMESPACE_DELIMITER + "element");
            }
            namespaces.put(prefix, uri);
        }
    }
}
