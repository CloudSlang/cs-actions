/*
 * Copyright 2020-2024 Open Text
 * This program and the accompanying materials
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
        public static final String FETCH_FULL_CONFIG = "fetchFullConfig";
    }

    public static class DeleteMonitorGroupInputs {
        public static final String EXTERNAL_ID = "externalId";
    }

    public static class ChangeMonitorGroupStatusInputs {
        public static final String STATUS = "status";
        public static final String ENABLE = "enable";
        public static final String TIME_PERIOD = "timePeriod";
        public static final String FROM_TIME = "fromTime";
        public static final String TO_TIME = "toTime";
        public static final String DESCRIPTION = "description";
    }

    public static class ChangeMonitorStatusInputs {
        public static final String MONITOR_ID = "monitorId";
    }

    public static class DeployTemplate {
        public static final String PATH_TO_TEMPLATE = "pathToTemplate";
        public static final String PATH_TO_TARGET_GROUP = "pathToTargetGroup";
        public static final String CONNECT_TO_SERVER = "connectToServer";
        public static final String TEST_REMOTES = "testRemotes";
        public static final String CUSTOM_PARAMETERS = "customParameters";

    }
    public static class DeleteRemoteServerInputs {
        public static final String PLATFORM = "platform";
        public static final String REMOTE_NAME = "remoteName";
    }

    public static class UpdateTemplate {
        public static final String FULL_PATH_TO_TEMPLATE = "fullPathToTemplate";
        public static final String PROPERTIES = "properties";
    }

    public static class GetMonitorsDeployedAt{
        public static final String TARGET_SERVER = "targetServer";
        public static final String COL_DELIMITER = "colDelimiter";
        public static final String ROW_DELIMITER = "rowDelimiter";
    }


        public static class RunMonitor {
        public static final String MONITOR_ID = "monitorId";
        public static final String EXECUTION_TIMEOUT = "executionTimeout";


    }
}

