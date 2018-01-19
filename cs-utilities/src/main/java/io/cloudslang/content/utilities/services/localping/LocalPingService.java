/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.utilities.services.localping;

import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.entities.LocalPingInputs;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.services.osdetector.LocalOsDetectorService;
import io.cloudslang.content.utilities.services.osdetector.OsDetectorHelperService;

import java.io.IOException;
import java.util.Map;

import static io.cloudslang.content.constants.OtherValues.EMPTY_STRING;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.HUNDRED_PERCENT_LOSS;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.LOCALHOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.OTHER;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PERCENTAGE_PACKETS_LOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.UNABLE_TO_DETECT_LOCAL_OPERATING_SYSTEM;
import static io.cloudslang.content.utilities.services.localping.LocalPingCommandFactory.getLocalPingCommand;
import static io.cloudslang.content.utilities.util.CommandExecutor.executeCommand;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by pinteae on 1/10/2018.
 */
public class LocalPingService {

    private static final String IS_ALIVE = "is alive";

    public Map<String, String> executePingCommand(LocalPingInputs localPingInputs) throws IOException {
        String osFamily = detectOsFamily(LOCALHOST);

        if (osFamily == null || osFamily.equalsIgnoreCase(OTHER)) {
            throw new RuntimeException(UNABLE_TO_DETECT_LOCAL_OPERATING_SYSTEM);
        }

        LocalPingCommand localPingCommand = getLocalPingCommand(osFamily);

        String command = localPingCommand.createCommand(localPingInputs);

        Map<String, String> resultsMap = localPingCommand.parseOutput(executeCommand(command));
        if (pingSucceeded(resultsMap.get(RETURN_RESULT), resultsMap.get(PERCENTAGE_PACKETS_LOST))) {
            resultsMap.put(RETURN_CODE, ReturnCodes.SUCCESS);
            resultsMap.put(EXCEPTION, EMPTY_STRING);
        } else {
            resultsMap.put(RETURN_CODE, ReturnCodes.FAILURE);
            resultsMap.put(EXCEPTION, resultsMap.get(RETURN_RESULT));
        }

        return resultsMap;
    }

    String detectOsFamily(String host) {
        OsDetectorHelperService osDetectorHelperService = new OsDetectorHelperService();
        LocalOsDetectorService localOsDetectorService = new LocalOsDetectorService(osDetectorHelperService);
        OsDetectorInputs osDetectorInputs = new OsDetectorInputs.Builder()
                .withHost(host)
                .build();

        return osDetectorHelperService.resolveOsFamily(localOsDetectorService.detectOs(osDetectorInputs).getFamily());
    }

    private boolean pingSucceeded(String commandOutput, String percentageLost) {
        boolean succeeded = true;
        if (isEmpty(percentageLost) || percentageLost.equalsIgnoreCase(HUNDRED_PERCENT_LOSS)) {
            succeeded = false;
        }
        //Applicable for SunOs systems
        if (containsIgnoreCase(commandOutput, IS_ALIVE)) {
            succeeded = true;
        }

        return succeeded;
    }
}
