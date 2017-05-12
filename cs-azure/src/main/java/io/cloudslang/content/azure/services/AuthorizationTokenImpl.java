/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.azure.services;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import io.cloudslang.content.azure.entities.AuthorizationTokenInputs;
import io.cloudslang.content.azure.utils.DateUtilities;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Mac;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static io.cloudslang.content.azure.utils.Constants.SHARED_ACCESS_SIGNATURE;
import static io.cloudslang.content.azure.utils.HttpUtils.getProxy;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by victor on 28.09.2016.
 */
public class AuthorizationTokenImpl {

    @NotNull
    public static String getToken(@NotNull final String identifier, @NotNull final String primaryOrSecondaryKey, @NotNull final Date expiryDate) {
        final Mac sha512Hmac = HmacUtils.getHmacSha512(primaryOrSecondaryKey.getBytes(UTF_8));
        final String dataToSign = String.format("%s\n%s", identifier, DateUtilities.formatDate(expiryDate));
        final byte[] encodedBytes = Base64.encodeBase64(sha512Hmac.doFinal(dataToSign.getBytes(UTF_8)));
        final String encodedString = new String(encodedBytes, UTF_8);
        return String.format(SHARED_ACCESS_SIGNATURE, identifier, DateUtilities.formatDate(expiryDate), encodedString);
    }

    @NotNull
    public static AuthenticationResult getToken(@NotNull final AuthorizationTokenInputs inputs) throws Exception {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        final AuthenticationContext context = new AuthenticationContext(inputs.getAuthority(), false, service);
        context.setProxy(getProxy(inputs.getProxyHost(), inputs.getProxyPort(), inputs.getProxyUsername(), inputs.getProxyPassword()));
        final Future<AuthenticationResult> future = context.acquireToken(inputs.getResource(), inputs.getClientId(), inputs.getUsername(), inputs.getPassword(), null);
        service.shutdown();
        return future.get();
    }
}
