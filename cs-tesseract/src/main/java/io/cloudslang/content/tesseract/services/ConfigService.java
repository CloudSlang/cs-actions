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
