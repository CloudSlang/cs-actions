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

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by marisca on 7/11/2017.
 */
public class PdfParseService {

    public static String getPdfContent(@NotNull final String content, final File file) throws IOException, TikaException, SAXException {
        final BodyContentHandler handler = new BodyContentHandler();
        final Metadata metadata = new Metadata();
        final FileInputStream inputStream = new FileInputStream(file);
        final ParseContext context = new ParseContext();

        final PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputStream, handler, metadata, context);

        return handler.toString();
    }
}
