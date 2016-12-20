/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.azure.actions.storage;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.azure.entities.StorageInputs;
import io.cloudslang.content.azure.services.StorageServiceImpl;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_HOST;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PASSWORD;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PORT;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_USERNAME;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_PROXY_PORT;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_TIMEOUT;
import static io.cloudslang.content.azure.utils.Constants.NEW_LINE;
import static io.cloudslang.content.azure.utils.InputsValidation.verifyStorageInputs;
import static io.cloudslang.content.azure.utils.StorageInputNames.STORAGE_ACCOUNT;
import static io.cloudslang.content.azure.utils.StorageInputNames.CONTAINER_NAME;
import static io.cloudslang.content.azure.utils.StorageInputNames.KEY;
import static io.cloudslang.content.azure.utils.StorageInputNames.TIMEOUT;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by victor on 31.10.2016.
 */
public class DeleteContainer {
    /**
     * @param storageAccount Azure storage account name
     * @param key            Azure account key
     * @param containerName  The container's name you want to delete
     * @param proxyHost      Proxy server used to access the web site
     * @param proxyPort      Proxy server port
     *                       Default: '8080'
     * @param proxyUsername  User name used when connecting to the proxy
     * @param proxyPassword  The proxy server password associated with the <proxyUsername> input value
     * @param timeout        The time for the operation to wait for a response
     * @return The container name if it succeeded
     */
    @Action(name = "Delete Container from a Storage Account",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(@Param(value = STORAGE_ACCOUNT, required = true) String storageAccount,
                                       @Param(value = KEY, required = true, encrypted = true) String key,
                                       @Param(value = CONTAINER_NAME, required = true) String containerName,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = TIMEOUT) String timeout) {
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        timeout = defaultIfEmpty(timeout, DEFAULT_TIMEOUT);

        final List<String> exceptionMessages = verifyStorageInputs(storageAccount, key, containerName, proxyPort, timeout);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        final int proxyPortInt = NumberUtilities.toInteger(proxyPort);
        final int timeoutInt = NumberUtilities.toInteger(timeout);

        final StorageInputs storageInputs = StorageInputs.builder()
                .storageAccount(storageAccount)
                .key(key)
                .containerName(containerName)
                .proxyHost(proxyHost)
                .proxyPort(proxyPortInt)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword)
                .timeout(timeoutInt)
                .build();

        try {
            return getSuccessResultsMap(StorageServiceImpl.deleteContainer(storageInputs));
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
