/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.entities.WSManRequestInputs;
import io.cloudslang.content.services.WSManRemoteShellService;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.entities.InputDefaults.MAX_ENVELOPE_SIZE;
import static io.cloudslang.content.entities.InputDefaults.PORT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.OS_DETECTOR_COMMAND;
import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by Tirla Florin-Alin on 24/11/2017.
 **/
public class PowerShellOsDetectorService implements OsDetectorService {
    private static final String POWER_SHELL = "PowerShell";
    private final OsDetectorHelperService osDetectorHelperService;
    private final WSManRemoteShellService wsManRemoteShellService;

    public PowerShellOsDetectorService(OsDetectorHelperService osDetectorHelperService, WSManRemoteShellService wsManRemoteShellService) {
        this.osDetectorHelperService = osDetectorHelperService;
        this.wsManRemoteShellService = wsManRemoteShellService;
    }

    @Override
    public OperatingSystemDetails detect(OsDetectorInputs wsManRequestInputs) {
        OperatingSystemDetails operatingSystemDetails = new OperatingSystemDetails();
        Map<String, String> execute = runCommandUsingPowershell(getWsManRequestInputs(wsManRequestInputs));

        return osDetectorHelperService.processOutput(operatingSystemDetails, execute, POWER_SHELL);
    }

    private Map<String, String> runCommandUsingPowershell(WSManRequestInputs wsManRequestInputs) {
        try {
            return wsManRemoteShellService.runCommand(wsManRequestInputs);
        } catch (final Exception exception) {
            return new HashMap<String, String>() {{put(RETURN_RESULT, exception.getMessage()); put(RETURN_CODE, FAILURE);}};
        }
    }

    private WSManRequestInputs getWsManRequestInputs(OsDetectorInputs osDetectorInputs) {
        return new WSManRequestInputs.WSManRequestInputsBuilder()
                .withHost(osDetectorInputs.getHost())
                .withPort(defaultIfEmpty(osDetectorInputs.getPort(), PORT.getValue()))
                .withProtocol(osDetectorInputs.getProtocol())
                .withUsername(osDetectorInputs.getUsername())
                .withPassword(osDetectorInputs.getPassword())
                .withAuthType(osDetectorInputs.getAuthType())
                .withKerberosConfFile(osDetectorInputs.getKerberosConfFile())
                .withKerberosLoginConfFile(osDetectorInputs.getKerberosLoginConfFile())
                .withKerberosSkipPortForLookup(osDetectorInputs.getKerberosSkipPortForLookup())
                .withProxyHost(osDetectorInputs.getProxyHost())
                .withProxyPort(osDetectorInputs.getProxyPort())
                .withProxyUsername(osDetectorInputs.getProxyUsername())
                .withProxyPassword(osDetectorInputs.getProxyPassword())
                .withTrustAllRoots(osDetectorInputs.getTrustAllRoots())
                .withX509HostnameVerifier(osDetectorInputs.getX509HostnameVerifier())
                .withKeystore(osDetectorInputs.getKeystore())
                .withKeystorePassword(osDetectorInputs.getKeystorePassword())
                .withTrustKeystore(osDetectorInputs.getTrustKeystore())
                .withTrustPassword(osDetectorInputs.getTrustPassword())
                .withWinrmLocale(osDetectorInputs.getWinrmLocale())
                .withMaxEnvelopeSize(MAX_ENVELOPE_SIZE.getValue())
                .withScript(OS_DETECTOR_COMMAND)
                .withOperationTimeout(valueOf(MILLISECONDS.toSeconds(parseLong(osDetectorInputs.getPowerShellTimeout()))))
                .build();
    }
}
