/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.azure.utils;

import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.azure.utils.AuthorizationInputNames.CLIENT_ID;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.EXPIRY;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.IDENTIFIER;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PASSWORD;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PRIMARY_OR_SECONDARY_KEY;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PORT;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.USERNAME;
import static io.cloudslang.content.azure.utils.Constants.EXCEPTION_INVALID_PROXY;
import static io.cloudslang.content.azure.utils.Constants.EXCEPTION_NULL_EMPTY;
import static io.cloudslang.content.azure.utils.StorageInputNames.BLOB_NAME;
import static io.cloudslang.content.azure.utils.StorageInputNames.CONTAINER_NAME;
import static io.cloudslang.content.azure.utils.StorageInputNames.TIMEOUT;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;

/**
 * Created by victor on 28.09.2016.
 */
public final class InputsValidation {
    @NotNull
    public static List<String> verifyStorageInputs(@Nullable final String accountName, @Nullable final String key, @Nullable final String proxyPort, @Nullable final String timeout) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyNotNullOrEmpty(exceptionMessages, accountName, IDENTIFIER);
        addVerifyNotNullOrEmpty(exceptionMessages, key, PRIMARY_OR_SECONDARY_KEY);
        addVerifyNotNullOrEmpty(exceptionMessages, timeout, TIMEOUT);
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyStorageInputs(@Nullable final String accountName, @Nullable final String key, @Nullable final String containerName, @Nullable final String proxyPort, @Nullable final String timeout) {
        final List<String> exceptionMessages = verifyStorageInputs(accountName, key, proxyPort, timeout);
        addVerifyNotNullOrEmpty(exceptionMessages, containerName, CONTAINER_NAME);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyStorageInputs(@Nullable final String accountName, @Nullable final String key, @Nullable final String containerName, @Nullable final String proxyPort, @Nullable final String blobName, @Nullable final String timeout) {
        final List<String> exceptionMessages = verifyStorageInputs(accountName, key, containerName, proxyPort, timeout);
        addVerifyNotNullOrEmpty(exceptionMessages, blobName, BLOB_NAME);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifySharedAccessInputs(@Nullable final String identifier, @Nullable final String primaryOrSecondaryKey, @Nullable final String expiry) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyNotNullOrEmpty(exceptionMessages, identifier, IDENTIFIER);
        addVerifyNotNullOrEmpty(exceptionMessages, primaryOrSecondaryKey, PRIMARY_OR_SECONDARY_KEY);
        addVerifyNotNullOrEmpty(exceptionMessages, expiry, EXPIRY);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyAuthorizationInputs(@Nullable final String username, @Nullable final String password, @Nullable final String clientId, @NotNull final String proxyPort) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyNotNullOrEmpty(exceptionMessages, username, USERNAME);
        addVerifyNotNullOrEmpty(exceptionMessages, password, PASSWORD);
        addVerifyNotNullOrEmpty(exceptionMessages, clientId, CLIENT_ID);
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyProxyPortInput(@Nullable final String proxyPort) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (StringUtilities.isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyProxy(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (StringUtilities.isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValidIpPort(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PROXY, PROXY_PORT));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (StringUtilities.isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PROXY, PROXY_PORT));
        }
        return exceptions;
    }
}
