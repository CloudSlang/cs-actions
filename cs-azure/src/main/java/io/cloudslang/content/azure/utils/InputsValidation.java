/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.azure.utils;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
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
import static io.cloudslang.content.azure.utils.Constants.*;
import static io.cloudslang.content.azure.utils.Inputs.CreateVMInputs.*;
import static io.cloudslang.content.azure.utils.StorageInputNames.BLOB_NAME;
import static io.cloudslang.content.azure.utils.StorageInputNames.CONTAINER_NAME;
import static io.cloudslang.content.azure.utils.StorageInputNames.TIMEOUT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.TRUST_ALL_ROOTS;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

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
    public static List<String> verifyCommonInputs(@Nullable final String proxyPort,
                                                  @Nullable final String trust_all_roots) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyProxy(exceptionMessages, proxyPort, HttpClientInputs.PROXY_PORT);
        addVerifyBoolean(exceptionMessages, trust_all_roots, TRUST_ALL_ROOTS);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyComputeInputs(@NotNull List<String> exceptions, @Nullable final String availabilitySetName, @Nullable final String diskType, @Nullable final String adminPassword, @Nullable final String sshPublicKeyName,
                                                   @Nullable final String storageAccount, @Nullable final String storageAccountType,@Nullable final String privateImageName, @Nullable final String publisher,@Nullable final String offer, @Nullable final String sku,
                                                   @Nullable final String diskSize) {


       if(isEmpty(availabilitySetName) && isEmpty(diskType)){
           exceptions.add(String.format(EXCEPTION_NULL_EMPTY_TWO_VALUES, AVAILABILITY_SET_NAME, DISK_TYPE));
       }
        if(isEmpty(adminPassword) && isEmpty(sshPublicKeyName)){
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY_TWO_VALUES, ADMIN_PASSWORD, SSH_PUBLIC_KEY_NAME));
        }
        if(isEmpty(storageAccount) && isEmpty(storageAccountType)){
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY_TWO_VALUES, STORAGE_ACCOUNT, STORAGE_ACCOUNT_TYPE));
        }
        if(isEmpty(privateImageName) && (isEmpty(publisher) || isEmpty(offer) || isEmpty(sku))){
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY_TWO_VALUES, PRIVATE_IMAGE_NAME, (PUBLISHER + COMMA + SPACE +OFFER + COMMA + SPACE + "and " + SPACE + SKU)));
        }

        if (StringUtilities.isEmpty(diskSize)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, DISK_SIZE_IN_GB));
        } else if (!NumberUtilities.isValidInt(diskSize)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, DISK_SIZE_IN_GB));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input,
                                                 @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
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
