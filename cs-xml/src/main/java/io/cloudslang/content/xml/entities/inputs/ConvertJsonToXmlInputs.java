

package io.cloudslang.content.xml.entities.inputs;

import io.cloudslang.content.xml.utils.Constants.Defaults;
import io.cloudslang.content.xml.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * Created by victor on 10.08.2016.
 */
public class ConvertJsonToXmlInputs {
    private final String json;
    private final boolean prettyPrint;
    private final boolean showXmlDeclaration;
    private final String rootTagName;
    private final String defaultJsonArrayItemName;
    private final Map<String, String> namespaces;
    private final Map<String, String> arraysItemNames;

    public ConvertJsonToXmlInputs(final ConvertJsonToXmlInputsBuilder builder) {
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

    public boolean getPrettyPrint() {
        return prettyPrint;
    }

    public boolean getShowXmlDeclaration() {
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
        private boolean prettyPrint;
        private boolean showXmlDeclaration;
        private String rootTagName;
        private String defaultJsonArrayItemName;
        private Map<String, String> namespaces;
        private Map<String, String> arraysItemNames;

        public ConvertJsonToXmlInputs build() {
            return new ConvertJsonToXmlInputs(this);
        }

        public ConvertJsonToXmlInputsBuilder withJson(final String json) {
            this.json = json;
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withPrettyPrint(final boolean prettyPrint) {
            this.prettyPrint = prettyPrint;
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withShowXmlDeclaration(final boolean showXmlDeclaration) {
            this.showXmlDeclaration = showXmlDeclaration;
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withRootTagName(final String rootTagName) {
            this.rootTagName = defaultIfEmpty(rootTagName, EMPTY);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withDefaultJsonArrayItemName(final String defaultJsonArrayItemName) {
            this.defaultJsonArrayItemName = defaultIfEmpty(defaultJsonArrayItemName, Defaults.JSON_ARRAY_ITEM_NAME);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withNamespaces(String namespacesUris, String namespacesPrefixes, String delimiter) {
            namespacesUris = defaultString(namespacesUris, EMPTY);
            namespacesPrefixes = defaultIfEmpty(namespacesPrefixes, EMPTY);
            delimiter = defaultIfEmpty(delimiter, Defaults.DELIMITER);
            this.namespaces = InputUtils.generateMap(namespacesUris, namespacesPrefixes, delimiter);
            return this;
        }

        public ConvertJsonToXmlInputsBuilder withJsonArraysNames(String jsonArraysNames, String jsonArraysItemNames, String delimiter) {
            jsonArraysNames = defaultString(jsonArraysNames, EMPTY);
            jsonArraysItemNames = defaultIfEmpty(jsonArraysItemNames, EMPTY);
            delimiter = defaultIfEmpty(delimiter, Defaults.DELIMITER);
            this.arraysItemNames = InputUtils.generateMap(jsonArraysNames, jsonArraysItemNames, delimiter);
            return this;
        }

    }
}
