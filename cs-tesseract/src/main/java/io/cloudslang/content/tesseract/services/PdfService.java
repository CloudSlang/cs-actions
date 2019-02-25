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
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.tesseract.utils.Constants.FILE_NOT_EXISTS;
import static java.util.Objects.requireNonNull;

public class PdfService {
    private static StringBuffer result = new StringBuffer();

    public static String imageConvert(String sourcePath, String dataPath, String lang, String dpi) throws Exception {
        String destination = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator)) + File.separator;

        if (!sourcePath.equals("")) {
            String[] files = sourcePath.split(",");
            for (String file : files) {
                File pdf = new File(file);
                for (File image : requireNonNull(convertPdfToImage(pdf, destination, Integer.parseInt(dpi)))) {
                    result.append(OcrService.extractText(image.getAbsolutePath(), dataPath, lang));
                    FileUtils.forceDelete(image);
                }
            }
        }
        return result.toString();
    }

    private static List<File> convertPdfToImage(File file, String destination, Integer dpi) throws Exception {
        if (file.exists()) {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            List<File> fileList = new ArrayList<>();

            String fileName = file.getName().replace(".pdf", "");

            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                // default image files path: original file path
                // if necessary, file.getParent() + "/" => another path
                File fileTemp = new File(destination + fileName + "_" + RandomStringUtils.randomAlphanumeric(15).toUpperCase() + ".png"); // jpg or png
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                // if necessary, change 200 into another integer.
                ImageIO.write(image, "PNG", fileTemp); // JPEG or PNG
                fileList.add(fileTemp);
            }
            doc.close();
            return fileList;
        }
        throw new RuntimeException(FILE_NOT_EXISTS);
    }
}

