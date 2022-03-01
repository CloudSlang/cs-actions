/*
 * (c) Copyright 2022 Micro Focus, L.P.
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

public class Constants {
    public static class Common {
        public static final String API = "/api/nutanix/";
        public static final String DEFAULT_API_VERSION = "v2.0";
        public static final String NEW_LINE = "\n";
        public static final String DEFAULT_PROXY_PORT = "8080";
        public static final String DEFAULT_NUTANIX_PORT = "9440";
        public static final String DEFAULT_TIMEZONE = "UTC";
        public static final String BOOLEAN_FALSE = "false";
        public static final String BOOLEAN_TRUE = "true";
        public static final String STRICT = "strict";
        public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
        public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid proxy details.";
        public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
        public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
        public static final String EXCEPTION_INVALID_NAME = "The %s can only contain letters, numbers, underscores, " +
                "and hyphens";
        public static final String BASIC = "basic";
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
        public static final String DELETE = "DELETE";
        public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
        public static final String CHANGEIT = "changeit";
        public static final String ZERO = "0";
        public static final String CONNECT_TIMEOUT_CONST = "10000";
        public static final String CONNECTIONS_MAX_PER_ROUTE_CONST = "2";
        public static final String CONNECTIONS_MAX_TOTAL_CONST = "20";
        public static final String AUTHORIZATION = "Basic ";
        public static final String PATH_SEPARATOR = "/";
        public static final String AND = "&";
        public static final String QUERY = "?";
        public static final String ID = "id";
        public static final String HTTPS = "https";
        public static final String AFFINITY = "AFFINITY";
        public static final String STATUS_CODE = "statusCode";
        public static final String APPLICATION_API_JSON = "application/json";
        public static final String DELIMITER = ",";
        public static final String ERROR_MESSAGE_PATH = "message";
        public static final String ALLOWED_CYPHERS = "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256," +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384," +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256," +
                "TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA256";
    }

    public static class CounterConstants {
        public static final String RESULT_STRING = "resultString";
        public static final String INCREMENT_BY_DEFAULT_VALUE = "1";
        public static final String RESULT = "result";
        public static final String HASMORE = "has more";
        public static final String NOMORE = "no more";
        public static final String FAILURE = "failure";
        public static final String EXCEPTION = "exception";
        public static final String COUNTER_OPERATION_NAME = "Counter";
        public static final String TO = "to";
        public static final String FROM = "from";
        public static final String INCREMENT_BY = "incrementBy";
        public static final String SESSION_COUNTER = "sessionCounter";
        public static final String RESET = "reset";
    }

    public static class GetVMDetailsConstants {
        public static final String GET_VM_DETAILS_OPERATION_NAME = "Get VM Details";
        public static final String INCLUDE_VM_DISK_CONFIG_INFO = "include_vm_disk_config=";
        public static final String INCLUDE_VM_NIC_CONFIG_INFO = "include_vm_nic_config=";
        public static final String VM_NAME_PATH = "name";
        public static final String IP_ADDRESS_PATH = "$.vm_nics[*].ip_address";
        public static final String MAC_ADDRESS_PATH = "$.vm_nics[*].mac_address";
        public static final String POWER_STATE_PATH = "power_state";
        public static final String VM_DISK_UUID_PATH = "$.vm_disk_info[*].disk_address.vmdisk_uuid";
        public static final String STORAGE_CONTAINER_UUID_PATH = "$.vm_disk_info[*].storage_container_uuid";
        public static final String VM_LOGICAL_TIMESTAMP_PATH = "$.vm_logical_timestamp";
        public static final String AFFINITY_PATH = "$.affinity";
        public static final String HOST_UUID_PATH = "$.affinity.host_uuids[*]";
        public static final String GET_VM_DETAILS_PATH = "/vms";
    }

    public static class DetachDisksConstants {
        public static final String DETACH_DISKS_OPERATION_NAME = "Detach Disks";
        public static final String DETACH_DISKS_PATH = "/disks/detach";
    }

    public static class AttachDisksConstants {
        public static final String ATTACH_DISKS_OPERATION_NAME = "Attach Disks";
        public static final String ATTACH_DISKS_PATH = "/disks/attach";
        public static final String TASK_UUID_PATH = "task_uuid";
    }

    public static class DeleteNICConstants {
        public static final String DELETE_NIC_OPERATION_NAME = "Delete NIC";
        public static final String DELETE_NIC_PATH = "/nics/";
        public static final String VM_LOGICAL_TIMESTAMP_QUERY_PARAM = "vm_logical_timestamp=";
    }

    public static class ListVMsConstants {
        public static final String LIST_VMS_OPERATION_NAME = "List VMs";
        public static final String FILTER = "filter=";
        public static final String OFFSET = "offset=";
        public static final String LENGTH = "length=";
        public static final String SORT_ORDER = "sortorder=";
        public static final String SORT_ATTRIBUTE = "sortattribute=";
        public static final String LIST_VM_JSON_PATH = "$.entities[*].name";
    }

    public static class CreateVMConstants {
        public static final String CREATE_VM_OPERATION_NAME = "Create VM";
        public static final String TASK_UUID_PATH = "task_uuid";
    }

    public static class UpdateVMConstants {
        public static final String UPDATE_VM_OPERATION_NAME = "Update VM";
        public static final String TASK_UUID_PATH = "task_uuid";

    }

    public static class DeleteVMConstants {
        public static final String DELETE_VM_OPERATION_NAME = "Delete VM";
        public static final String DELETE_SNAPSHOTS = "delete_snapshots=";
        public static final String LOGICAL_TIMESTAMP = "logical_timestamp=";
    }

    public static class SetVMPowerStateConstants {
        public static final String SET_VM_POWER_STATE_OPERATION_NAME = "Set VM Power State";
        public static final String SET_POWER_STATE_PATH = "set_power_state";
    }

    public static class GetTaskDetailsConstants {
        public static final String GET_TASK_DETAILS_OPERATION_NAME = "Get Task Details";
        public static final String INCLUDE_SUBTASKS_INFO = "include_subtasks_info=";
        public static final String GET_TASK_DETAILS_PATH = "/tasks";
        public static final String VM_UUID_PATH = "$.entity_list[*].entity_id";
        public static final String TASK_STATUS_PATH = "progress_status";
        public static final String SUCCEEDED = "Succeeded";
        public static final String FAILED = "Failed";
        public static final String TASK_FAILURE_PATH = "$.meta_response.error_detail";
    }

    public static class AddNICConstants {
        public static final String ADD_NIC_OPERATION_NAME = "Add NIC";
        public static final String ADD_NIC_PATH = "/nics";
        public static final String TASK_UUID_PATH = "task_uuid";
    }

}
