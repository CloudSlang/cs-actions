/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */




package io.cloudslang.content.utilities.services;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileNotFoundException;
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

    public static String mergeFiles(final String pathToFile, final String pathToFiles) throws IOException {
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        PDFmerger.setDestinationFileName(pathToFile);
        for(String s: pathToFiles.split(",")){
            PDFmerger.addSource(new File(s.trim()));
        }
        PDFmerger.mergeDocuments(null);
        return pathToFile;
    }
}
