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

package io.cloudslang.content.oracle.oci.utils;

import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class CommonOutputs {
        public static final String DOCUMENT = "document";

    }
    public static class CreateInstanceOutputs {
        public static final String INSTANCE_ID = "instance_id";
    }

    public static class ListInstancesOutputs {
        public static final String INSTANCE_LIST = "instance_list";
    }

    public static class GetInstanceDetailsOutputs {
        public static final String INSTANCE_STATE = "instance_state";
    }

    public static class ListVnicAttachmentsOutputs {
        public static final String VNIC_LIST = "vnic_list";
    }

    public static class GetVnicDetailsOutputs {
        public static final String PRIVATE_IP = "private_ip";
        public static final String PUBLIC_IP = "public_ip";

    }

    public static class GetInstanceDefaultCredentialsOutputs {
        public static final String INSTANCE_USERNAME = "instance_username";
        public static final String INSTANCE_PASSWORD = "instance_password";
    }

}
