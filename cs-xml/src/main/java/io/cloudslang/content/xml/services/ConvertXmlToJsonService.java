

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
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.cloudslang.content.xml.utils.Constants.Defaults;
import static io.cloudslang.content.xml.utils.Constants.Defaults.PREFIX_DELIMITER;
import static io.cloudslang.content.xml.utils.Constants.JSON_ATTRIBUTE_PREFIX;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertXmlToJsonService {
    private final StringBuilder namespacesPrefixes;
    private final StringBuilder namespacesUris;

    public ConvertXmlToJsonService() {
        namespacesPrefixes = new StringBuilder();
        namespacesUris = new StringBuilder();
    }

    public String convertToJsonString(final ConvertXmlToJsonInputs inputs) throws JDOMException, IOException, SAXException {
        if (StringUtils.isBlank(inputs.getXml())) {
            return EMPTY;
        }
        final InputSource inputSource = new InputSource(new StringReader(inputs.getXml()));
        final SAXBuilder builder = new SAXBuilder();
        XmlUtils.setFeatures(builder, inputs.getParsingFeatures());
        final Element root = builder.build(inputSource).getRootElement();
        final List<Element> xmlElements = Collections.singletonList(root);
        if (root.getChildren().isEmpty() && !inputs.getIncludeAttributes()) {
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
            rootJson.add(rootElement.getName(), jsonObject);
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
                    new JsonPrimitive(element.getValue()) : convertXmlElementsToJsonObject(Collections.singletonList(element), includeAttributes, textPropName);
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
        addNamespaces(xmlElement.getAdditionalNamespaces());
        if (includeAttributes) {
            jsonObject = addAttributesToJsonObject(jsonObject, xmlElement.getAttributes());
        }
        List<Element> children = xmlElement.getChildren();
        jsonObject = searchAndAddArrayElementsToJsonObject(jsonObject, children, includeAttributes, textPropName);
        children = eliminateArrayElements(children);
        jsonObject = addJsonObjectsAndPrimitives(jsonObject, children, includeAttributes, textPropName);
        jsonObject = addTextProp(jsonObject, xmlElement.getText(), textPropName);
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

    private void addNamespaces(final List<Namespace> namespaces) {
        for (final Namespace namespace : namespaces) {
            if (namespacesUris.length() > 0) {
                namespacesPrefixes.append(Defaults.DELIMITER);
                namespacesUris.append(Defaults.DELIMITER);
            }
            namespacesPrefixes.append(namespace.getPrefix());
            namespacesUris.append(namespace.getURI());
        }
    }

    private JsonObject addAttributesToJsonObject(final JsonObject jsonObject, final List<Attribute> attributes) {
        for (final Attribute attribute : attributes) {
            jsonObject.addProperty(JSON_ATTRIBUTE_PREFIX + attribute.getName(), attribute.getValue());
        }
        return jsonObject;
    }

    @NotNull
    private String getElementFullName(final Element element) {
        final StringBuilder name = new StringBuilder(element.getNamespacePrefix());
        if (!element.getNamespacePrefix().isEmpty())
            name.append(PREFIX_DELIMITER);
        name.append(element.getName());
        return name.toString();
    }

    private List<String> getElementsFullName(final List<Element> elements) {
        final List<String> names = new ArrayList<>();
        for (final Element element : elements) {
            names.add(getElementFullName(element));
        }
        return names;
    }


    private boolean isPrimitiveElement(final Element element) {
        return element.getChildren().isEmpty() && element.getAttributes().isEmpty(); //if it doesn't have child and doesn't have attributes it's primitive.
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
