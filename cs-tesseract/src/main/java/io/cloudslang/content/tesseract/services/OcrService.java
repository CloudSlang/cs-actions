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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.recognition.software.jdeskew.ImageDeskew;
import net.lingala.zip4j.core.ZipFile;
import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.lept;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import static io.cloudslang.content.tesseract.utils.Constants.ENG;
import static io.cloudslang.content.tesseract.utils.Constants.MINIMUM_DESKEW_THRESHOLD;
import static io.cloudslang.content.tesseract.utils.Constants.TESSDATA;
import static io.cloudslang.content.tesseract.utils.Constants.TESSDATA_ZIP;
import static io.cloudslang.content.tesseract.utils.Constants.TESSERACT_DATA_ERROR;
import static io.cloudslang.content.tesseract.utils.Constants.TESSERACT_INITIALIZE_ERROR;
import static io.cloudslang.content.tesseract.utils.Constants.TESSERACT_PARSE_ERROR;
import static io.cloudslang.content.tesseract.utils.Constants.TEXT_BLOCK;
import static io.cloudslang.content.tesseract.utils.Constants.UTF_8;
import static java.lang.Boolean.parseBoolean;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.bytedeco.javacpp.lept.L_CLONE;
import static org.bytedeco.javacpp.lept.boxDestroy;
import static org.bytedeco.javacpp.lept.boxaDestroy;
import static org.bytedeco.javacpp.lept.boxaGetBox;
import static org.bytedeco.javacpp.lept.boxaGetCount;
import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.bytedeco.javacpp.tesseract.PSM_AUTO_OSD;
import static org.bytedeco.javacpp.tesseract.RIL_BLOCK;
import static org.bytedeco.javacpp.tesseract.TessBaseAPI;
import static org.bytedeco.javacpp.tesseract.TessBaseAPIGetComponentImages;
import static org.bytedeco.javacpp.tesseract.TessBaseAPIGetUTF8Text;
import static org.bytedeco.javacpp.tesseract.TessBaseAPIInit3;
import static org.bytedeco.javacpp.tesseract.TessBaseAPISetImage2;
import static org.bytedeco.javacpp.tesseract.TessBaseAPISetPageSegMode;
import static org.bytedeco.javacpp.tesseract.TessBaseAPISetRectangle;

public class OcrService {

    public static String extractTextFromImage(String filePath, String dataPath, String language, String textBlocks, String deskew) throws Exception {
        String tempPathDirectory = null;
        String tempImagePath = null;
        String result;
        final TessBaseAPI api = new TessBaseAPI();

        try {
            //Initialize Tesseract OCR
            if (isEmpty(dataPath)) {
                tempPathDirectory = getTempResourcePath();
                if (TessBaseAPIInit3(api, tempPathDirectory, ENG) != 0) {
                    throw new Exception(TESSERACT_INITIALIZE_ERROR);
                }
            } else {
                if (TessBaseAPIInit3(api, dataPath, language) != 0) {
                    throw new Exception(TESSERACT_INITIALIZE_ERROR);
                }
            }

            final lept.PIX image;
            if (parseBoolean(deskew)) {
                tempImagePath = deskewImage(filePath);
                image = pixRead(tempImagePath);
            } else {
                image = pixRead(filePath);
            }
            // Open input image with leptonica library
            TessBaseAPISetImage2(api, image);
            pixDestroy(image);

            if (parseBoolean(textBlocks)) {
                result = extractBlocks(api);
            } else {
                result = extractAllText(api);
            }

            return result;
        } finally {
            if (tempPathDirectory != null) {
                FileUtils.forceDelete(new File(tempPathDirectory));
            }
            if (tempImagePath != null) {
                FileUtils.forceDelete(new File(tempImagePath));
            }
            api.End();
        }
    }

    private static String extractAllText(TessBaseAPI api) throws Exception {
        final BytePointer outText;
        final String result;
        outText = api.GetUTF8Text();
        if (outText == null) {
            throw new Exception(TESSERACT_PARSE_ERROR);
        }
        result = outText.getString();
        outText.deallocate();
        return result;
    }

    private static String extractBlocks(TessBaseAPI api) throws UnsupportedEncodingException {
        TessBaseAPISetPageSegMode(api, PSM_AUTO_OSD);
        lept.BOXA boxes = TessBaseAPIGetComponentImages(api, RIL_BLOCK, true, (PointerPointer) null, null);
        final int boxCount = boxaGetCount(boxes);

        JsonObject outputJson = new JsonObject();
        for (int i = 0; i < boxCount; i++) {
            lept.BOX box = boxaGetBox(boxes, i, L_CLONE);
            if (box == null) {
                continue;
            }
            TessBaseAPISetRectangle(api, box.x(), box.y(), box.w(), box.h());
            BytePointer utf8Text = TessBaseAPIGetUTF8Text(api);
            String ocrResult = utf8Text.getString(UTF_8);

            outputJson = buildOutputJson(outputJson, ocrResult, i);
            boxDestroy(box);
            utf8Text.deallocate();
        }
        boxaDestroy(boxes);
        if (boxCount == 0) {
            throw new UnsupportedEncodingException("Failed to extract text blocks (Empty page), check text orientation or check if text exists.");
        }
        return new GsonBuilder().setPrettyPrinting().create().toJson(outputJson);
    }

    private static JsonObject buildOutputJson(JsonObject outputJson, String textBlock, int i) {
        outputJson.addProperty(TEXT_BLOCK + i, textBlock);
        return outputJson;
    }

    private static String getTempResourcePath() throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        final String jarFolder;

        try {
            stream = OcrService.class.getResourceAsStream(TESSDATA_ZIP);
            if (stream == null) {
                throw new Exception(TESSERACT_DATA_ERROR);
            }

            int readBytes;
            final byte[] buffer = new byte[4096];
            final Path tempDir = Files.createTempDirectory(TESSDATA);
            jarFolder = tempDir.toFile().getPath().replace('\\', File.separatorChar)
                    + File.separatorChar;

            resStreamOut = new FileOutputStream(jarFolder + TESSDATA_ZIP);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }

            new ZipFile(jarFolder + TESSDATA_ZIP).extractAll(jarFolder);
        } finally {
            if (stream != null) stream.close();
            if (resStreamOut != null) resStreamOut.close();
        }

        return Paths.get(jarFolder).toRealPath().toString();
    }

    private static String deskewImage(String filePath) throws IOException {
        File imageFile = new File(filePath);
        BufferedImage bi = ImageIO.read(imageFile);
        ImageDeskew id = new ImageDeskew(bi);

        double imageSkewAngle = id.getSkewAngle(); // determine skew angle
        if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
            bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
        }

        ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);

        if (!iter.hasNext()) {
            throw new RuntimeException("No readers found!");
        }

        ImageReader reader = iter.next();
        String formatName = reader.getFormatName();
        iis.close();

        String tempImage = Files.createTempFile("tempImage", formatName).toString();

        ImageIO.write(bi, formatName, new File(tempImage));

        return tempImage;
    }
}
