package io.cloudslang.content.oracle.oci.actions.volumes;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIAttachVolumeInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.oci.services.VolumeImpl;
import io.cloudslang.content.oracle.oci.utils.Descriptions;
import io.cloudslang.content.oracle.oci.utils.HttpUtils;
import io.cloudslang.content.oracle.oci.utils.InputsValidation;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.AttachVolumeConstants.ATTACH_VOLUME_OPERATION_NAME;
import static io.cloudslang.content.oracle.oci.utils.Constants.AttachVolumeConstants.VOLUME_ATTACHMENT_ID_JSON_PATH;
import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.GetVolumeAttachmentDetailsConstants.VOLUME_ATTACHMENT_LIFECYCLE_STATE_JSON_PATH;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.AttachVolume.*;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.Common.*;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.ListInstances.COMPARTMENT_OCID_DESC;
import static io.cloudslang.content.oracle.oci.utils.Inputs.AttachVolumeInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.API_VERSION;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Inputs.ListInstancesInputs.COMPARTMENT_OCID;
import static io.cloudslang.content.oracle.oci.utils.Outputs.AttachVolumeOutputs.VOLUME_ATTACHMENT_STATE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class AttachVolume {

    @Action(name = ATTACH_VOLUME_OPERATION_NAME,
            description = ATTACH_VOLUME_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = VOLUME_ATTACHMENT_ID, description = VOLUME_ATTACHMENT_ID_DESC),
                    @Output(value = VOLUME_ATTACHMENT_STATE, description = VOLUME_ATTACHMENT_STATE_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED,
                            description = Descriptions.Common.SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR,
                            description = Descriptions.Common.FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = TENANCY_OCID, required = true, description = TENANCY_OCID_DESC) String tenancyOcid,
                                       @Param(value = USER_OCID, required = true, description = USER_OCID_DESC) String userOcid,
                                       @Param(value = FINGER_PRINT, encrypted = true, required = true, description = FINGER_PRINT_DESC) String fingerPrint,
                                       @Param(value = PRIVATE_KEY_DATA, encrypted = true, description = PRIVATE_KEY_DATA_DESC) String privateKeyData,
                                       @Param(value = PRIVATE_KEY_FILE, description = PRIVATE_KEY_FILE_DESC) String privateKeyFile,
                                       @Param(value = COMPARTMENT_OCID, required = true, description = COMPARTMENT_OCID_DESC) String compartmentOcid,
                                       @Param(value = API_VERSION, description = API_VERSION_DESC) String apiVersion,
                                       @Param(value = REGION, required = true, description = REGION_DESC) String region,
                                       @Param(value = INSTANCE_ID, required = true, description = INSTANCE_ID_DESC) String instanceId,
                                       @Param(value = VOLUME_ID, required = true, description = VOLUME_ID_DESC) String volumeId,
                                       @Param(value = VOLUME_TYPE, required = true, description = VOLUME_TYPE_DESC) String volumeType,
                                       @Param(value = DEVICE_NAME, description = DEVICE_NAME_DESC) String deviceName,
                                       @Param(value = DISPLAY_NAME, description = DISPLAY_NAME_DESC) String displayName,
                                       @Param(value = IS_READ_ONLY, description = IS_READ_ONLY_DESC) String isReadOnly,
                                       @Param(value = IS_SHAREABLE, description = IS_SHAREABLE_DESC) String isShareable,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword) {
        deviceName = defaultIfEmpty(deviceName, EMPTY);
        displayName = defaultIfEmpty(displayName, EMPTY);
        isReadOnly = defaultIfEmpty(isReadOnly, EMPTY);
        isShareable = defaultIfEmpty(isShareable, EMPTY);
        apiVersion = defaultIfEmpty(apiVersion, DEFAULT_API_VERSION);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);

        final List<String> exceptionMessage = InputsValidation.verifyCommonInputs(privateKeyData, privateKeyFile, proxyPort);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }
        try {
            final Map<String, String> result =
                    VolumeImpl.attachVolume(OCIAttachVolumeInputs.builder()
                            .volumeType(volumeType)
                            .deviceName(deviceName)
                            .displayName(displayName)
                            .isReadOnly(isReadOnly)
                            .isShareable(isShareable)
                            .commonInputs(
                                    OCICommonInputs.builder()
                                            .tenancyOcid(tenancyOcid)
                                            .compartmentOcid(compartmentOcid)
                                            .userOcid(userOcid)
                                            .fingerPrint(fingerPrint)
                                            .privateKeyData(privateKeyData)
                                            .privateKeyFile(privateKeyFile)
                                            .apiVersion(apiVersion)
                                            .region(region)
                                            .instanceId(instanceId)
                                            .volumeId(volumeId)
                                            .proxyHost(proxyHost)
                                            .proxyPort(proxyPort)
                                            .proxyUsername(proxyUsername)
                                            .proxyPassword(proxyPassword)
                                            .build()).build());
            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = HttpUtils.getOperationResults(result, returnMessage, returnMessage, returnMessage);
            Integer statusCode = Integer.parseInt(result.get(STATUS_CODE));
            if (statusCode >= 200 && statusCode < 300) {
                results.put(VOLUME_ATTACHMENT_ID, JsonPath.read(returnMessage, VOLUME_ATTACHMENT_ID_JSON_PATH));
                results.put(VOLUME_ATTACHMENT_STATE, JsonPath.read(returnMessage, VOLUME_ATTACHMENT_LIFECYCLE_STATE_JSON_PATH));
            } else {
                return HttpUtils.getFailureResults(compartmentOcid, statusCode, returnMessage);
            }
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }

}
