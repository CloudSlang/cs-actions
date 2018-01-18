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

import com.google.common.collect.ImmutableList;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static java.lang.String.format;
import static java.net.InetAddress.getByName;
import static java.net.NetworkInterface.getByInetAddress;
import static org.apache.commons.lang3.SystemUtils.OS_ARCH;
import static org.apache.commons.lang3.SystemUtils.OS_NAME;
import static org.apache.commons.lang3.SystemUtils.OS_VERSION;

/**
 * Created by Tirla Florin-Alin on 05/12/2017.
 **/
public class LocalOsDetectorService implements OperatingSystemDetector {
    private final OsDetectorHelperService osDetectorHelperService;

    public LocalOsDetectorService(OsDetectorHelperService osDetectorHelperService) {
        this.osDetectorHelperService = osDetectorHelperService;
    }

    private boolean isLocalAddress(String host) {
        InetAddress address;
        try {
            address = getByName(host);
        } catch (UnknownHostException ignored) {
            return false;
        }

        if (address.isAnyLocalAddress() || address.isLoopbackAddress()) {
            return true;
        }

        try {
            return getByInetAddress(address) != null;
        } catch (SocketException ignored) {
            return false;
        }
    }

    @Override
    public OperatingSystemDetails detectOs(OsDetectorInputs osDetectorInputs) {
        OperatingSystemDetails operatingSystemDetails = new OperatingSystemDetails();
        String host = osDetectorInputs.getHost();
        if (isLocalAddress(host)) {
            operatingSystemDetails.setName(OS_NAME);
            operatingSystemDetails.setVersion(OS_VERSION);
            operatingSystemDetails.setArchitecture(OS_ARCH);
            operatingSystemDetails.setFamily(osDetectorHelperService.resolveOsFamily(OS_NAME));

            operatingSystemDetails.addCommandOutput("Local", ImmutableList.of(format("%s is a local address.", host)));
        } else {
            operatingSystemDetails.addCommandOutput("Local", ImmutableList.of(format("%s is not a local address.", host)));
        }
        return operatingSystemDetails;
    }
}
