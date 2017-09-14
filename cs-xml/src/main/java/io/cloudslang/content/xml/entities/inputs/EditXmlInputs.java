/***********************************************************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
***********************************************************************************************************************/
package io.cloudslang.content.xml.entities.inputs;

import static io.cloudslang.content.xml.utils.ValidateUtils.validateIsNotEmpty;

/**
 * Created by moldovas on 7/8/2016.
 */
public class EditXmlInputs {
    private String xml;
    private String filePath;
    private String action;
    private String xpath1;
    private String xpath2;
    private String type;
    private String name;
    private String value;
    private String parsingFeatures;

    public EditXmlInputs(EditXmlInputsBuilder builder) {
        this.xml = builder.xml;
        this.filePath = builder.filePath;
        this.action = builder.action;
        this.xpath1 = builder.xpath1;
        this.xpath2 = builder.xpath2;
        this.type = builder.type;
        this.name = builder.name;
        this.value = builder.value;
        this.parsingFeatures = builder.parsingFeatures;
    }

    public String getXml() {
        return xml;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getAction() {
        return action;
    }

    public String getXpath1() {
        return xpath1;
    }

    public String getXpath2() {
        return xpath2;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getParsingFeatures() {
        return parsingFeatures;
    }

    public static class EditXmlInputsBuilder {
        private String xml;
        private String filePath;
        private String action;
        private String xpath1;
        private String xpath2;
        private String type;
        private String name;
        private String value;
        private String parsingFeatures;

        public EditXmlInputs build() {
            return new EditXmlInputs(this);
        }

        public EditXmlInputs.EditXmlInputsBuilder withXml(String inputValue) {
            xml = inputValue;
            return this;
        }

        public EditXmlInputs.EditXmlInputsBuilder withFilePath(String inputValue) {
            filePath = inputValue;
            return this;
        }

        public EditXmlInputs.EditXmlInputsBuilder withAction(String inputValue) throws Exception {
            validateIsNotEmpty(inputValue, "action input is required.");
            action = inputValue.toLowerCase();
            return this;
        }

        public EditXmlInputs.EditXmlInputsBuilder withXpath1(String inputValue) throws Exception {
            validateIsNotEmpty(inputValue, "xpath1 input is required.");
            xpath1 = inputValue;
            return this;
        }

        public EditXmlInputs.EditXmlInputsBuilder withType(String inputValue) {
            type = inputValue;
            return this;
        }

        public EditXmlInputs.EditXmlInputsBuilder withName(String inputValue) {
            name = inputValue;
            return this;
        }

        public EditXmlInputs.EditXmlInputsBuilder withValue(String inputValue) {
            value = inputValue;
            return this;
        }

        public EditXmlInputs.EditXmlInputsBuilder withXpath2(String inputValue) {
            xpath2 = inputValue;
            return this;
        }

        public EditXmlInputs.EditXmlInputsBuilder withParsingFeatures(String inputValue) {
            parsingFeatures = inputValue;
            return this;
        }
    }

}
