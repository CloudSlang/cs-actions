package io.cloudslang.content.filesystem.entities;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetChildrenInputs {

    private String source;
    private String delimiter;

    public GetChildrenInputs(String source, String delimiter) {
        this.source = source;
        this.delimiter = delimiter;
    }

    public static GetChildrenInputsBuilder builder() {return new GetChildrenInputsBuilder();}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public static class GetChildrenInputsBuilder {

        private String source = EMPTY;
        private String delimiter = EMPTY;

        GetChildrenInputsBuilder(){}

        public GetChildrenInputsBuilder source(final String source) {
            this.source = source;
            return this;
        }

        public GetChildrenInputsBuilder delimiter(final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public GetChildrenInputs build() {
            return new GetChildrenInputs(source, delimiter);
        }
    }

}
