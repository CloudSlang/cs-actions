/*
 * Copyright 2024 Open Text
 * This program and the accompanying materials
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



package io.cloudslang.content.azure.actions.compute.virtualmachines;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.azure.entities.AzureCommonInputs;
import io.cloudslang.content.azure.entities.AzureComputeCommonInputs;
import io.cloudslang.content.azure.entities.AzureCreateVMInputs;
import io.cloudslang.content.azure.services.AzureComputeImpl;
import io.cloudslang.content.azure.utils.Constants;
import io.cloudslang.content.constants.ReturnCodes;
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
import static io.cloudslang.content.azure.utils.Constants.Common.*;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.CREATE_VM;
import static io.cloudslang.content.azure.utils.Descriptions.Common.*;
import static io.cloudslang.content.azure.utils.Descriptions.ComputeCommonDescriptions.*;
import static io.cloudslang.content.azure.utils.Descriptions.CreateStreamingJob.NIC_RESOURCE_GROUP_NAME_DESC;
import static io.cloudslang.content.azure.utils.Descriptions.CreateStreamingJob.RESOURCE_GROUP_NAME_DESC;
import static io.cloudslang.content.azure.utils.Descriptions.CreateVMDescriptions.*;
import static io.cloudslang.content.azure.utils.HttpUtils.getFailureResults;
import static io.cloudslang.content.azure.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.azure.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.azure.utils.Inputs.ComputeCommonInputs.*;
import static io.cloudslang.content.azure.utils.Inputs.CreateVMInputs.*;
import static io.cloudslang.content.azure.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.azure.utils.InputsValidation.verifyComputeInputs;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CreateVM {

    @Action(name = CREATE_VM,
            description = CREATE_VM_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = SUBSCRIPTION_ID, required = true, description = SUBSCRIPTION_ID_DESC) String subscriptionId,
                                       @Param(value = AZURE_PROTOCOL, required = true, description = AZURE_PROTOCOL_DESC) String azureProtocol,
                                       @Param(value = AZURE_HOST, required = true, description = AZURE_HOST_DESC) String azureHost,
                                       @Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC, encrypted = true) String authToken,
                                       @Param(value = API_VERSION, description = COMPUTE_API_VERSION_DESC) String apiVersion,
                                       @Param(value = LOCATION, required = true, description = COMPUTE_LOCATION_DESC) String location,
                                       @Param(value = RESOURCE_GROUP_NAME, required = true, description = RESOURCE_GROUP_NAME_DESC) String resourceGroupName,
                                       @Param(value = NIC_RESOURCE_GROUP_NAME, description = NIC_RESOURCE_GROUP_NAME_DESC) String nicResourceGroupName,
                                       @Param(value = NIC_NAME, required = true, description = NIC_NAME_DESC) String nicName,
                                       @Param(value = VM_SIZE, required = true, description = VM_SIZE_DESC) String vmSize,
                                       @Param(value = VM_NAME, required = true, description = VM_NAME_DESC) String vmName,
                                       @Param(value = ADMIN_USERNAME, required = true, description = ADMIN_USERNAME_DESC) String adminUsername,

                                       @Param(value = AVAILABILITY_SET_NAME, description = AVAILABILITY_SET_NAME_DESC) String availabilitySetName,
                                       @Param(value = DISK_TYPE, description = DISK_TYPE_DESC) String diskType,
                                       @Param(value = ADMIN_PASSWORD, description = ADMIN_PASSWORD_DESC, encrypted = true) String adminPassword,
                                       @Param(value = SSH_PUBLIC_KEY_NAME, description = SSH_PUBLIC_KEY_NAME_DESC) String sshPublicKeyName,
                                       @Param(value = STORAGE_ACCOUNT, description = STORAGE_ACCOUNT_DESC) String storageAccount,
                                       @Param(value = STORAGE_ACCOUNT_TYPE, description = STORAGE_ACCOUNT_TYPE_DESC) String storageAccountType,
                                       @Param(value = PUBLISHER, description = PUBLISHER_DESC) String publisher,
                                       @Param(value = OFFER, description = OFFER_DESC) String offer,
                                       @Param(value = SKU, description = SKU_DESC) String sku,
                                       @Param(value = IMAGE_VERSION, description = IMAGE_VERSION_DESC) String imageVersion,
                                       @Param(value = PLAN, description = PLAN_DESC) String plan,
                                       @Param(value = PRIVATE_IMAGE_NAME, description = PRIVATE_IMAGE_NAME_DESC) String privateImageName,
                                       @Param(value = DATA_DISK_NAME, description = DATA_DISK_NAME_DESC) String dataDiskName,
                                       @Param(value = OS_DISK_NAME, description = OS_DISK_NAME_DESC) String osDiskName,
                                       @Param(value = DISK_SIZE_IN_GB, description = DISK_SIZE_IN_GB_DESC) String diskSizeInGB,
                                       @Param(value = TAG_KEY_LIST, description = TAG_KEY_LIST_DESC) String tagKeyList,
                                       @Param(value = TAG_VALUE_LIST, description = TAG_VALUE_LIST_DESC) String tagValueList,

                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword) {

        apiVersion = defaultIfEmpty(apiVersion, DEFAULT_COMPUTE_API_VERSION);
        azureProtocol = defaultIfEmpty(azureProtocol, HTTPS);
        azureHost = defaultIfEmpty(azureHost, AZURE_HOST_CONST);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, Constants.DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        plan = defaultIfEmpty(plan, BOOLEAN_FALSE);
        imageVersion = defaultIfEmpty(imageVersion, LATEST);
        diskSizeInGB = defaultIfEmpty(diskSizeInGB, DEFAULT_DISK_SIZE);
        dataDiskName = defaultIfEmpty(dataDiskName, vmName);
        osDiskName = defaultIfEmpty(osDiskName, vmName);

        if(nicResourceGroupName.isEmpty())
        {
            nicResourceGroupName = resourceGroupName;
        }

        List<String> exceptionMessage = verifyCommonInputs(proxyPort, trustAllRoots);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }
        exceptionMessage = verifyComputeInputs(exceptionMessage, availabilitySetName, diskType, adminPassword, sshPublicKeyName,
                storageAccount, storageAccountType, privateImageName, publisher, offer, sku, diskSizeInGB);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }
        try {
            final Map<String, String> result = AzureComputeImpl.createVM(AzureCreateVMInputs.builder().azureComputeCommonInputs(
                    AzureComputeCommonInputs.builder().azureHost(azureHost)
                            .azureProtocol(azureProtocol)
                            .vmName(vmName)
                            .azureCommonInputs(AzureCommonInputs.builder()
                                    .location(location)
                                    .apiVersion(apiVersion)
                                    .authToken(authToken)
                                    .subscriptionId(subscriptionId)
                                    .resourceGroupName(resourceGroupName)
                                    .proxyPort(proxyPort)
                                    .proxyHost(proxyHost)
                                    .proxyUsername(proxyUsername)
                                    .proxyPassword(proxyPassword)
                                    .trustAllRoots(trustAllRoots)
                                    .x509HostnameVerifier(x509HostnameVerifier)
                                    .trustKeystore(trustKeystore)
                                    .trustPassword(trustPassword).build())
                            .build()).adminUsername(adminUsername)
                    .adminPassword(adminPassword)
                    .availabilitySetName(availabilitySetName)
                    .diskType(diskType)
                    .tagValueList(tagValueList)
                    .tagKeyList(tagKeyList)
                    .sshPublicKeyName(sshPublicKeyName)
                    .nicName(nicName)
                    .vmSize(vmSize)
                    .imageVersion(imageVersion)
                    .diskSizeInGB(diskSizeInGB)
                    .storageAccount(storageAccount)
                    .storageAccountType(storageAccountType)
                    .publisher(publisher)
                    .offer(offer)
                    .sku(sku)
                    .plan(plan)
                    .privateImageName(privateImageName)
                    .dataDiskName(dataDiskName)
                    .osDiskName(osDiskName)
                    .nicResourceGroupName(nicResourceGroupName)
                    .build());

            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
            final int statusCode = Integer.parseInt(result.get(STATUS_CODE));
            if (!(statusCode >= 200 && statusCode < 300)) {
                return getFailureResults(subscriptionId, statusCode, returnMessage);
            }

            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
