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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.cloudslang.content.xml.entities.inputs.ConvertXmlToJsonInputs;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.xml.utils.Constants.Defaults;
import static io.cloudslang.content.xml.utils.Constants.JSON_ATTRIBUTE_PREFIX;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertXmlToJsonService {
    private final StringBuilder namespacesPrefixes;
    private final StringBuilder namespacesUris;
    private final Map<Element, List<Attr>> attributes = new IdentityHashMap<>();
    private final Map<Element, Map<String, String>> namespaceDeclarations = new IdentityHashMap<>();

    public ConvertXmlToJsonService() {
        namespacesPrefixes = new StringBuilder();
        namespacesUris = new StringBuilder();
    }

    public String convertToJsonString(final ConvertXmlToJsonInputs inputs) throws ParserConfigurationException, IOException, SAXException {
        if (StringUtils.isBlank(inputs.getXml())) {
            return EMPTY;
        }
        attributes.clear();
        namespaceDeclarations.clear();
        final InputSource inputSource = new InputSource(new StringReader(inputs.getXml()));
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlUtils.setFeatures(factory, inputs.getParsingFeatures());
        final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        try {
            factory.newSAXParser().parse(inputSource, new ConversionHandler(document));
        } catch (SAXParseException e) {
            throw new SAXException("Error on line " + e.getLineNumber() + ": " + e.getMessage(), e);
        }
        final Element root = document.getDocumentElement();
        final List<Element> xmlElements = Collections.singletonList(root);
        if (getChildren(root).isEmpty() && !inputs.getIncludeAttributes()) {
            return prettyPrint(addJsonObjectsAndPrimitives(new JsonObject(), xmlElements, inputs.getIncludeAttributes(), inputs.getTextElementsName()),
                    inputs.getPrettyPrint());
        }
        final JsonObject jsonObjectValue = convertXmlElementsToJsonObject(xmlElements, inputs.getIncludeAttributes(), inputs.getTextElementsName());
        final JsonObject jsonObject = getJsonObjectWithRootElement(root, jsonObjectValue, inputs.getIncludeRootElement());
        return prettyPrint(jsonObject, inputs.getPrettyPrint());
    }


    private JsonObject getJsonObjectWithRootElement(final Element rootElement, final JsonObject jsonObject, final boolean addRootElement) {
        if (addRootElement) {
            final JsonObject rootJson = new JsonObject();
            rootJson.add(rootElement.getLocalName(), jsonObject);
            return rootJson;
        }
        return jsonObject;
    }

    private String prettyPrint(final JsonObject rootJson, final boolean prettyPrint) {
        if (prettyPrint) {
            final Gson gson = new GsonBuilder().setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            return gson.toJson(rootJson);
        }
        return rootJson.toString();
    }


    private JsonObject addJsonObjectsAndPrimitives(final JsonObject jsonObject, final List<Element> elements, final boolean includeAttributes, final String textPropName) {
        for (final Element element : elements) {
            final JsonElement jsonElement = isPrimitiveElement(element) ?
                    new JsonPrimitive(element.getTextContent()) : convertXmlElementsToJsonObject(Collections.singletonList(element), includeAttributes, textPropName);
            jsonObject.add(getElementFullName(element), jsonElement);
        }
        return jsonObject;
    }

    private JsonObject convertXmlElementsToJsonObject(final List<Element> xmlElements, final boolean includeAttributes, final String textPropName) {
        JsonObject result = new JsonObject();
        for (final Element xmlElement : xmlElements) {
            result = addXmlElementToJsonObject(result, xmlElement, includeAttributes, textPropName);
        }
        return result;
    }

    private JsonArray convertXmlElementsToJsonArray(final List<Element> xmlElements, final boolean includeAttributes, final String textPropName) {
        final JsonArray result = new JsonArray();
        for (final Element xmlElement : xmlElements) {
            result.add(addXmlElementToJsonObject(new JsonObject(), xmlElement, includeAttributes, textPropName));
        }
        return result;
    }

    private JsonObject addXmlElementToJsonObject(JsonObject jsonObject, final Element xmlElement, final boolean includeAttributes, final String textPropName) {
        addNamespaces(xmlElement);
        if (includeAttributes) {
            jsonObject = addAttributesToJsonObject(jsonObject, attributes.get(xmlElement));
        }
        List<Element> children = getChildren(xmlElement);
        jsonObject = searchAndAddArrayElementsToJsonObject(jsonObject, children, includeAttributes, textPropName);
        children = eliminateArrayElements(children);
        jsonObject = addJsonObjectsAndPrimitives(jsonObject, children, includeAttributes, textPropName);
        jsonObject = addTextProp(jsonObject, getText(xmlElement), textPropName);
        return jsonObject;
    }

    private JsonObject searchAndAddArrayElementsToJsonObject(final JsonObject jsonObject, final List<Element> elements, final boolean includeAttributes, final String textPropName) {
        final List<String> arrayElementsNames = getListOfArrayElementNames(elements);
        for (final String arrayName : arrayElementsNames) {
            final List<Element> arrayElements = getElementsByName(arrayName, elements);
            jsonObject.add(arrayName, convertXmlElementsToJsonArray(arrayElements, includeAttributes, textPropName));
        }
        return jsonObject;
    }

    private List<Element> eliminateArrayElements(List<Element> elements) {
        final List<String> arrayElementsNames = getListOfArrayElementNames(elements);
        for (final String arrayName : arrayElementsNames) {
            elements = eliminateElementsWithName(arrayName, elements);
        }
        return elements;
    }

    private JsonObject addTextProp(final JsonObject jsonObject, final String text, final String textPropName) {
        if (isNotEmpty(text) && text.matches(".*[a-zA-Z0-9].*")) {
            jsonObject.addProperty(textPropName, text);
        }
        return jsonObject;
    }

    private void addNamespaces(final Element element) {
        final String elementPrefix = StringUtils.defaultString(element.getPrefix());
        for (final Map.Entry<String, String> namespace : namespaceDeclarations.get(element).entrySet()) {
            if (namespace.getKey().equals(elementPrefix)) {
                continue;
            }
            if (namespacesUris.length() > 0) {
                namespacesPrefixes.append(Defaults.DELIMITER);
                namespacesUris.append(Defaults.DELIMITER);
            }
            namespacesPrefixes.append(namespace.getKey());
            namespacesUris.append(namespace.getValue());
        }
    }

    private JsonObject addAttributesToJsonObject(final JsonObject jsonObject, final List<Attr> attributes) {
        for (final Attr attribute : attributes) {
            jsonObject.addProperty(JSON_ATTRIBUTE_PREFIX + attribute.getLocalName(), attribute.getValue());
        }
        return jsonObject;
    }

    @NotNull
    private String getElementFullName(final Element element) {
        return element.getTagName();
    }

    private List<String> getElementsFullName(final List<Element> elements) {
        final List<String> names = new ArrayList<>();
        for (final Element element : elements) {
            names.add(getElementFullName(element));
        }
        return names;
    }


    private boolean isPrimitiveElement(final Element element) {
        return getChildren(element).isEmpty() && attributes.get(element).isEmpty();
    }

    private List<Element> getChildren(final Element element) {
        final List<Element> result = new ArrayList<>();
        final NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                result.add((Element) nodes.item(i));
            }
        }
        return result;
    }

    private String getText(final Element element) {
        final StringBuilder result = new StringBuilder();
        final NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            final Node node = nodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
                result.append(node.getNodeValue());
            }
        }
        return result.toString();
    }

    private class ConversionHandler extends DefaultHandler {
        private final Document document;
        private final Map<String, String> pendingNamespaces = new LinkedHashMap<>();
        private Element current;

        private ConversionHandler(Document document) {
            this.document = document;
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) {
            pendingNamespaces.put(prefix, uri);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes saxAttributes) {
            final Element element = document.createElementNS(uri, qName);
            // SAX preserves source ordering, unlike DOM's NamedNodeMap.
            final List<Attr> orderedAttributes = new ArrayList<>();
            for (int i = 0; i < saxAttributes.getLength(); i++) {
                final String attributeName = saxAttributes.getQName(i);
                if (!XMLConstants.XMLNS_ATTRIBUTE.equals(attributeName)
                        && !attributeName.startsWith(XMLConstants.XMLNS_ATTRIBUTE + ":")
                        && !XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(saxAttributes.getURI(i))) {
                    final Attr attribute = document.createAttributeNS(saxAttributes.getURI(i), saxAttributes.getQName(i));
                    attribute.setValue(saxAttributes.getValue(i));
                    element.setAttributeNodeNS(attribute);
                    orderedAttributes.add(attribute);
                }
            }
            attributes.put(element, orderedAttributes);
            namespaceDeclarations.put(element, new LinkedHashMap<>(pendingNamespaces));
            pendingNamespaces.clear();
            if (current == null) {
                document.appendChild(element);
            } else {
                current.appendChild(element);
            }
            current = element;
        }

        @Override
        public void characters(char[] characters, int start, int length) {
            if (current != null) {
                current.appendChild(document.createTextNode(new String(characters, start, length)));
            }
        }

        @Override
        public void ignorableWhitespace(char[] characters, int start, int length) {
            characters(characters, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            final Node parent = current.getParentNode();
            current = parent instanceof Element ? (Element) parent : null;
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            throw e;
        }
    }

    private List<String> getListOfArrayElementNames(final List<Element> elements) {
        final List<String> names = new ArrayList<>();
        final List<String> elementsStr = getElementsFullName(elements);
        for (final String name : elementsStr) {
            if (elementsStr.indexOf(name) != elementsStr.lastIndexOf(name) && !names.contains(name)) {
                names.add(name);
            }
        }
        return names;
    }

    private List<Element> getElementsByName(final String arrayName, final List<Element> elementsToSearch) {
        final List<Element> elements = new ArrayList<>();
        for (Element element : elementsToSearch) {
            if (getElementFullName(element).equals(arrayName)) {
                elements.add(element);
            }
        }
        return elements;
    }

    private List<Element> eliminateElementsWithName(final String arrayName, final List<Element> elements) {
        final List<Element> newElements = new ArrayList<>();
        for (final Element element : elements) {
            if (!getElementFullName(element).equals(arrayName)) {
                newElements.add(element);
            }
        }
        return newElements;
    }

    public String getNamespacesUris() {
        return namespacesUris.toString();
    }

    public String getNamespacesPrefixes() {
        return namespacesPrefixes.toString();
    }
}
