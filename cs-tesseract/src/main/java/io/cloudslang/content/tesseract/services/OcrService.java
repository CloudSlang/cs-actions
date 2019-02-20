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

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static io.cloudslang.content.tesseract.utils.Constants.*;
import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.bytedeco.javacpp.tesseract.TessBaseAPI;

public class OcrService {
    public static String extractText(String filePath) throws URISyntaxException {
        if (!new File(filePath).exists())
            throw new RuntimeException(FILE_NOT_EXISTS);

        final BytePointer outText;
        final TessBaseAPI api = new TessBaseAPI();
        final String result;

        //Initialize Tesseract OCR
        if (api.Init(String.valueOf(Paths.get(OcrService.class.getResource(TESSDATA).toURI())
                .toAbsolutePath()), ENG) != 0) {
            throw new RuntimeException(TESSERACT_INITIALIZE_ERROR);
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
    }
}
