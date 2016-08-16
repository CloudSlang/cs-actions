package io.cloudslang.content.xml.entities.inputs;

import io.cloudslang.content.xml.utils.Constants.BooleanNames;
import io.cloudslang.content.xml.utils.Constants.Defaults;
import io.cloudslang.content.xml.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static io.cloudslang.content.xml.utils.Constants.EMPTY_STRING;

/**
 * Created by victor on 10.08.2016.
 */
public class ConvertJsonToXmlInputs {
    private String json;
    private Boolean prettyPrint;
    private Boolean showXmlDeclaration;
    private String rootTagName;
    private String defaultJsonArrayItemName;
    private Map<String, String> namespaces;
    private Map<String, String> arraysItemNames;

    public ConvertJsonToXmlInputs(ConvertJsonToXmlInputsBuilder builder) {
        this.json = builder.json;
        this.prettyPrint = builder.prettyPrint;
        this.showXmlDeclaration = builder.showXmlDeclaration;
        this.rootTagName = builder.rootTagName;
        this.defaultJsonArrayItemName = builder.defaultJsonArrayItemName;
        this.namespaces = builder.namespaces;
        this.arraysItemNames = builder.arraysItemNames;
    }

    public String getJson() {
        return json;
    }

    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public Boolean getShowXmlDeclaration() {
        return showXmlDeclaration;
    }

    public String getRootTagName() {
        return rootTagName;
    }

    public String getDefaultJsonArrayItemName() {
        return defaultJsonArrayItemName;
    }

    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    public Map<String, String> getArraysItemNames() {
        return arraysItemNames;
    }

    public static class ConvertJsonToXmlInputsBuilder {
        private String json;
        private Boolean prettyPrint;
        private Boolean showXmlDeclaration;
        private String rootTagName;
        private String defaultJsonArrayItemName;
        private Map<String, String> namespaces;
        private Map<String, String> arraysItemNames;

        public ConvertJsonToXmlInputs build() {
            return new ConvertJsonToXmlInputs(this);
        }

        public ConvertJsonToXmlInputsBuilder withJson(String json) {
            this.json = json;
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withPrettyPrint(Boolean prettyPrint) {
            this.prettyPrint = prettyPrint;
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withShowXmlDeclaration(Boolean showXmlDeclaration) {
            this.showXmlDeclaration = showXmlDeclaration;
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withRootTagName(String rootTagName) {
            this.rootTagName = StringUtils.defaultIfEmpty(rootTagName, EMPTY_STRING);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withDefaultJsonArrayItemName(String defaultJsonArrayItemName) {
            this.defaultJsonArrayItemName = StringUtils.defaultIfEmpty(defaultJsonArrayItemName, Defaults.JSON_ARRAY_ITEM_NAME);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withNamespaces(String namespacesUris, String namespacesPrefixes, String delimiter) {
            namespacesUris = StringUtils.defaultString(namespacesUris, EMPTY_STRING);
            namespacesPrefixes = StringUtils.defaultIfEmpty(namespacesPrefixes, EMPTY_STRING);
            delimiter = StringUtils.defaultIfEmpty(delimiter, Defaults.DELIMITER);
            this.namespaces = InputUtils.generateMap(namespacesUris, namespacesPrefixes, delimiter);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withJsonArraysNames(String jsonArraysNames, String jsonArraysItemNames, String delimiter) {
            jsonArraysNames = StringUtils.defaultString(jsonArraysNames, EMPTY_STRING);
            jsonArraysItemNames = StringUtils.defaultIfEmpty(jsonArraysItemNames, EMPTY_STRING);
            delimiter = StringUtils.defaultIfEmpty(delimiter, Defaults.DELIMITER);
            this.arraysItemNames = InputUtils.generateMap(jsonArraysNames, jsonArraysItemNames, delimiter);
            return this;
        }

    }
}
