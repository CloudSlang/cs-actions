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



package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;

public class OperatingSystemDetectorService implements OperatingSystemDetector {
    private final SshOsDetectorService sshOsDetectorService;
    private final PowerShellOsDetectorService powershellOsDetectorService;
    private final NmapOsDetectorService nmapOsDetectorService;
    private final LocalOsDetectorService localOsDetectorService;
    private final OsDetectorHelperService osDetectorHelperService;

    public OperatingSystemDetectorService(SshOsDetectorService sshOsDetectorService, PowerShellOsDetectorService powershellOsDetectorService,
                                          NmapOsDetectorService nmapOsDetectorService, LocalOsDetectorService localOsDetectorService, OsDetectorHelperService osDetectorHelperService) {
        this.sshOsDetectorService = sshOsDetectorService;
        this.powershellOsDetectorService = powershellOsDetectorService;
        this.nmapOsDetectorService = nmapOsDetectorService;
        this.localOsDetectorService = localOsDetectorService;
        this.osDetectorHelperService = osDetectorHelperService;
    }

    public OperatingSystemDetails detectOs(OsDetectorInputs osDetectorInputs) {
        OperatingSystemDetails localOsSystemDetails = localOsDetectorService.detectOs(osDetectorInputs);
        if (osDetectorHelperService.foundOperatingSystem(localOsSystemDetails)) {
            return localOsSystemDetails;
        }

        OperatingSystemDetails sshOsSystemDetails = sshOsDetectorService.detectOs(osDetectorInputs);
        sshOsSystemDetails.collectOsCommandOutputs(localOsSystemDetails);
        if (osDetectorHelperService.foundOperatingSystem(sshOsSystemDetails)) {
            return sshOsSystemDetails;
        }

        OperatingSystemDetails powershellOsSystemDetails = powershellOsDetectorService.detectOs(osDetectorInputs);
        powershellOsSystemDetails.collectOsCommandOutputs(sshOsSystemDetails);
        if (osDetectorHelperService.foundOperatingSystem(powershellOsSystemDetails)) {
            return powershellOsSystemDetails;
        }

        OperatingSystemDetails nmapOsSystemDetails = nmapOsDetectorService.detectOs(osDetectorInputs);
        nmapOsSystemDetails.collectOsCommandOutputs(powershellOsSystemDetails);
        return nmapOsSystemDetails;
    }

}
