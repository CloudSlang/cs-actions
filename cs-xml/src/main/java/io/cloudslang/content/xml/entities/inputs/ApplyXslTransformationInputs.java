package io.cloudslang.content.xml.entities.inputs;

import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.xml.utils.Constants;

/**
 * Created by moldovas on 08/07/2016.
 */
public class ApplyXslTransformationInputs {
    private String xmlDocument;
    private String xslTemplate;
    private String outputFile;
    private String parsingFeatures;

    private ApplyXslTransformationInputs(ApplyXslTransformationBuilder builder) {
        this.xmlDocument = builder.xmlDocument;
        this.xslTemplate = builder.xslTemplate;
        this.outputFile = builder.outputFile;
        this.parsingFeatures = builder.parsingFeatures;
    }

    public String getXmlDocument() {
        return xmlDocument;
    }

    public String getXslTemplate() {
        return xslTemplate;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getParsingFeatures() {
        return parsingFeatures;
    }

    public static class ApplyXslTransformationBuilder {
        private String xmlDocument;
        private String xslTemplate;
        private String outputFile;
        private String parsingFeatures;

        public ApplyXslTransformationInputs build() {
            return new ApplyXslTransformationInputs(this);
        }


        public ApplyXslTransformationBuilder withXmlDocument(String inputValue) {
            xmlDocument = inputValue;
            return this;
        }

        public ApplyXslTransformationBuilder withXslTemplate(String inputValue) {
            xslTemplate = inputValue;
            return this;
        }

        public ApplyXslTransformationBuilder withOutputFile(String inputValue) {
            outputFile = StringUtilities.isBlank(inputValue) ? Constants.EMPTY_STRING : inputValue;
            return this;
        }

        public ApplyXslTransformationBuilder withParsingFeatures(String inputValue) {
            parsingFeatures = inputValue;
            return this;
        }
    }
}

