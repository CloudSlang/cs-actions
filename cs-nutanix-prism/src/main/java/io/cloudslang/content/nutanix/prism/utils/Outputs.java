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

package io.cloudslang.content.nutanix.prism.utils;

import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class CommonOutputs {
        public static final String DOCUMENT = "document";

    }

    public static class GetVMDetailsOutputs {
        public static final String VM_NAME = "vmName";

    }

    public static class ListVMOutputs {
        public static final String VM_LIST = "vmList";
    }

    public static class CreateVMOutputs {
        public static final String TASK_UUID = "taskUUID";
    }

}
