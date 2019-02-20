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

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.cloudslang.content.tesseract.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.bytedeco.javacpp.tesseract.TessBaseAPI;

public class OcrService {
    public static String extractText(String filePath, String dataPath, String language) throws IOException, ZipException {
        String tempPathDirectory = null;

        try {
            if (!new File(filePath).exists())
                throw new RuntimeException(FILE_NOT_EXISTS);

            final BytePointer outText;
            final TessBaseAPI api = new TessBaseAPI();
            final String result;

            //Initialize Tesseract OCR
            if (isEmpty(dataPath)) {
                tempPathDirectory = getTempResourcePath();
                if (api.Init(tempPathDirectory, ENG) != 0) {
                    throw new RuntimeException(TESSERACT_INITIALIZE_ERROR);
                }
            } else {
                if (api.Init(dataPath, language) != 0) {
                    throw new RuntimeException(TESSERACT_INITIALIZE_ERROR);
                }
            }

            // Open input image with leptonica library
            final lept.PIX image = pixRead(filePath);
            api.SetImage(image);

            // Get OCR result
            outText = api.GetUTF8Text();
            if (outText == null)
                throw new RuntimeException(TESSERACT_PARSE_ERROR);
            result = outText.getString();

            // Destroy used object and release memory
            api.End();
            outText.deallocate();
            pixDestroy(image);

            return result;
        } finally {
            if (tempPathDirectory != null) {
                FileUtils.forceDelete(new File(tempPathDirectory));
            }
        }
    }

    private static String getTempResourcePath() throws IOException, ZipException {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        final String jarFolder;

        try {
            stream = OcrService.class.getResourceAsStream(TESSDATA_ZIP);
            if (stream == null) {
                throw new RuntimeException(TESSERACT_DATA_ERROR);
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
}
