/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.sitescope.constants;

import io.cloudslang.content.constants.InputNames;

public final class Inputs extends InputNames {
    public static class CommonInputs {
        public static final String HOST = "host";
        public static final String PORT = "port";
        public static final String PROTOCOL = "protocol";
        public static final String FULL_PATH_TO_GROUP = "fullPathToGroup";
        public static final String FULL_PATH_TO_MONITOR = "fullPathToMonitor";
        public static final String DELIMITER = "delimiter";
        public static final String IDENTIFIER = "identifier";
    }

    public static class DeleteMonitorGroupInputs {
        public static final String EXTERNAL_ID = "externalId";
    }

    public static class EnableMonitorGroupInputs {
        public static final String ENABLE = "enable";
        public static final String TIME_PERIOD = "timePeriod";
        public static final String FROM_TIME = "fromTime";
        public static final String TO_TIME = "toTime";
        public static final String DESCRIPTION = "description";
    }

    public static class EnableMonitorInputs {
        public static final String MONITOR_ID = "monitorId";
    }

    public static class DeployTemplate {
        public static final String PATH_TO_TEMPLATE = "pathToTemplate";
        public static final String PATH_TO_TARGET_GROUP = "pathToTargetGroup";
        public static final String CONNECT_TO_SERVER = "connectToServer";
        public static final String TEST_REMOTES = "testRemotes";
        public static final String CUSTOM_PARAMETERS = "customParameters";

    }


        public static class RunMonitor {
        public static final String MONITOR_ID = "monitorId";
        public static final String TIMEOUT_RUN_MONITOR = "timeOut";


    }
}

