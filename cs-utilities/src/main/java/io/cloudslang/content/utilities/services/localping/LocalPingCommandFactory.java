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

import static java.lang.String.format;

/**
 * Created by pinteae on 1/11/2018.
 */
public class LocalPingCommandFactory {

    private static final String UNSUPPORTED_OPERATING_SYSTEM_S = "Unsupported operating system %s";
    private static final String WINDOWS = "Windows";
    private static final String LINUX = "Linux";
    private static final String SUN_OS = "SunOS";

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
