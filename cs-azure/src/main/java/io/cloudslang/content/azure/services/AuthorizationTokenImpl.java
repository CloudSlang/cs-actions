package io.cloudslang.content.azure.services;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import io.cloudslang.content.azure.utils.DateUtilities;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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


    @NotNull
    public static AuthenticationResult getToken(@NotNull final String username, @NotNull final String password, @NotNull final String authority,
                                                @NotNull final String clientId, @NotNull final String resource) throws Exception {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        final AuthenticationContext context = new AuthenticationContext(authority, false, service);
        final Future<AuthenticationResult> future = context.acquireToken(resource, clientId, username, password, null);
        service.shutdown();
        return future.get();
    }
}