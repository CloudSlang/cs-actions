package io.cloudslang.content.utilities.services.base64decoder;

import io.cloudslang.content.utilities.entities.base64decoder.Base64DecoderInputs;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64DecoderImpl {

    @NotNull
    public static String writeBytesToFile(Base64DecoderInputs base64DecoderInputs) throws IOException {
        byte[] data = Base64.decodeBase64(base64DecoderInputs.getContentBytes());
        try (OutputStream stream = new FileOutputStream(base64DecoderInputs.getFilePath())) {
            stream.write(data);
        }
        return base64DecoderInputs.getFilePath();
    }
}
