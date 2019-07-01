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

import io.cloudslang.content.ssh.entities.SSHShellInputs;
import io.cloudslang.content.ssh.services.actions.ScoreSSHShellCommand;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Tirla Florin-Alin on 08/12/2017.
 **/
@RunWith(PowerMockRunner.class)
public class SshOsDetectorServiceTest {
    @Mock
    private OsDetectorHelperService osDetectorHelperService;

    @Mock
    private ScoreSSHShellCommand scoreSSHShellCommand;

    private SshOsDetectorService sshOsDetectorService;

    @Before
    public void setUp() {
        sshOsDetectorService = new SshOsDetectorService(osDetectorHelperService, scoreSSHShellCommand);
    }

    @Test
    public void testSshDetection() {
        doReturn(new HashMap<String, String>()).when(scoreSSHShellCommand).execute(any(SSHShellInputs.class));
        OperatingSystemDetails toBeReturnedOsDetails = new OperatingSystemDetails();
        toBeReturnedOsDetails.setArchitecture("xYZ");
        toBeReturnedOsDetails.setFamily("b os fam");
        toBeReturnedOsDetails.setName("b os");
        toBeReturnedOsDetails.setVersion("ultimate");
        doReturn(toBeReturnedOsDetails).when(osDetectorHelperService).processOutput(any(OperatingSystemDetails.class), anyMapOf(String.class, String.class), anyString());

        OperatingSystemDetails actualOsDetails = sshOsDetectorService.detectOs(new OsDetectorInputs.Builder().build());

        assertEquals("b os", actualOsDetails.getName());
        assertEquals("xYZ", actualOsDetails.getArchitecture());
        assertEquals("b os fam", actualOsDetails.getFamily());
        assertEquals("ultimate", actualOsDetails.getVersion());
    }
}