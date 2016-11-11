package io.cloudslang.content.xml.entities.inputs;

import io.cloudslang.content.xml.utils.Constants.Defaults;
import org.apache.commons.lang3.StringUtils;

import static io.cloudslang.content.xml.utils.Constants.EMPTY_STRING;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by victor on 10.08.2016.
 */
public class ConvertXmlToJsonInputs {
    private String xml;
    private String textElementsName;
    private boolean includeRootElement;
    private boolean includeAttributes;
    private boolean prettyPrint;
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

    public boolean getIncludeRootElement() {
        return includeRootElement;
    }

    public boolean getIncludeAttributes() {
        return includeAttributes;
    }

    public boolean getPrettyPrint() {
        return prettyPrint;
    }

    public String getParsingFeatures() {
        return parsingFeatures;
    }

    public static class ConvertXmlToJsonInputsBuilder {
        private String xml;
        private String textElementsName;
        private boolean includeRootElement;
        private boolean includeAttributes;
        private boolean prettyPrint;
        private String parsingFeatures;

        public ConvertXmlToJsonInputs build() {
            return new ConvertXmlToJsonInputs(this);
        }

        public ConvertXmlToJsonInputsBuilder withXml(final String xml) {
            this.xml = defaultIfBlank(xml, EMPTY_STRING);
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withTextElementsName(final String textElementsName) {
            this.textElementsName = defaultIfEmpty(textElementsName, Defaults.DEFAULT_TEXT_ELEMENTS_NAME);
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withIncludeRootElement(final boolean includeRootElement) {
            this.includeRootElement = includeRootElement;
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withIncludeAttributes(final boolean includeAttributes) {
            this.includeAttributes = includeAttributes;
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withPrettyPrint(final boolean prettyPrint) {
            this.prettyPrint = prettyPrint;
            return this;
        }

        public ConvertXmlToJsonInputsBuilder withParsingFeatures(final String parsingFeatures) {
            this.parsingFeatures = parsingFeatures;
            return this;
        }
    }
}
