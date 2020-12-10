package io.cloudslang.content.filesystem.entities;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MD5SumInputs {

    private String source;
    private String compareTo;

    public MD5SumInputs(String source, String compareTo) {
        this.source = source;
        this.compareTo = compareTo;
    }

    public static MD5SumInputsBuilder builder() {return new MD5SumInputsBuilder();}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCompareTo() {
        return compareTo;
    }

    public void setCompareTo(String compareTo) {
        this.compareTo = compareTo;
    }

    public static class MD5SumInputsBuilder {

        private String source = EMPTY;
        private String compareTo = EMPTY;

        MD5SumInputsBuilder() {}

        public MD5SumInputsBuilder source(final String source) {
            this.source = source;
            return this;
        }

        public MD5SumInputsBuilder compareTo(final String compareTo) {
            this.compareTo = compareTo;
            return this;
        }

        public MD5SumInputs build() {
            return new MD5SumInputs(source, compareTo);
        }
    }
}
