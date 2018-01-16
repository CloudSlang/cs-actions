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
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.HUNDRED_PERCENT_LOSS;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PERCENTAGE_PACKETS_LOST;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by pinteae on 1/10/2018.
 */
public class LocalPingService {

    private static final String LOCALHOST = "127.0.0.1";
    private static final String OTHER = "other";
    private static final String UNABLE_TO_DETECT_LOCAL_OPERATING_SYSTEM = "Unable to detect local operating system.";

    public Map<String, String> executePingCommand(LocalPingInputs localPingInputs) throws IOException {
        String osFamily = detectOsFamily(LOCALHOST);

        if (osFamily == null || osFamily.equalsIgnoreCase(OTHER)) {
            throw new RuntimeException(UNABLE_TO_DETECT_LOCAL_OPERATING_SYSTEM);
        }

        LocalPingCommand localPingCommand = LocalPingCommandFactory.getLocalPingCommand(osFamily);

        String command = localPingCommand.createCommand(localPingInputs);

        Map<String, String> resultsMap = localPingCommand.parseOutput(executeCommand(command));
        if (pingSucceeded(resultsMap.get(PERCENTAGE_PACKETS_LOST))) {
            resultsMap.put(RETURN_CODE, ReturnCodes.SUCCESS);
        } else {
            resultsMap.put(RETURN_CODE, ReturnCodes.FAILURE);
        }

        return resultsMap;
    }

    @NotNull
    private String executeCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder output = new StringBuilder();
        String currentLine;
        while ((currentLine = inputStream.readLine()) != null) {
            output.append(currentLine)
                    .append(System.lineSeparator());
        }
        return output.toString();
    }

    private boolean pingSucceeded(String percentageLost) {
        boolean succeeded = true;
        if (isEmpty(percentageLost)) {
            succeeded = false;
        } else if (percentageLost.equalsIgnoreCase(HUNDRED_PERCENT_LOSS)) {
            succeeded = false;
        }

        return succeeded;
    }

    private String detectOsFamily(String host) {
        OsDetectorHelperService osDetectorHelperService = new OsDetectorHelperService();
        LocalOsDetectorService localOsDetectorService = new LocalOsDetectorService(osDetectorHelperService);
        OsDetectorInputs osDetectorInputs = new OsDetectorInputs.Builder()
                .withHost(host)
                .build();

        return osDetectorHelperService.resolveOsFamily(localOsDetectorService.detectOs(osDetectorInputs).getFamily());
    }
}
