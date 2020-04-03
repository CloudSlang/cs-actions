package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public enum ImageSource {
    AUTO,
    PHOTO,
    SCANNER;


    public static ImageSource fromString(String str) throws Exception {
        for (ImageSource is : ImageSource.values()) {
            if (is.name().equals(str)) {
                return is;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_IMAGE_SOURCE, str));
    }
}
