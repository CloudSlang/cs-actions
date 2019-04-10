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


package io.cloudslang.content.utilities.services.localping;

import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.LINUX;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.SUN_OS;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.UNSUPPORTED_OPERATING_SYSTEM_S;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.WINDOWS;
import static java.lang.String.format;

/**
 * Created by pinteae on 1/11/2018.
 */
public class LocalPingCommandFactory {

    public static LocalPingCommand getLocalPingCommand(String osFamily) {
        switch (osFamily) {
            case WINDOWS:
                return new WindowsPingCommand();
            case LINUX:
                return new LinuxPingCommand();
            case SUN_OS:
                return new SunOsPingCommand();

            default:
                throw new RuntimeException(format(UNSUPPORTED_OPERATING_SYSTEM_S, osFamily));
        }
    }
}
