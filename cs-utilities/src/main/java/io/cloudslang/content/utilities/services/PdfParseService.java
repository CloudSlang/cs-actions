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

/**
 * Created by marisca on 7/11/2017.
 */
public class PdfParseService {

    public static String getPdfContent(final Path path, final String password) throws IOException {
        final PDDocument document;

        if (password.isEmpty()) {
            document = PDDocument.load(path.toFile());
        } else {
            document = PDDocument.load(path.toFile(), password);
        }

        final String pdfContent = new PDFTextStripper().getText(document);
        document.close();

        return pdfContent;
    }
}
