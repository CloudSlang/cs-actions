/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */

package io.cloudslang.content.utilities.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.file.Path;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by marisca on 7/11/2017.
 */
public class PdfParseService {

    public static String getPdfContent(final Path path, final String password) throws IOException {
        try (final PDDocument document = getPdfDocument(path, password)) {
            return new PDFTextStripper().getText(document);
        }
    }

    private static PDDocument getPdfDocument(final Path path, final String password) throws IOException {
        if (isEmpty(password))
            return PDDocument.load(path.toFile());
        return PDDocument.load(path.toFile(), password);
    }

    public static String getOccurrences(final String pdfContent, final String text, boolean ignoreCase) {
        if (ignoreCase)
            return valueOf(countMatches(pdfContent.toLowerCase(), text.toLowerCase()));
        return valueOf(countMatches(pdfContent, text));
    }
}
