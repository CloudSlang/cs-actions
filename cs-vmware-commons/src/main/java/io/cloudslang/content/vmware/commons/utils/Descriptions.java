/*
 * (c) Copyright 2023 Open Text
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
package io.cloudslang.content.vmware.commons.utils;

public class Descriptions {
    public static final String RETURN_CODE_DESC =  "The returnCode of the operation: 0 for success, -1 for failure.";
    public static final String RETURN_RESULT_DESC ="VM id resulted from the task in case of success or the error message in case of failure";
    public static final String EXCEPTION_DESC = "In case of failure response, this result contains the java stack trace of the runtime exception or fault details that the remote server generated throughout its communication with the client.";
    public static final String SUCCESS_DESC = "The operation completed successfully.";
    public static final String FAILURE_DESC ="Something went wrong.";
    public static final String HOST_DESC="VMware vCenter, ESX or ESXi host hostname or IP.";
    public static final String USERNAME_DESC ="VMware username.";
    public static final String PASSWORD_DESC ="VMware user's password.";
    public static final String VM_FOLDER_DESC ="The ID or name of the VIRTUAL_MACHINE folder to which you want to add your virtual machine. Example of folder id: group-v336";
    public static final String VM_SOURCE_DESC ="Primary Virtual Machine identifier of the virtual machine/template to clone from. UUID, name or vmid (vm-127) can be used.";
    public static final String VM_RESOURCE_POOL_DESC ="The ID or name of the resource pool to which you want to add your virtual machine. One of host system, cluster, or resource pool must be specified. Example of resource pool id: resgroup-452 ";
    public static final String DATASTORE_DESC ="The ID or name of datastore or datastore cluster to store new virtual machine. Example of datastore id: datastore-26";
    public static final String CLUSTER_DESC ="The ID or name of the cluster to which you want to add your virtual machine. One of host system, cluster, or resource pool must be specified. Example of cluster id: domain-c133";
    public static final String VM_IDENTIFIER_TYPE_DESC ="Identifier type used for vmSource, vmFolder, vmResourcePool, datastore and cluster. Valid values: 'name', 'vmid'. Default value: 'name'";
    public static final String DESCRIPTION_DESC ="Description / annotation. To be able to clone the machine, even when no description input is given, the user needs to have the following permission to the virtual machine: Virtual machine -> Configuration -> Set annotation.";
    public static final String HOST_SYSTEM_DESC = "Name or ID of destination host system (ESXi) for new virtual machine as seen in the vCenter UI.  Only supported when host is a vCenter.  If not specified the same host system of the source virtual machine will be used. One of host system, cluster, or resource pool must be specified.";
    public static final String TIMEOUT_DESC = "The amount of time (in seconds) to wait to complete the execution. A value of '0' disables this feature. \n" +
            "Default: 60  \n";
}
