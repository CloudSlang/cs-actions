package io.cloudslang.content.xml.entities.inputs;

import io.cloudslang.content.xml.entities.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by markowis on 03/03/2016.
 */
public class CustomInputs {
    private String attributeName;
    private String value;
    private String xmlElement;
    private String xsdDocument;
    private String xsdDocumentSource;
    private String queryType;
    private String delimiter;

    public CustomInputs(CustomInputsBuilder builder) {
        this.attributeName = builder.attributeName;
        this.value = builder.value;
        this.xmlElement = builder.xmlElement;
        this.xsdDocument = builder.xsdDocument;
        this.xsdDocumentSource = builder.xsdDocumentSource;
        this.queryType = builder.queryType;
        this.delimiter = builder.delimiter;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getValue() {
        return value;
    }

    public String getXmlElement() {
        return xmlElement;
    }

    public String getXsdDocument() {
        return xsdDocument;
    }

    public String getXsdDocumentSource() {
        return xsdDocumentSource;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public static class CustomInputsBuilder {
        private String attributeName;
        private String value;
        private String xmlElement;
        private String xsdDocument;
        private String xsdDocumentSource;
        private String queryType;
        private String delimiter;

        public CustomInputs build() {
            return new CustomInputs(this);
        }

        public CustomInputsBuilder withAttributeName(String inputValue) {
            attributeName = inputValue;
            return this;
        }

        public CustomInputsBuilder withValue(String inputValue) {
            value = inputValue;
            return this;
        }

        public CustomInputsBuilder withXmlElement(String inputValue) {
            xmlElement = inputValue;
            return this;
        }

        public CustomInputsBuilder withXsdDocument(String inputValue) {
            xsdDocument = inputValue;
            return this;
        }

        public CustomInputsBuilder withQueryType(String inputValue) {
            queryType = inputValue;
            return this;
        }

        public CustomInputsBuilder withDelimiter(String inputValue) {
            if(StringUtils.isBlank(inputValue)){
                delimiter = Constants.Defaults.DELIMITER;
            }
            else {
                delimiter = inputValue;
            }
            return this;
        }

        public CustomInputsBuilder withXsdDocumentSource(String xsdDocumentSource) {
            if (xsdDocumentSource.isEmpty()) {
                this.xsdDocumentSource = Constants.Defaults.XSD_DOCUMENT_SOURCE;
            } else if (Constants.Defaults.XSD_DOCUMENT_SOURCE.equalsIgnoreCase(xsdDocumentSource) || Constants.XSD_PATH.equalsIgnoreCase(xsdDocumentSource)){
                this.xsdDocumentSource = xsdDocumentSource;
            } else {
                throw new RuntimeException(xsdDocumentSource + Constants.INVALID_XSD_DOCUMENT_SOURCE);
            }
            return this;
        }
    }
}

