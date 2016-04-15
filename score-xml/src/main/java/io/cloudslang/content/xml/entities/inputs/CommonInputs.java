package io.cloudslang.content.xml.entities.inputs;

/**
 * Created by markowis on 03/03/2016.
 */
public class CommonInputs {
    private String xmlDocument;
    private String xPathQuery;
    private boolean secureProcessing;

    public CommonInputs(CommonInputsBuilder builder) {
        this.xmlDocument = builder.xmlDocument;
        this.xPathQuery = builder.xPathQuery;
        this.secureProcessing = builder.secureProcessing;
    }

    public String getXmlDocument() {
        return xmlDocument;
    }

    public String getXPathQuery() {
        return xPathQuery;
    }

    public boolean getSecureProcessing() {
        return secureProcessing;
    }

    public static class CommonInputsBuilder {
        private String xmlDocument;
        private String xPathQuery;
        private boolean secureProcessing;

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public CommonInputsBuilder withXmlDocument(String inputValue) {
            xmlDocument = inputValue;
            return this;
        }

        public CommonInputsBuilder withXpathQuery(String inputValue) {
            xPathQuery = inputValue;
            return this;
        }

        public CommonInputsBuilder withSecureProcessing(String inputValue) {
            secureProcessing = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}
