package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public enum WritingStyle {
    DEFAULT,
    AMERICAN,
    GERMAN,
    RUSSIAN,
    POLISH,
    THAI,
    JAPANESE,
    ARABIC,
    BALTIC,
    BRITISH,
    BULGARIAN,
    CANADIAN,
    CZECH,
    CROATIAN,
    FRENCH,
    GREEK,
    HUNGARIAN,
    ITALIAN,
    ROMANIAN,
    SLOVAK,
    SPANISH,
    TURKISH,
    UKRAINIAN,
    COMMON,
    CHINESE,
    AZERBAIJAN,
    KAZAKH,
    KIRGIZ,
    LATVIAN;


    public static WritingStyle fromString(String str) throws Exception {
        for (WritingStyle ws : WritingStyle.values()) {
            if (ws.name().equals(str)) {
                return ws;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_WRITING_STYLE, str));
    }
}
