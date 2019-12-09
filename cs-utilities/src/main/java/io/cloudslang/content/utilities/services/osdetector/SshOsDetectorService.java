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

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.ssh.entities.SSHConnection;
import io.cloudslang.content.ssh.entities.SSHShellInputs;
import io.cloudslang.content.ssh.services.actions.ScoreSSHShellCommand;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;

import java.util.Map;

import static io.cloudslang.content.ssh.utils.Constants.DEFAULT_PORT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.OS_DETECTOR_COMMAND;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by Tirla Florin-Alin on 24/11/2017.
 **/
public class SshOsDetectorService implements OperatingSystemDetector {
    private static final String SSH = "SSH";
    private final OsDetectorHelperService osDetectorHelperService;
    private final ScoreSSHShellCommand scoreSSHShellCommand;

    public SshOsDetectorService(OsDetectorHelperService osDetectorHelperService, ScoreSSHShellCommand scoreSSHShellCommand) {
        this.osDetectorHelperService = osDetectorHelperService;
        this.scoreSSHShellCommand = scoreSSHShellCommand;
    }

    @Override
    public OperatingSystemDetails detectOs(OsDetectorInputs osDetectorInputs) {
        OperatingSystemDetails operatingSystemDetails = new OperatingSystemDetails();
        Map<String, String> execute = scoreSSHShellCommand.execute(getSshShellInputs(osDetectorInputs));

        return osDetectorHelperService.processOutput(operatingSystemDetails, execute, SSH);
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
