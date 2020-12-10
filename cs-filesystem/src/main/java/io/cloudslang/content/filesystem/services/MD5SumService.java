package io.cloudslang.content.filesystem.services;

import io.cloudslang.content.filesystem.entities.MD5SumInputs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.filesystem.constants.Constants.*;
import static io.cloudslang.content.filesystem.constants.ResultsName.CHECKSUM;

public class MD5SumService {

    public static Map<String, String> execute(MD5SumInputs md5SumInputs) throws NoSuchAlgorithmException, IOException {

        Map<String, String> result = new HashMap<>();

        MessageDigest md5 = MessageDigest.getInstance(MD5);
        FileInputStream fStream = null;

        try {
            fStream = new FileInputStream(md5SumInputs.getSource());

            byte[] buf = new byte[1024];
            int nRead = fStream.read(buf);
            while (nRead > 0) {
                md5.update(buf, 0, nRead);
                nRead = fStream.read(buf);
            }
        } finally {
            if (fStream != null) {
                fStream.close();
            }
        }
        byte[] digest = md5.digest();
        StringBuffer hash = new StringBuffer();
        for (byte b : digest) {
            hash.append(Integer.toHexString((b & 0xFF) | 0x100).toLowerCase().substring(1, 3));
        }

        result.put(RETURN_RESULT, hash.toString());
        result.put(CHECKSUM, hash.toString());
        if (hash.toString().equals(md5SumInputs.getCompareTo()))
            result.put(RETURN_CODE, EQUALS_VALUE);
        else
            result.put(RETURN_CODE, LESS);

        return result;

    }
}
