package io.cloudslang.content.xml.entities.inputs;

import io.cloudslang.content.xml.utils.Constants.BooleanNames;
import io.cloudslang.content.xml.utils.Constants.Defaults;
import org.apache.commons.lang3.StringUtils;

import static io.cloudslang.content.xml.utils.Constants.EMPTY_STRING;

/**
 * Created by victor on 10.08.2016.
 */
public class ConvertXmlToJsonInputs {
    private String xml;
    private String textElementsName;
    private Boolean includeRootElement;
    private Boolean includeAttributes;
    private Boolean prettyPrint;
    private String parsingFeatures;

    public ConvertXmlToJsonInputs(ConvertXmlToJsonInputsBuilder builder) {
        this.xml = builder.xml;
        this.textElementsName = builder.textElementsName;
        this.includeRootElement = builder.includeRootElement;
        this.includeAttributes = builder.includeAttributes;
        this.prettyPrint = builder.prettyPrint;
        this.parsingFeatures = builder.parsingFeatures;
    }

    public String getXml() {
        return xml;
    }

    public String getTextElementsName() {
        return textElementsName;
    }

    public Boolean getIncludeRootElement() {
        return includeRootElement;
    }

    public Boolean getIncludeAttributes() {
        return includeAttributes;
    }

    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public String getParsingFeatures() {
        return parsingFeatures;
    }

    public static class ConvertXmlToJsonInputsBuilder {
        private String xml;
        private String textElementsName;
        private Boolean includeRootElement;
        private Boolean includeAttributes;
        private Boolean prettyPrint;
        private String parsingFeatures;

        public ConvertXmlToJsonInputs build() {
            return new ConvertXmlToJsonInputs(this);
        }

        public ConvertXmlToJsonInputsBuilder withXml(String xml) {
            this.xml = StringUtils.defaultIfBlank(xml, EMPTY_STRING);
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withTextElementsName(String textElementsName) {
            this.textElementsName = StringUtils.defaultIfEmpty(textElementsName, Defaults.DEFAULT_TEXT_ELEMENTS_NAME);
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withIncludeRootElement(Boolean includeRootElement) {
            this.includeRootElement = includeRootElement;
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withIncludeAttributes(Boolean includeAttributes) {
            this.includeAttributes = includeAttributes;
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withPrettyPrint(Boolean prettyPrint) {
            this.prettyPrint = prettyPrint;
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withParsingFeatures(String parsingFeatures) {
            this.parsingFeatures = parsingFeatures;
            return this;
        }
    }
}
