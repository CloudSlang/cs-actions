package io.cloudslang.content.azure.services;

import io.cloudslang.content.azure.utils.DateUtilities;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Created by victor on 28.09.2016.
 */
public class AuthorizationTokenImpl {

    @NotNull
    public static String getToken(@NotNull final String identifier, @NotNull final String primaryOrSecondaryKey, @NotNull final Date expiryDate) {
        final Mac sha512Hmac = HmacUtils.getHmacSha512(primaryOrSecondaryKey.getBytes(StandardCharsets.UTF_8));
        final String dataToSign = String.format("%s\n%s", identifier, DateUtilities.formatDate(expiryDate));
        final byte[] encodedBytes = Base64.encodeBase64(sha512Hmac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8)));
        final String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        return String.format("SharedAccessSignature uid=%s&ex=%s&sn=%s", identifier, DateUtilities.formatDate(expiryDate), encodedString);
    }
}