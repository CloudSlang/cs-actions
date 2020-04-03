package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public enum TextType {
    NORMAL("normal"),
    TYPEWRITER("typewriter"),
    MATRIX("matrix"),
    INDEX("index"),
    OCR_A("ocrA"),
    OCR_B("ocrB"),
    E13B("e13b"),
    CMC7("cmc7"),
    GOTHIC("gothic");


    private final String str;


    TextType(String str) {
        this.str = str;
    }


    @Override
    public String toString() {
        return this.str;
    }


    public static TextType fromString(String str) throws Exception {
        for (TextType t : TextType.values()) {
            if (t.str.equals(str)) {
                return t;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_TEXT_TYPE, str));
    }
}
