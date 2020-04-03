package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public enum MarkingType {
    SIMPLE_TEXT("simpleText"),
    UNDERLINED_TEXT("underlinedText"),
    TEXT_IN_FRAME("textInFrame"),
    GREY_BOXES("greyBoxes"),
    CHAR_BOX_SERIES("charBoxSeries"),
    SIMPLE_COMB("simpleComb"),
    COMB_IN_FRAME("combInFrame"),
    PARTITIONED_FRAME("partitionedFrame");

    private final String str;


    MarkingType(String str) {
        this.str = str;
    }


    @Override
    public String toString() {
        return this.str;
    }


    public static MarkingType fromString(String str) throws Exception {
        for (MarkingType mt : MarkingType.values()) {
            if (mt.name().equals(str)) {
                return mt;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_MARKING_TYPE, str));
    }
}
