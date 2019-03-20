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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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

    public static String imageConvert(String sourcePath, String dataPath, String lang, String dpi, String textBlocks, String deskew, String fromPage, String toPage, String pageIndex)
            throws Exception {
        StringBuilder result = new StringBuilder();
        List<File> fileList = null;
        String destination = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator)) + File.separator;
        try {
            if (!sourcePath.equals(EMPTY)) {
                JsonObject outputObject = new JsonObject();
                JsonArray outputArray = new JsonArray();
                File pdf = new File(sourcePath);
                fileList = requireNonNull(convertPdfToImage(pdf, destination, dpi, fromPage, toPage,
                        pageIndex));
                if (Boolean.parseBoolean(textBlocks)) {
                    for (File image : fileList) {
                        outputArray.add(OcrService.extractTextFromImage(image.getAbsolutePath(), dataPath, lang, textBlocks,
                                deskew));
                        FileUtils.forceDelete(image);
                    }
                    outputObject.add(PAGE, outputArray);
                    result.append(outputObject.toString());
                } else {
                    for (File image : fileList) {
                        result.append(OcrService.extractTextFromImage(image.getAbsolutePath(), dataPath, lang, textBlocks,
                                deskew));
                        FileUtils.forceDelete(image);
                    }
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


    private static List<File> convertPdfToImage(File file, String destination, String dpi, String fromPage,
                                                String toPage, String pageIndex) throws Exception {
        if (file.exists()) {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            List<File> fileList = new ArrayList<>();

            String fileName = file.getName().replace(PDF_EXTENSION, EMPTY);

            if (StringUtils.equals(fromPage, EMPTY) && StringUtils.equals(toPage, EMPTY) && StringUtils.equals(pageIndex, EMPTY))
                fileList = PdfService.iteration(destination, Integer.parseInt(dpi), renderer, fileList, fileName, 0,
                        doc.getNumberOfPages(), pageIndex);
            if (StringUtils.equals(fromPage, EMPTY) && !StringUtils.equals(toPage, EMPTY) && StringUtils.equals(pageIndex, EMPTY))
                fileList = PdfService.iteration(destination, Integer.parseInt(dpi), renderer, fileList, fileName, 0, Integer.parseInt(toPage),
                        pageIndex);
            if (!StringUtils.equals(fromPage, EMPTY) && !StringUtils.equals(toPage, EMPTY) && StringUtils.equals(pageIndex, EMPTY)) {
                int fromPageImp = Integer.parseInt(fromPage);
                fileList = PdfService.iteration(destination, Integer.parseInt(dpi), renderer, fileList, fileName, --fromPageImp, Integer.parseInt(toPage),
                        pageIndex);
            }
            if (!StringUtils.equals(pageIndex, EMPTY))
                fileList = iteration(destination, Integer.parseInt(dpi), renderer, fileList, fileName, 0, 0, pageIndex);

            doc.close();
            return fileList;
        }
        throw new Exception(FILE_NOT_EXISTS);
    }

    private static List<File> iteration(String destination, Integer dpi, PDFRenderer renderer, List<File> fileList,
                                        String fileName, Integer fromPage, Integer toPage, String pageIndex)
            throws IOException {
        if (!pageIndex.equals(EMPTY)) {
            final String[] arrSplit = pageIndex.split(COMMA);
            for (String anArrSplit : arrSplit) {
                int val = Integer.parseInt(anArrSplit);
                fileCreation(destination, dpi, renderer, fileList, fileName, --val);
            }
        } else {
            for (int i = fromPage; i < toPage; i++) {
                // default image files path: original file path
                // if necessary, file.getParent() + "/" => another path
                fileCreation(destination, dpi, renderer, fileList, fileName, i);
            }
        }
        return fileList;
    }

    private static void fileCreation(String destination, Integer dpi, PDFRenderer renderer, List<File> fileList,
                                     String fileName, Integer i) throws IOException {
        File fileTemp = new File(destination + fileName + UNDERSCORE +
                RandomStringUtils.randomAlphanumeric(15).toUpperCase() + PNG_EXTENSION); // jpg or png
        BufferedImage image = renderer.renderImageWithDPI(i, dpi);
        // if necessary, change 200 into another integer.
        ImageIO.write(image, PNG, fileTemp); // JPEG or PNG
        fileList.add(fileTemp);
    }
}

