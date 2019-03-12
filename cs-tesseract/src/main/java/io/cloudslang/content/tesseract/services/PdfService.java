/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.tesseract.services;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.tesseract.utils.Constants.*;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class PdfService {
    private static StringBuffer result = new StringBuffer();

    public static String imageConvert(String sourcePath, String dataPath, String lang, String dpi, String textBlocks, String deskew, String startPage, String finishPage, String certainPages) throws Exception {
        List<File> fileList = null;
        String destination = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator)) + File.separator;
        try {
            if (!sourcePath.equals(EMPTY)) {
                File pdf = new File(sourcePath);
                fileList = requireNonNull(convertPdfToImage(pdf, destination, Integer.parseInt(dpi), Integer.parseInt(startPage), Integer.parseInt(finishPage), certainPages));
                for (File image : fileList) {
                    result.append(OcrService.extractTextFromImage(image.getAbsolutePath(), dataPath, lang, textBlocks, deskew));
                    FileUtils.forceDelete(image);
                }
            }
            return result.toString();
        } finally {
            if (fileList != null) {
                for (File image : fileList) {
                    if (image.exists())
                        FileUtils.forceDelete(image);
                }
            }
        }
    }

    private static List<File> convertPdfToImage(File file, String destination, Integer dpi, Integer startPage, Integer finishPage, String indexPages) throws Exception {
        if (file.exists()) {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            List<File> fileList = new ArrayList<>();

            String fileName = file.getName().replace(PDF, EMPTY);


            if (startPage == 0 && finishPage == 0 && indexPages.equals(ZERO))
                fileList = PdfService.interation(destination, dpi, renderer, fileList, fileName, 0, doc.getNumberOfPages());
            if (startPage == 0 && finishPage != 0 && indexPages.equals(ZERO))
                fileList = PdfService.interation(destination, dpi, renderer, fileList, fileName, 0, finishPage);
            if (startPage != 0 && finishPage != 0 && indexPages.equals(ZERO))
                fileList = PdfService.interation(destination, dpi, renderer, fileList, fileName, --startPage, finishPage);
            else {
                String[] arrSplit = indexPages.split(COMMA);
                for (String anArrSplit : arrSplit) {
                    int val = Integer.parseInt(anArrSplit);
                    File fileTemp = new File(destination + fileName + UNDERSCORE + RandomStringUtils.randomAlphanumeric(15).toUpperCase() + PDF); // jpg or png
                    BufferedImage image = renderer.renderImageWithDPI(--val, dpi);
                    ImageIO.write(image, PNG, fileTemp); // JPEG or PNG
                    fileList.add(fileTemp);
                }
            }

            doc.close();
            return fileList;
        }
        throw new Exception(FILE_NOT_EXISTS);
    }

    private static List<File> interation(String destination, Integer dpi, PDFRenderer renderer, List<File> fileList, String fileName, Integer startPage, Integer finishPage) throws IOException {
        for (int i = startPage; i < finishPage; i++) {
            // default image files path: original file path
            // if necessary, file.getParent() + "/" => another path
            File fileTemp = new File(destination + fileName + UNDERSCORE + RandomStringUtils.randomAlphanumeric(15).toUpperCase() + PDF); // jpg or png
            BufferedImage image = renderer.renderImageWithDPI(i, dpi);
            // if necessary, change 200 into another integer.
            ImageIO.write(image, PNG, fileTemp); // JPEG or PNG
            fileList.add(fileTemp);
        }
        return fileList;
    }
}

