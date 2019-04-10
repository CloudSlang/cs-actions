/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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


package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.entities.LocalPingInputs;
import io.cloudslang.content.utilities.services.localping.LocalPingService;

import java.util.Map;

import static io.cloudslang.content.constants.OtherValues.EMPTY_STRING;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OutputsDescription.EXCEPTION_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OutputsDescription.RETURN_CODE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.FAILURE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.SUCCESS_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.IP_VERSION;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.LOCAL_PING_OPERATION_NAME;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_RECEIVED;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_SENT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_COUNT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_SIZE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PERCENTAGE_PACKETS_LOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TARGET_HOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TIMEOUT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_AVG;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MAX;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MIN;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.InputsDescription.IP_VERSION_INPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.InputsDescription.PACKETS_COUNT_INPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.InputsDescription.PACKET_SIZE_INPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.InputsDescription.TARGET_HOST_INPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.InputsDescription.TIMEOUT_INPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.OperationDescription.LOCAL_PING_OPERATION_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.OutputsDescription.LOCAL_PING_RETURN_RESULT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.OutputsDescription.PACKETS_RECEIVED_OUTPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.OutputsDescription.PACKETS_SENT_OUTPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.OutputsDescription.PERCENTAGE_PACKETS_LOST_OUTPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.OutputsDescription.TRANSMISSION_TIME_AVG_OUTPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.OutputsDescription.TRANSMISSION_TIME_MAX_OUTPUT_DESC;
import static io.cloudslang.content.utilities.entities.constants.LocalPingDescriptions.OutputsDescription.TRANSMISSION_TIME_MIN_OUTPUT_DESC;
import static io.cloudslang.content.utils.Constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static java.net.InetAddress.getByName;

/**
 * Created by pinteae on 1/11/2018.
 */
public class LocalPing {

    @Action(name = LOCAL_PING_OPERATION_NAME,
            description = LOCAL_PING_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = LOCAL_PING_RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = PACKETS_SENT, description = PACKETS_SENT_OUTPUT_DESC),
                    @Output(value = PACKETS_RECEIVED, description = PACKETS_RECEIVED_OUTPUT_DESC),
                    @Output(value = PERCENTAGE_PACKETS_LOST, description = PERCENTAGE_PACKETS_LOST_OUTPUT_DESC),
                    @Output(value = TRANSMISSION_TIME_MIN, description = TRANSMISSION_TIME_MIN_OUTPUT_DESC),
                    @Output(value = TRANSMISSION_TIME_MAX, description = TRANSMISSION_TIME_MAX_OUTPUT_DESC),
                    @Output(value = TRANSMISSION_TIME_AVG, description = TRANSMISSION_TIME_AVG_OUTPUT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true, description = FAILURE_DESC),
            }
    )
    public Map<String, String> execute(@Param(value = TARGET_HOST, required = true, description = TARGET_HOST_INPUT_DESC) String targetHost,
                                       @Param(value = PACKET_COUNT, description = PACKETS_COUNT_INPUT_DESC) String packetCount,
                                       @Param(value = PACKET_SIZE, description = PACKET_SIZE_INPUT_DESC) String packetSize,
                                       @Param(value = TIMEOUT, description = TIMEOUT_INPUT_DESC) String timeout,
                                       @Param(value = IP_VERSION, description = IP_VERSION_INPUT_DESC) String ipVersion) {
        try {
            String targetIpAddress = getByName(targetHost).getHostAddress();

            LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                    .targetHost(targetIpAddress)
                    .ipVersion(ipVersion)
                    .packetCount(packetCount)
                    .packetSize(packetSize)
                    .timeout(timeout)
                    .build();

            return new LocalPingService().executePingCommand(localPingInputs);
        } catch (Exception e) {
            Map<String, String> resultsMap = getFailureResultsMap(e);
            resultsMap.put(PACKETS_SENT, EMPTY_STRING);
            resultsMap.put(PACKETS_RECEIVED, EMPTY_STRING);
            resultsMap.put(PERCENTAGE_PACKETS_LOST, EMPTY_STRING);
            resultsMap.put(TRANSMISSION_TIME_MIN, EMPTY_STRING);
            resultsMap.put(TRANSMISSION_TIME_AVG, EMPTY_STRING);
            resultsMap.put(TRANSMISSION_TIME_MAX, EMPTY_STRING);

            return resultsMap;
        }
    }
}
