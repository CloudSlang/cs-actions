package io.cloudslang.content.xml.entities.inputs;

import io.cloudslang.content.xml.utils.Constants.*;
import io.cloudslang.content.xml.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import static io.cloudslang.content.xml.utils.Constants.EMPTY_STRING;

/**
 * Created by victor on 10.08.2016.
 */
public class ConvertJsonToXmlInputs {
    private String json;
    private String prettyPrint;
    private String showXmlDeclaration;
    private String rootTagName;
    private String defaultJsonArrayItemName;
    private String namespacesPrefixes;
    private String namespacesUris;
    private String jsonArraysNames;
    private String jsonArraysItemNames;
    private String delimiter;

    public ConvertJsonToXmlInputs(ConvertJsonToXmlInputsBuilder builder) {
        this.json = builder.json;
        this.prettyPrint = builder.prettyPrint;
        this.showXmlDeclaration = builder.showXmlDeclaration;
        this.rootTagName = builder.rootTagName;
        this.defaultJsonArrayItemName = builder.defaultJsonArrayItemName;
        this.namespacesPrefixes = builder.namespacesPrefixes;
        this.namespacesUris = builder.namespacesUris;
        this.jsonArraysNames = builder.jsonArraysNames;
        this.jsonArraysItemNames = builder.jsonArraysItemNames;
        this.delimiter = builder.delimiter;
    }

    public String getJson() {
        return json;
    }

    public String getPrettyPrint() {
        return prettyPrint;
    }

    public String getShowXmlDeclaration() {
        return showXmlDeclaration;
    }

    public String getRootTagName() {
        return rootTagName;
    }

    public String getDefaultJsonArrayItemName() {
        return defaultJsonArrayItemName;
    }

    public String getNamespacesPrefixes() {
        return namespacesPrefixes;
    }

    public String getNamespacesUris() {
        return namespacesUris;
    }

    public String getJsonArraysNames() {
        return jsonArraysNames;
    }

    public String getJsonArraysItemNames() {
        return jsonArraysItemNames;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public static class ConvertJsonToXmlInputsBuilder {
        private String json;
        private String prettyPrint;
        private String showXmlDeclaration;
        private String rootTagName;
        private String defaultJsonArrayItemName;
        private String namespacesPrefixes;
        private String namespacesUris;
        private String jsonArraysNames;
        private String jsonArraysItemNames;
        private String delimiter;

        public ConvertJsonToXmlInputs build() {
            return new ConvertJsonToXmlInputs(this);
        }

        public ConvertJsonToXmlInputsBuilder withJson(String json) {
            this.json = json;
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withPrettyPrint(String prettyPrint) {
            this.prettyPrint = StringUtils.defaultIfEmpty(prettyPrint, BooleanNames.TRUE);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withShowXmlDeclaration(String showXmlDeclaration) {
            this.showXmlDeclaration = StringUtils.defaultIfEmpty(showXmlDeclaration, BooleanNames.FALSE);
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

        public ConvertJsonToXmlInputsBuilder withNamespacesPrefixes(String namespacesPrefixes) {
            this.namespacesPrefixes = StringUtils.defaultIfEmpty(namespacesPrefixes, EMPTY_STRING);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withNamespacesUris(String namespacesUris) {
            this.namespacesUris = StringUtils.defaultString(namespacesUris, EMPTY_STRING);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withJsonArraysNames(String jsonArraysNames) {
            this.jsonArraysNames = StringUtils.defaultString(jsonArraysNames, EMPTY_STRING);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withJsonArraysItemNames(String jsonArraysItemNames) {
            this.jsonArraysItemNames = StringUtils.defaultIfEmpty(jsonArraysItemNames, EMPTY_STRING);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withDelimiter(String delimiter) {
            this.delimiter = StringUtils.defaultIfEmpty(delimiter, Defaults.DELIMITER);
            return this;
        }
    }
}
