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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.cloudslang.content.tesseract.utils.Constants.TESSDATA_ZIP;
import static io.cloudslang.content.tesseract.utils.Constants.TESSERACT_DATA_ERROR;

public class ConfigService {

    public static String copyConfigFiles(String toPath) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        final String jarFolder;
        ZipFile zipFile = null;
        try {
            stream = ConfigService.class.getResourceAsStream(TESSDATA_ZIP);
            if (stream == null) {
                throw new Exception(TESSERACT_DATA_ERROR);
            }

            int readBytes;
            final byte[] buffer = new byte[4096];
            final Path configFolder = Paths.get(toPath);
            if (!Files.exists(configFolder)) {
                Files.createDirectories(configFolder);
            }
            jarFolder = configFolder.toFile().getPath().replace('\\', File.separatorChar)
                    + File.separatorChar;

            resStreamOut = new FileOutputStream(jarFolder + TESSDATA_ZIP);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }

            zipFile = new ZipFile(jarFolder + TESSDATA_ZIP);
            zipFile.extractAll(jarFolder);
        } finally {
            if (stream != null) stream.close();
            if (resStreamOut != null) resStreamOut.close();
            if (zipFile != null) Files.delete(zipFile.getFile().toPath());
        }

        return Paths.get(jarFolder).toRealPath().toString();
    }
}
