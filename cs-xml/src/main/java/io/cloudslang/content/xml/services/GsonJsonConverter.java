package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.utils.XmlUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static io.cloudslang.content.xml.utils.Constants.*;

/**
 * Created by persdana on 11/17/2014.
 */
public class GsonJsonConverter {
    private StringBuilder namespacesPrefixes;
    private StringBuilder namespacesUris;

    public GsonJsonConverter() {
        namespacesPrefixes = new StringBuilder();
        namespacesUris = new StringBuilder();
    }

    public String convertToJsonString(String xml,
                                      Boolean includeAttributes,
                                      Boolean prettyPrint,
                                      Boolean addRootElement,
                                      String parsingFeatures,
                                      String textPropName) throws JDOMException, IOException, SAXException {

        InputSource inputSource = new InputSource(new StringReader(xml));
        SAXBuilder builder = new SAXBuilder();
        XmlUtils.setFeatures(builder, parsingFeatures);
        Document document = builder.build(inputSource);
        Element root = document.getRootElement();

        List<Element> xmlElements = Collections.singletonList(root);
        JsonObject jsonObject = convertToJsonObject(xmlElements, includeAttributes, textPropName);

        //add root element
        JsonObject rootJson = new JsonObject();
        if (addRootElement) {
            rootJson.add(root.getName(), jsonObject);
        } else {
            rootJson = jsonObject;
        }

        //pretty print
        if (prettyPrint) {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            return gson.toJson(rootJson);
        }
        return rootJson.toString();

    }

    private JsonObject addJsonObjectsAndPrimitives(JsonObject jsonObject, List<Element> elements, Boolean includeAttributes, String textPropName) {
        for (Element element : elements) {
            JsonElement jsonElement = isPrimitiveElement(element)?
                    new JsonPrimitive(element.getValue()) : convertToJsonObject(Collections.singletonList(element), includeAttributes, textPropName);
            jsonObject.add(getElementFullName(element), jsonElement);
        }
        return jsonObject;
    }


    private JsonObject convertToJsonObject(List<Element> xmlElements, Boolean includeAttributes, String textPropName) {
        JsonObject result = new JsonObject();
        for (Element xmlElement : xmlElements) {
            result = addToJsonObjectXmlElement(result, xmlElement, includeAttributes, textPropName);
        }
        return result;
    }

    private JsonArray convertToJsonArray(List<Element> xmlElements, Boolean includeAttributes, String textPropName) {
        JsonArray result = new JsonArray();
        for (Element xmlElement : xmlElements) {
            result.add(addToJsonObjectXmlElement(new JsonObject(), xmlElement, includeAttributes, textPropName));
        }
        return result;
    }

    private JsonObject addToJsonObjectXmlElement(JsonObject jsonObject, Element xmlElement, Boolean includeAttributes,String textPropName) {
        addNamespaces(xmlElement.getAdditionalNamespaces());

        if (includeAttributes) {
            jsonObject = addAttributes(jsonObject, xmlElement.getAttributes());
        }

        List<Element> children = xmlElement.getChildren();
        List<String> arrayElementsNames = getListOfArrayElementNames(children);

        for (String arrayName: arrayElementsNames) {
            //add array
            List<Element> elements = getElementsByName(arrayName, children);
            jsonObject.add(arrayName, convertToJsonArray(elements, includeAttributes, textPropName));
            //eliminate what was added
            children = eliminateElementsWithName(arrayName, children);
        }

        //add jsonObjects and jsonPrimitives
        jsonObject = addJsonObjectsAndPrimitives(jsonObject, children, includeAttributes, textPropName);

        //add text prop
        jsonObject = addTextProp(jsonObject, xmlElement.getText(), textPropName);
        return jsonObject;
    }

    private JsonObject addTextProp(JsonObject jsonObject, String text, String textPropName) {
        if (!StringUtils.isEmpty(text) && text.matches(".*[a-zA-Z0-9].*")) {
            jsonObject.addProperty(textPropName, text);
        }
        return jsonObject;
    }

    private void addNamespaces(List<Namespace> namespaces) {
        for (Namespace namespace : namespaces) {
            if (namespacesUris.length() > 0) {
                namespacesPrefixes.append(Defaults.DELIMITER);
                namespacesUris.append(Defaults.DELIMITER);
            }
            namespacesPrefixes.append(namespace.getPrefix());
            namespacesUris.append(namespace.getURI());
        }
    }

    private JsonObject addAttributes(JsonObject jsonObject, List<Attribute> attributes) {
        for (Attribute attribute : attributes) {
            jsonObject.addProperty(JSON_ATTRIBUTE_PREFIX + attribute.getName(), attribute.getValue());
        }
        return jsonObject;
    }

    private String getElementFullName(Element element) {
        String name = element.getNamespacePrefix();
        if (!name.isEmpty())
            name += Defaults.PREFIX_DELIMITER;
        name += element.getName();
        return name;
    }

    private List<String> getElementsFullName(List<Element> elements) {
        List<String> names = new LinkedList<>();
        for (Element element : elements) {
            names.add(getElementFullName(element));
        }
        return names;
    }


    private boolean isPrimitiveElement(Element element) {
        return element.getChildren().isEmpty() && element.getAttributes().isEmpty(); //if it doesn't have child and doesn't have attributes it's primitive.
    }

    /**
     * @param elements - a list of xml Elements
     * @return the List of names of the elements that were found at least twice in the <elements>
     */
    private List<String> getListOfArrayElementNames(List<Element> elements) {
        List<String> names = new LinkedList<>();
        List<String> elementsStr = getElementsFullName(elements);
        for (String name : elementsStr) {
            if (elementsStr.indexOf(name) != elementsStr.lastIndexOf(name) && !names.contains(name)) {
                names.add(name);
            }
        }
        return names;
    }

    private List<Element> getElementsByName(String arrayName, List<Element> elementsToSearch) {
        List<Element> elements = new LinkedList<>();
        for (Element element : elementsToSearch) {
            if (getElementFullName(element).equals(arrayName)) {
                elements.add(element);
            }
        }
        return elements;
    }

    private List<Element> eliminateElementsWithName(String arrayName, List<Element> elements) {
        List<Element> newElements = new LinkedList<>();
        for (Element element : elements) {
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
