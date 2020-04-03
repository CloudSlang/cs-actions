package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public enum Profile {
    DOCUMENT_CONVERSION("documentConversion"),
    DOCUMENT_ARCHIVING("documentArchiving"),
    TEXT_EXTRACTION("textExtraction"),
    BARCODE_RECOGNITION("barcodeRecognition");

    private final String str;


    Profile(String str) {
        this.str = str;
    }


    @Override
    public String toString() {
        return this.str;
    }


    public static Profile fromString(String str) throws Exception {
        for (Profile p : Profile.values()) {
            if (p.str.equals(str)) {
                return p;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_PROFILE, str));
    }
}
