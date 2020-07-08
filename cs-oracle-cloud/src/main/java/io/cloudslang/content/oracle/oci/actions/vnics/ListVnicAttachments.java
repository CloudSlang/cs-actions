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

package io.cloudslang.content.oracle.oci.actions.vnics;

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
import io.cloudslang.content.oracle.oci.services.VnicImpl;
import io.cloudslang.content.oracle.oci.utils.Constants;
import io.cloudslang.content.oracle.oci.utils.Descriptions;
import io.cloudslang.content.oracle.oci.utils.HttpUtils;
import io.cloudslang.content.oracle.oci.utils.InputsValidation;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.CreateInstanceConstants.AVAILABILITY_DOMAIN;
import static io.cloudslang.content.oracle.oci.utils.Constants.ListVnicAttachmentsConstants.*;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.Common.*;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.CreateInstance.AVAILABILITY_DOMAIN_DESC;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.ListInstances.COMPARTMENT_OCID_DESC;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.ListVnicAttachments.*;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.API_VERSION;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Inputs.ListInstancesInputs.COMPARTMENT_OCID;
import static io.cloudslang.content.oracle.oci.utils.Outputs.ListVnicAttachmentsOutputs.VNIC_ATTACHMENTS_LIST;
import static io.cloudslang.content.oracle.oci.utils.Outputs.ListVnicAttachmentsOutputs.VNIC_LIST;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ListVnicAttachments {

    @Action(name = LIST_VNIC_ATTACHMENTS_OPERATION_NAME,
            description = LIST_VNIC_ATTACHMENTS_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = VNIC_LIST, description = VNIC_LIST_DESC),
                    @Output(value = VNIC_ATTACHMENTS_LIST, description = VNIC_ATTACHMENTS_LIST_DESC),
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
                                       @Param(value = AVAILABILITY_DOMAIN, description = AVAILABILITY_DOMAIN_DESC) String availabilityDomain,
                                       @Param(value = INSTANCE_ID, description = INSTANCE_ID_DESC) String instanceId,
                                       @Param(value = VNIC_ID, description = VNIC_ID_DESC) String vnicId,
                                       @Param(value = LIMIT, description = LIMIT_DESC) String limit,
                                       @Param(value = PAGE, description = PAGE_DESC) String page,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword) {
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
                    VnicImpl.listVnicAttachments(OCICommonInputs.builder()
                            .tenancyOcid(tenancyOcid)
                            .compartmentOcid(compartmentOcid)
                            .userOcid(userOcid)
                            .fingerPrint(fingerPrint)
                            .privateKeyData(privateKeyData)
                            .privateKeyFile(privateKeyFile)
                            .apiVersion(apiVersion)
                            .region(region)
                            .instanceId(instanceId)
                            .availabilityDomain(availabilityDomain)
                            .vnicId(vnicId)
                            .page(page)
                            .limit(limit)
                            .proxyHost(proxyHost)
                            .proxyPort(proxyPort)
                            .proxyUsername(proxyUsername)
                            .proxyPassword(proxyPassword)
                            .build());
            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = HttpUtils.getOperationResults(result, returnMessage, returnMessage, returnMessage);
            Integer statusCode = Integer.parseInt(result.get(STATUS_CODE));

            if (statusCode >= 200 && statusCode < 300) {
                final List<String> vnicList = JsonPath.read(returnMessage, LIST_VNIC_JSON_PATH);
                final List<String> vnicAttachmentsList = JsonPath.read(returnMessage, LIST_VNIC_ATTACHMENTS_JSON_PATH);
                if (!vnicList.isEmpty()) {
                    final String instanceListAsString = StringUtils.join(vnicList.toArray(), Constants.Common.DELIMITER);
                    results.put(VNIC_LIST, instanceListAsString);
                } else {
                    results.put(VNIC_LIST, EMPTY);
                }
                if (!vnicAttachmentsList.isEmpty()) {
                    final String instanceListAsString = StringUtils.join(vnicAttachmentsList.toArray(), Constants.Common.DELIMITER);
                    results.put(VNIC_ATTACHMENTS_LIST, instanceListAsString);
                } else {
                    results.put(VNIC_ATTACHMENTS_LIST, EMPTY);
                }

            } else {
                return HttpUtils.getFailureResults(tenancyOcid, statusCode, returnMessage);
            }
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }
}
