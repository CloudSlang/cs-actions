package io.cloudslang.content.maps.entities;

import io.cloudslang.content.maps.constants.DefaultInputValues;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.utils.BuilderUtils;
import org.apache.commons.lang3.StringUtils;

public class ModifyMapElementsInput {

    private final String map;
    private final String elements;
    private final String method;
    private final String value;
    private final String pairDelimiter;
    private final String entryDelimiter;
    private final String mapStart;
    private final String mapEnd;
    private final String elementWrapper;
    private final boolean stripWhitespaces;


    private ModifyMapElementsInput(Builder builder) {
        this.map = builder.map;
        this.elements = builder.elements;
        this.method = builder.method;
        this.value = builder.value;
        this.pairDelimiter = builder.pairDelimiter;
        this.entryDelimiter = builder.entryDelimiter;
        this.mapStart = builder.mapStart;
        this.mapEnd = builder.mapEnd;
        this.elementWrapper = builder.elementWrapper;
        this.stripWhitespaces = builder.stripWhitespaces;
    }


    public String getMap() {
        return map;
    }


    public String getElements() {
        return elements;
    }


    public String getMethod() {
        return method;
    }


    public String getValue() {
        return value;
    }


    public String getPairDelimiter() {
        return pairDelimiter;
    }


    public String getEntryDelimiter() {
        return entryDelimiter;
    }


    public String getMapStart() {
        return mapStart;
    }


    public String getMapEnd() {
        return mapEnd;
    }


    public String getElementWrapper() {
        return elementWrapper;
    }


    public boolean isStripWhitespaces() {
        return stripWhitespaces;
    }


    public static class Builder {
        private String map;
        private String elements;
        private String method;
        private String value;
        private String pairDelimiter;
        private String entryDelimiter;
        private String mapStart;
        private String mapEnd;
        private String elementWrapper;
        private boolean stripWhitespaces;


        public Builder map(String map) {
            this.map = StringUtils.defaultString(map, DefaultInputValues.MAP);
            return this;
        }


        public Builder elements(String elements) {
            this.elements = StringUtils.defaultString(elements, DefaultInputValues.ELEMENTS);
            return this;
        }


        public Builder method(String method) {
            this.method = StringUtils.defaultString(method, DefaultInputValues.METHOD);
            return this;
        }

        public Builder value(String value) {
            this.value = StringUtils.defaultString(value, DefaultInputValues.METHOD_VALUE);
            return this;
        }


        public Builder pairDelimiter(String pairDelimiter) {
            this.pairDelimiter = pairDelimiter;
            return this;
        }


        public Builder entryDelimiter(String entryDelimiter) {
            this.entryDelimiter = entryDelimiter;
            return this;
        }


        public Builder mapStart(String mapStart) {
            this.mapStart = StringUtils.defaultString(mapStart, DefaultInputValues.MAP_START);
            return this;
        }


        public Builder mapEnd(String mapEnd) {
            this.mapEnd = StringUtils.defaultString(mapEnd, DefaultInputValues.MAP_END);
            return this;
        }


        public Builder elementWrapper(String elementWrapper) {
            this.elementWrapper = StringUtils.defaultString(elementWrapper, DefaultInputValues.ELEMENT_WRAPPER);
            return this;
        }


        public Builder stripWhitespaces(String stripWhitespaces) throws ValidationException {
            this.stripWhitespaces = BuilderUtils.parseStripWhitespaces(stripWhitespaces);
            return this;
        }


        public ModifyMapElementsInput build() {
            return new ModifyMapElementsInput(this);
        }
    }
}