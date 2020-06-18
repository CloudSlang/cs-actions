/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.oracle.oci.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIInstanceInputs;
import io.cloudslang.content.oracle.oci.services.InstanceImpl;
import io.cloudslang.content.oracle.oci.utils.Descriptions;
import io.cloudslang.content.oracle.oci.utils.HttpUtils;
import io.cloudslang.content.oracle.oci.utils.InputsValidation;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.Common.EMPTY;
import static io.cloudslang.content.oracle.oci.utils.Constants.CreateInstanceConstants.*;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.Common.*;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.CreateInstance.*;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.ListInstances.COMPARTMENT_OCID_DESC;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.API_VERSION;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Inputs.ListInstancesInputs.COMPARTMENT_OCID;
import static io.cloudslang.content.oracle.oci.utils.Outputs.CreateInstanceOutputs.INSTANCE_ID;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.AVAILABILITY_DOMAIN;
import static io.cloudslang.content.oracle.oci.utils.Outputs.CreateInstanceOutputs.INSTANCE_NAME;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.*;

public class CreateInstance {


    @Action(name = CREATE_INSTANCE_OPERATION_NAME,
            description = CREATE_INSTANCE_OPERATION_NAME_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = INSTANCE_ID, description = INSTANCE_ID_DESC),
                    @Output(value = INSTANCE_NAME, description = INSTANCE_NAME_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = Descriptions.Common.SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = Descriptions.Common.FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = TENANCY_OCID, required = true, description = TENANCY_OCID_DESC) String tenancyOcid,
                                       @Param(value = USER_OCID, required = true, description = USER_OCID_DESC) String userOcid,
                                       @Param(value = FINGER_PRINT, encrypted = true, required = true, description = FINGER_PRINT_DESC) String fingerPrint,
                                       @Param(value = PRIVATE_KEY_DATA, encrypted = true, description = PRIVATE_KEY_DATA_DESC) String privateKeyData,
                                       @Param(value = PRIVATE_KEY_FILE, description = PRIVATE_KEY_FILE_DESC) String privateKeyFile,
                                       @Param(value = COMPARTMENT_OCID, required = true, description = COMPARTMENT_OCID_DESC) String compartmentOcid,
                                       @Param(value = API_VERSION, description = API_VERSION_DESC) String apiVersion,
                                       @Param(value = REGION, required = true, description = REGION_DESC) String region,
                                       @Param(value = AVAILABILITY_DOMAIN, required = true, description = AVAILABILITY_DOMAIN_DESC) String availabilityDomain,
                                       @Param(value = SHAPE, required = true, description = SHAPE_DESC) String shape,
                                       @Param(value = SUBNET_ID, required = true, description = SUBNET_ID_DESC) String subnetId,
                                       @Param(value = SOURCE_TYPE, required = true, description = SOURCE_TYPE_DESC) String sourceType,
                                       @Param(value = IMAGE_ID, description = IMAGE_ID_DESC) String imageId,
                                       @Param(value = BOOT_VOLUME_SIZE_IN_GBS, description = BOOT_VOLUME_SIZE_IN_GBS_DESC) String bootVolumeSizeInGBs,
                                       @Param(value = KMS_KEY_ID, description = KMS_KEY_ID_DESC) String kmsKeyId,
                                       @Param(value = BOOT_VOLUME_ID, description = BOOT_VOLUME_ID_DESC) String bootVolumeId,
                                       @Param(value = DEDICATED_VM_HOST_ID, description = DEDICATED_VM_HOST_ID_DESC) String dedicatedVmHostId,
                                       @Param(value = DISPLAY_NAME, description = DISPLAY_NAME_DESC) String displayName,
                                       @Param(value = DEFINED_TAGS, description = DEFINED_TAGS_DESC) String definedTags,
                                       @Param(value = FREEFORM_TAGS, description = FREEFORM_TAGS_DESC) String freeformTags,
                                       @Param(value = SSH_AUTHORIZED_KEYS, encrypted = true, description = SSH_AUTHORIZED_KEYS_DESC) String sshAuthorizedKeys,
                                       @Param(value = USERDATA, description = USERDATA_DESC) String userdata,
                                       @Param(value = EXTENDED_METADATA, description = EXTENDED_METADATA_DESC) String extendedMetadata,
                                       @Param(value = LAUNCH_MODE, description = LAUNCH_MODE_DESC) String launchMode,
                                       @Param(value = FAULT_DOMAIN, description = FAULT_DOMAIN_DESC) String faultDomain,
                                       @Param(value = IS_PV_ENCRYPTION_IN_TRANSIT_ENABLED, description = IS_PV_ENCRYPTION_IN_TRANSIT_ENABLED_DESC) String isPvEncryptionInTransitEnabled,
                                       @Param(value = IPXE_SCRIPT, description = IPXE_SCRIPT_DESC) String ipxeScript,
                                       @Param(value = VNIC_DISPLAY_NAME, description = VNIC_DISPLAY_NAME_DESC) String vnicDisplayName,
                                       @Param(value = HOSTNAME_LABEL, description = HOSTNAME_LABEL_DESC) String hostnameLabel,
                                       @Param(value = ASSIGN_PUBLIC_IP, description = ASSIGN_PUBLIC_IP_DESC) String assignPublicIp,
                                       @Param(value = VNIC_DEFINED_TAGS, description = VNIC_DEFINED_TAGS_DESC) String vnicDefinedTags,
                                       @Param(value = VNIC_FREEFORM_TAGS, description = VNIC_FREEFORM_TAGS_DESC) String vnicFreeformTags,
                                       @Param(value = NETWORK_SECURITY_GROUP_IDS, description = NETWORK_SECURITY_GROUP_IDS_DESC) String networkSecurityGroupIds,
                                       @Param(value = PRIVATE_IP, description = PRIVATE_IP_DESC) String privateIp,
                                       @Param(value = SKIP_SOURCE_DEST_CHECK, description = SKIP_SOURCE_DEST_CHECK_DESC) String skipSourceDestCheck,
                                       @Param(value = OCPUS, description = OCPUS_DESC) String ocpus,
                                       @Param(value = BOOT_VOLUME_TYPE, description = BOOT_VOLUME_TYPE_DESC) String bootVolumeType,
                                       @Param(value = FIRMWARE, description = FIRMWARE_DESC) String firmware,
                                       @Param(value = IS_CONSISTENT_VOLUME_NAMING_ENABLED, description = IS_CONSISTENT_VOLUME_NAMING_ENABLED_DESC) String isConsistentVolumeNamingEnabled,
                                       @Param(value = NETWORKTYPE, description = NETWORKTYPE_DESC) String networkType,
                                       @Param(value = REMOTE_DATA_VOLUME_TYPE, description = REMOTE_DATA_VOLUME_TYPE_DESC) String remoteDataVolumeType,
                                       @Param(value = IS_MANAGEMENT_DISABLED, description = IS_MANAGEMENT_DISABLED_DESC) String isManagementDisabled,
                                       @Param(value = IS_MONITORING_DISABLED, description = IS_MONITORING_DISABLED_DESC) String isMonitoringDisabled,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = KEYSTORE, description = KEYSTORE_DESC) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = KEYSTORE_PASSWORD_DESC) String keystorePassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSE_CHARACTER_SET_DESC) String responseCharacterSet) {
        apiVersion = defaultIfEmpty(apiVersion, DEFAULT_API_VERSION);
        isManagementDisabled = defaultIfEmpty(isManagementDisabled, EMPTY);
        isMonitoringDisabled = defaultIfEmpty(isMonitoringDisabled, EMPTY);
        assignPublicIp = defaultIfEmpty(assignPublicIp, EMPTY);
        definedTags = defaultIfEmpty(definedTags, EMPTY);
        displayName = defaultIfEmpty(displayName, EMPTY);
        freeformTags = defaultIfEmpty(freeformTags, EMPTY);
        hostnameLabel = defaultIfEmpty(hostnameLabel, EMPTY);
        networkSecurityGroupIds = defaultIfEmpty(networkSecurityGroupIds, EMPTY);
        privateIp = defaultIfEmpty(privateIp, EMPTY);
        skipSourceDestCheck = defaultIfEmpty(skipSourceDestCheck, EMPTY);
        dedicatedVmHostId = defaultIfEmpty(dedicatedVmHostId, EMPTY);
        vnicDefinedTags = defaultIfEmpty(vnicDefinedTags, EMPTY);
        vnicFreeformTags = defaultIfEmpty(vnicFreeformTags, EMPTY);
        vnicDisplayName = defaultIfEmpty(vnicDisplayName, EMPTY);
        extendedMetadata = defaultIfEmpty(extendedMetadata, EMPTY);
        faultDomain = defaultIfEmpty(faultDomain, EMPTY);
        ipxeScript = defaultIfEmpty(ipxeScript, EMPTY);
        isPvEncryptionInTransitEnabled = defaultIfEmpty(isPvEncryptionInTransitEnabled, EMPTY);
        launchMode = defaultIfEmpty(launchMode, EMPTY);
        bootVolumeType = defaultIfEmpty(bootVolumeType, EMPTY);
        firmware = defaultIfEmpty(firmware, EMPTY);
        isConsistentVolumeNamingEnabled = defaultIfEmpty(isConsistentVolumeNamingEnabled, EMPTY);
        networkType = defaultIfEmpty(networkType, EMPTY);
        remoteDataVolumeType = defaultIfEmpty(remoteDataVolumeType, EMPTY);
        ocpus = defaultIfEmpty(ocpus, EMPTY);
        bootVolumeSizeInGBs = defaultIfEmpty(bootVolumeSizeInGBs, EMPTY);
        imageId = defaultIfEmpty(imageId, EMPTY);
        kmsKeyId = defaultIfEmpty(kmsKeyId, EMPTY);
        sourceType = defaultIfEmpty(sourceType, EMPTY);
        bootVolumeId = defaultIfEmpty(bootVolumeId, EMPTY);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        keystore = defaultIfEmpty(keystore, DEFAULT_JAVA_KEYSTORE);
        keystorePassword = defaultIfEmpty(keystorePassword, CHANGEIT);
        connectTimeout = defaultIfEmpty(connectTimeout, CONNECT_TIMEOUT_CONST);
        socketTimeout = defaultIfEmpty(socketTimeout, ZERO);
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_TRUE);
        isPvEncryptionInTransitEnabled = defaultIfEmpty(isPvEncryptionInTransitEnabled, BOOLEAN_FALSE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF8);

        final List<String> exceptionMessage = InputsValidation.verifyCommonInputs(proxyPort, trustAllRoots,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal,privateKeyData,privateKeyFile);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }

        try {
            final Map<String, String> result =
                    InstanceImpl.createInstance(OCIInstanceInputs.builder()
                            .isManagementDisabled(isManagementDisabled)
                            .isMonitoringDisabled(isMonitoringDisabled)
                            .assignPublicIp(assignPublicIp)
                            .definedTags(definedTags)
                            .displayName(displayName)
                            .freeformTags(freeformTags)
                            .hostnameLabel(hostnameLabel)
                            .networkSecurityGroupIds(networkSecurityGroupIds)
                            .privateIp(privateIp)
                            .skipSourceDestCheck(skipSourceDestCheck)
                            .subnetId(subnetId)
                            .dedicatedVmHostId(dedicatedVmHostId)
                            .vnicDefinedTags(vnicDefinedTags)
                            .vnicFreeformTags(vnicFreeformTags)
                            .vnicDisplayName(vnicDisplayName)
                            .extendedMetadata(extendedMetadata)
                            .faultDomain(faultDomain)
                            .ipxeScript(ipxeScript)
                            .isPvEncryptionInTransitEnabled(isPvEncryptionInTransitEnabled)
                            .launchMode(launchMode)
                            .bootVolumeType(bootVolumeType)
                            .firmware(firmware)
                            .isConsistentVolumeNamingEnabled(isConsistentVolumeNamingEnabled)
                            .networkType(networkType)
                            .remoteDataVolumeType(remoteDataVolumeType)
                            .shape(shape)
                            .sshAuthorizedKeys(sshAuthorizedKeys)
                            .userdata(userdata)
                            .ocpus(ocpus)
                            .bootVolumeSizeInGBs(bootVolumeSizeInGBs)
                            .imageId(imageId)
                            .kmsKeyId(kmsKeyId)
                            .sourceType(sourceType)
                            .bootVolumeId(bootVolumeId)
                            .commonInputs(OCICommonInputs.builder()
                                    .compartmentOcid(compartmentOcid)
                                    .availabilityDomain(availabilityDomain)
                                    .tenancyOcid(tenancyOcid)
                                    .userOcid(userOcid)
                                    .fingerPrint(fingerPrint)
                                    .privateKeyData(privateKeyData)
                                    .privateKeyFile(privateKeyFile)
                                    .apiVersion(apiVersion)
                                    .region(region)
                                    .proxyHost(proxyHost)
                                    .proxyPort(proxyPort)
                                    .proxyUsername(proxyUsername)
                                    .proxyPassword(proxyPassword)
                                    .trustAllRoots(trustAllRoots)
                                    .x509HostnameVerifier(x509HostnameVerifier)
                                    .trustKeystore(trustKeystore)
                                    .trustPassword(trustPassword)
                                    .keystore(keystore)
                                    .keystorePassword(keystorePassword)
                                    .connectTimeout(connectTimeout)
                                    .socketTimeout(socketTimeout)
                                    .keepAlive(keepAlive)
                                    .connectionsMaxPerRoot(connectionsMaxPerRoute)
                                    .connectionsMaxTotal(connectionsMaxTotal)
                                    .responseCharacterSet(responseCharacterSet)
                                    .build()).build());
            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = HttpUtils.getOperationResults(result, returnMessage, returnMessage, returnMessage);
            Integer statusCode = Integer.parseInt(result.get(STATUS_CODE));

            if (statusCode >= 200 && statusCode < 300) {
                results.put(INSTANCE_ID, JsonPath.read(returnMessage, INSTANCE_ID_JSON_PATH));
                results.put(INSTANCE_NAME, JsonPath.read(returnMessage, INSTANCE_NAME_JSON_PATH));

            } else {
                return HttpUtils.getFailureResults(compartmentOcid, statusCode, returnMessage);
            }
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }
}
