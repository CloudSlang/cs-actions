package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public enum ExportFormat {
    TXT("txt"),
    TXT_UNSTRUCTURED("txtUnstructured"),
    RTF("rtf"),
    DOCX("docx"),
    XLSX("xlsx"),
    PPTX("pptx"),
    PDF_SEARCHABLE("pdfSearchable"),
    PDF_TEXT_AND_IMAGES("pdfTextAndImages"),
    PDF_A("pdfa"),
    XML("xml"),
    XML_FOR_CORRECTED_IMAGE("xmlForCorrectedImage"),
    ALTO("alto");

    private final String str;


    ExportFormat(String str) {
        this.str = str;
    }


    @Override
    public String toString() {
        return this.str;
    }


    public static ExportFormat fromString(String str) throws Exception {
        for (ExportFormat ef : ExportFormat.values()) {
            if (ef.name().equals(str)) {
                return ef;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_EXPORT_FORMAT, str));
    }
}
