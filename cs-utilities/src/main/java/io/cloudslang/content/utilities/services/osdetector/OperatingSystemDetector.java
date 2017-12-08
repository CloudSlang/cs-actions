package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.util.OsDetectorUtils;

public class OperatingSystemDetector {
    private final SshOsDetectorService sshOsDetectorService;
    private final PowerShellOsDetectorService powershellOsDetectorService;
    private final NmapOsDetectorService nmapOsDetectorService;
    private final LocalOsDetectorService localOsDetectorService;
    private final OsDetectorUtils osDetectorUtils;

    public OperatingSystemDetector(SshOsDetectorService sshOsDetectorService, PowerShellOsDetectorService powershellOsDetectorService,
                                   NmapOsDetectorService nmapOsDetectorService, LocalOsDetectorService localOsDetectorService, OsDetectorUtils osDetectorUtils) {
        this.sshOsDetectorService = sshOsDetectorService;
        this.powershellOsDetectorService = powershellOsDetectorService;
        this.nmapOsDetectorService = nmapOsDetectorService;
        this.localOsDetectorService = localOsDetectorService;
        this.osDetectorUtils = osDetectorUtils;
    }

    public OperatingSystemDetails detectOs(OsDetectorInputs osDetectorInputs) {
        OperatingSystemDetails localOsSystemDetails = localOsDetectorService.detect(osDetectorInputs);
        if (osDetectorUtils.foundOperatingSystem(localOsSystemDetails)) {
            return localOsSystemDetails;
        }

        OperatingSystemDetails sshOsSystemDetails = sshOsDetectorService.detect(osDetectorInputs);
        sshOsSystemDetails.collectOsCommandOutputs(localOsSystemDetails);
        if (osDetectorUtils.foundOperatingSystem(sshOsSystemDetails)) {
            return sshOsSystemDetails;
        }

        OperatingSystemDetails powershellOsSystemDetails = powershellOsDetectorService.detect(osDetectorInputs);
        powershellOsSystemDetails.collectOsCommandOutputs(sshOsSystemDetails);
        if (osDetectorUtils.foundOperatingSystem(powershellOsSystemDetails)) {
            return powershellOsSystemDetails;
        }

        OperatingSystemDetails nmapOsSystemDetails = nmapOsDetectorService.detect(osDetectorInputs);
        nmapOsSystemDetails.collectOsCommandOutputs(powershellOsSystemDetails);
        return nmapOsSystemDetails;
    }

}
