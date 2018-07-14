/*
 * (c) Copyright 2018 Micro Focus
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
package io.cloudslang.content.alibaba.utils.constants;

public class Descriptions {
    public static class Common {
        // Inputs
        public static final String REGION_ID_DESC = "";
        public static final String ACCESS_KEY_ID_DESC = "";
        public static final String ACCESS_KEY_SECRET_ID_DESC = "";
        public static final String PROXY_HOST_DESC = "";
        public static final String PROXY_PORT_DESC = "";
        public static final String PROXY_USERNAME_DESC = "";
        public static final String PROXY_PASSWORD_DESC = "";
        public static final String INSTANCE_ID_DESC = "";
        public static final String FORCE_STOP_DESC = "";

        //Results
        public static final String RETURN_RESULT_DESC = "The authentication token in case of success, or an error" +
                " message in case of failure.";
        public static final String RETURN_CODE_DESC = "";
        public static final String REQUEST_ID_DESC = "";
        public static final String EXCEPTION_DESC = "";
    }

    public static class DeleteInstance {
        public static final String DELTETE_INSTANCE_DESC = "This operation is used to release a Pay-As-You-Go or expired " +
                "Subscription instance having the status Stopped.";
    }

    public static class StartInstance {
        public static final String START_INSTANCE_DESC = "This operation is used to start a specified instance.";

        //Inputs
        public static final String INIT_LOCAL_DISK_DESC = "Recover to the previous normal status of instance local disk " +
                "when exceptions occurs. Valid values: true, false";
    }

    public static class StopInstance {
        public static final String STOP_INSTANCE_DESC = "This operation is used to stop an ECS instance.";

        //Inputs
        public static final String CONFIRM_STOP_DESC = "";
        public static final String STOPPED_MODE_DESC = "";
    }

    public static class RESTARTInstance {
        public static final String RESTART_INSTANCE_DESC = "This operation is used to resrtart an ECS instance.";
    }
}
