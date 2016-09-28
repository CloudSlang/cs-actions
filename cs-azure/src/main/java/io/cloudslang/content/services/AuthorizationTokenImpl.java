package io.cloudslang.content.services;

import io.cloudslang.content.utils.DateUtilities;
import io.cloudslang.content.utils.NumberUtilities;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Created by victor on 28.09.2016.
 */
public class AuthorizationTokenImpl {

    @NotNull
    public static String getToken(@NotNull final String identifier, @NotNull final String primaryOrSecondaryKey, @NotNull final String expiryOffset) {
        final int offset = NumberUtilities.toInteger(expiryOffset);
        final Date expiryDate = DateUtilities.getDateWithOffset(offset);
        final String stringDate = "2014-08-04T22:03:00.0000000Z";
        final Mac sha512Hmac = HmacUtils.getHmacSha512(primaryOrSecondaryKey.getBytes(StandardCharsets.UTF_8));
        final String dataToSign = String.format("%s\n%s", identifier, stringDate);
        final byte[] encodedString = Base64.encodeBase64(sha512Hmac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8)));
        return String.format("SharedAccessSignature uid=%s&ex=%s&sn=%s", identifier, stringDate, new String(encodedString, StandardCharsets.UTF_8));
    }
}
