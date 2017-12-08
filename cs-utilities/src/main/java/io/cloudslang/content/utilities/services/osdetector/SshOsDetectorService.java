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

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.ssh.entities.SSHConnection;
import io.cloudslang.content.ssh.entities.SSHShellInputs;
import io.cloudslang.content.ssh.services.actions.ScoreSSHShellCommand;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.util.OsDetectorUtils;

import java.util.Map;

import static io.cloudslang.content.ssh.utils.Constants.DEFAULT_PORT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.OS_DETECTOR_COMMAND;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by Tirla Florin-Alin on 24/11/2017.
 **/
public class SshOsDetectorService implements OsDetectorService {
    private static final String SSH = "SSH";
    private final OsDetectorUtils osDetectorUtils;
    private final ScoreSSHShellCommand scoreSSHShellCommand;

    public SshOsDetectorService(OsDetectorUtils osDetectorUtils, ScoreSSHShellCommand scoreSSHShellCommand) {
        this.osDetectorUtils = osDetectorUtils;
        this.scoreSSHShellCommand = scoreSSHShellCommand;
    }

    @Override
    public OperatingSystemDetails detect(OsDetectorInputs osDetectorInputs) {
        OperatingSystemDetails operatingSystemDetails = new OperatingSystemDetails();
        Map<String, String> execute = scoreSSHShellCommand.execute(getSshShellInputs(osDetectorInputs));

        return osDetectorUtils.processOutput(operatingSystemDetails, execute, SSH);
    }

    private SSHShellInputs getSshShellInputs(OsDetectorInputs osDetectorInputs) {
        SSHShellInputs sshShellInputs = new SSHShellInputs();
        sshShellInputs.setHost(osDetectorInputs.getHost());
        sshShellInputs.setPort(defaultIfEmpty(osDetectorInputs.getPort(), valueOf(DEFAULT_PORT)));
        sshShellInputs.setUsername(osDetectorInputs.getUsername());
        sshShellInputs.setPassword(osDetectorInputs.getPassword());
        sshShellInputs.setPrivateKeyFile(osDetectorInputs.getPrivateKeyFile());
        sshShellInputs.setPrivateKeyData(osDetectorInputs.getPrivateKeyData());
        sshShellInputs.setAgentForwarding(osDetectorInputs.getAgentForwarding());
        sshShellInputs.setTimeout(osDetectorInputs.getSshTimeout());
        sshShellInputs.setConnectTimeout(osDetectorInputs.getSshConnectTimeout());
        sshShellInputs.setCloseSession(valueOf(true));
        sshShellInputs.setKnownHostsPolicy(osDetectorInputs.getKnownHostsPolicy());
        sshShellInputs.setKnownHostsPath(osDetectorInputs.getKnownHostsPath());
        sshShellInputs.setAllowedCiphers(osDetectorInputs.getAllowedCiphers());
        sshShellInputs.setProxyHost(osDetectorInputs.getProxyHost());
        sshShellInputs.setProxyPort(osDetectorInputs.getProxyPort());
        sshShellInputs.setProxyUsername(osDetectorInputs.getProxyUsername());
        sshShellInputs.setProxyPassword(osDetectorInputs.getProxyPassword());
        sshShellInputs.setCommand(OS_DETECTOR_COMMAND);
        sshShellInputs.setUseShell(valueOf(false));
        sshShellInputs.setSshGlobalSessionObject(new GlobalSessionObject<Map<String, SSHConnection>>());

        return sshShellInputs;
    }
}
