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
        public static final String REGION_ID_DESC = "Region ID of an instance. You can call DescribeRegions to obtain the latest region list.";
        public static final String ACCESS_KEY_ID_DESC = "The Access Key ID associated with your Alibaba cloud account.";
        public static final String ACCESS_KEY_SECRET_ID_DESC = "The Secret ID of the Access Key associated with your Alibaba cloud account.";
        public static final String PROXY_HOST_DESC = "Proxy server used to access the Alibaba cloud services.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the Alibaba cloud services." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username input value.";
        public static final String INSTANCE_ID_DESC = "The specified instance ID.";
        public static final String FORCE_STOP_DESC = "Whether to force shutdown upon device restart.  Value range:" +
                "true: force the instance to shut down " +
                "false: the instance shuts down normally" +
                "Default: false";
        public static final String IMAGE_ID_DESC = "ID of an image file. An image is a running environment template for ECS instances.";
        public static final String INSTANCE_TYPE_DESC = "Instance type. For more information, call DescribeInstanceTypes to view the latest instance type list.";
        public static final String SECURITY_GROUP_ID_DESC = "ID of the security group to which an ECS instance belongs. " +
                "A security group is a firewall group that manages instances in the same region with the same security requirements and mutual trust.";
        public static final String ZONE_ID_DESC = "ID of a zone to which an instance belongs.  If it is null, a zone is selected by the system. " +
                "Default value: ''";
        public static final String INSTANCE_NAME_DESC = "Name of an ECS instance." +
                "It can contain [2, 128] characters in length, must begin with an English or Chinese character, " +
                "and can contain digits, periods (.), colons (:), underscores (_), and hyphens (-)." +
                "If this parameter is not specified, the default value is the InstanceId of the instance.";
        public static final String DESCRIPTION_DESC = "Description of an ECS instance." +
                "It can be [2, 256] characters in length." +
                "It cannot begin with \"http://\" or \"https://\"." +
                "Default value: ''.";
        public static final String INTERNET_CHARGE_TYPE_DESC = "Internet billing method. PayByTraffic: You are billed based " +
                "on the traffic usage. Default: PayByTraffic";
        public static final String INTERNET_MAX_BANDWIDTH_IN_DESC = "Maximum inbound bandwidth from the Internet, its unit of measurement is Mbit/s. " +
                "Value range: [1, 200]. Default: '200'";
        public static final String INTERNET_MAX_BANDWIDTH_OUT_DESC = "Maximum outbound bandwidth to the Internet, its unit of measurement is Mbit/s. " +
                "If this parameter is not specified, an error is returned. " +
                "Value range: PayByTraffic: [0,100]. Default: '0'";
        public static final String HOSTNAME_DESC = "Host name of the ECS instance." +
                "It cannot start or end with a period (.) or a hyphen (-) and it cannot have two or more consecutive periods (.) or hyphens (-)." +
                "On Windows, the host name can contain [2, 15] characters in length. It can contain uppercase or lowercase letters, digits, periods (.), and hyphens (-). It cannot be only digits." +
                "On other OSs, such as Linux, the host name can contain [2, 128] characters in length. It can be segments separated by periods (.) and can contain uppercase or lowercase letters, digits, and hyphens (-).";
        public static final String PASSWORD_DESC = "Password of the ECS instance." +
                "It can be 8 to 30 characters in length and can contain uppercase and lowercase letters, digits, and special characters." +
                "Special characters such as ( ) ' ~ !  @ # $ % ^ & * - + = | { } [ ] : ; ‘ < > , . ? are allowed. /";
        public static final String PASSWORD_INHERIT_DESC = "Whether to use the password pre-configured in the image you select or not. " +
                "When PasswordInherit is specified, the Password must be null. " +
                "For a secure access, make sure that the selected image has password configured.";
        public static final String IS_OPTIMIZED_DESC = "Whether it is an I/O-optimized instance or not. " +
                "For phased-out instance types, the default value is none. For other instance types, the default value is optimized." +
                "Valid values: none, optimized";
        public static final String SYSTEM_DISK_CATEGORY_DESC = "The category of the system disk.  Optional values:" +
                "cloud: Basic cloud disk." +
                "cloud_efficiency: Ultra cloud disk." +
                "cloud_ssd: Cloud SSD." +
                "ephemeral_ssd: Ephemeral SSD." +
                "For phased-out instance types and non-I/O optimized instances, the default value is cloud." +
                "Otherwise, the default value is cloud_efficiency.";
        public static final String SYSTEM_DISK_SIZE_DESC = "Size of the system disk, measured in GiB. Value range: [20, 500]. " +
                "The specified value must be equal to or greater than max{20, Imagesize}. Default: max{40, ImageSize}.";
        public static final String SYSTEM_DISK_NAME_DESC = "Name of the system disk." +
                "It can be [2, 128] characters in length, must begin with an English letter or Chinese character, and can contain digits, colons (:), underscores (_), or hyphens (-)." +
                "The name is displayed in the ECS console." +
                "It cannot begin with http:// or https://." +
                "Default value: ''";
        public static final String SYSTEM_DISK_DESCRIPTION_DESC = "Description of a system disk." +
                "It can be [2, 256] characters in length." +
                "The description is displayed in the ECS console." +
                "It cannot begin with http:// or https://." +
                "Default value: ''";
        public static final String DELIMITER_DESC = "The delimiter used to separate the values for dataDisksSizeList, dataDisksCategoryList, " +
                "dataDisksEncryptedList, dataDisksSnapshotList, dataDisksNameList, dataDisksDescriptionList, dataDisksDeleteWithInstanceList, tagsKeyList, tagsValueList inputs. Default: ','";
        public static final String DATA_DISKS_SIZE_LIST_DESC = "Size of the n data disk in GBs, n starts from 1. Optional values:" +
                "cloud: [5, 2000], cloud_efficiency: [20, 32768], cloud_ssd: [20, 32768], ephemeral_ssd: [5, 800]." +
                "The value must be equal to or greater than the specific snapshot (SnapshotId).";
        public static final String DATA_DISKS_CATEGORY_LIST_DESC = "Category of the data disk n, the valid range of n is [1, 16]. Optional values:" +
                "cloud: Basic cloud disk, cloud_efficiency: Ultra cloud disk, cloud_ssd: Cloud SSD, ephemeral_ssd: Ephemeral SSD" +
                "Default: cloud.";
        public static final String DATA_DISKS_ENCRYPTED_LIST_DESC = "Whether the data disk n is encrypted or not. " +
                "Valid values: 'true', 'false'" +
                "Default: false.";
        public static final String DATA_DISKS_SNAPSHOT_ID_LIST_DESC = "Snapshot is used to create the data disk. After the parameter DataDisk.n.SnapshotId is specified, " +
                "parameter DataDisk.n.Size is ignored, and the size of a new disk is the size of the specified snapshot." +
                "If the specified snapshot was created on or before July 15, 2013, this invocation is denied, " +
                "and an error InvalidSnapshot.TooOld is returned.";
        public static final String DATA_DISKS_DISK_NAME_LIST_DESC = "Name of a data disk." +
                "It can be [2, 128] characters in length. Must begin with an English letter or Chinese character. " +
                "It can contain digits, colons (:), underscores (_), or hyphens (-)." +
                "The data disk name is displayed in the ECS console." +
                "Cannot begin with \"http://\" or \"https://\"." +
                "Default value: ''";
        public static final String DATA_DISKS_DESCRIPTION_LIST_DESC = "Description of a data disk." +
                "It can be [2, 256] characters in length." +
                "The disk description is displayed in the console." +
                "It cannot begin with \"http://\" or \"https://\". " +
                "Default value: ''";
        public static final String DATA_DISKS_DELETE_WITH_INSTANCE_LIST_DESC = "Whether a data disk is released along with the instance or not." +
                "This parameter is only valid for an independent cloud disk, whose value of parameter DataDisk.n.Category is cloud, cloud_efficiency, or cloud_ssd. " +
                "If you specify a value to DataDisk.n.DeleteWithInstance for ephemeral_ssd, an error is returned. " +
                "Valid values: 'true', 'false' Default: 'true'";
        public static final String CLUSTER_ID_DESC = "The cluster ID to which the instance belongs.";
        public static final String HPC_CLUSTER_ID_DESC = "The cluster ID to which the instance belongs.";
        public static final String V_SWITCH_ID_DESC = "The VSwitch ID must be specified when you create a VPC-connected instance.";
        public static final String PRIVATE_IP_ADDRESS_DESC = "Private IP address of an ECS instance. PrivateIpAddress depends " +
                "on VSwitchId and cannot be specified separately";
        public static final String INSTANCE_CHARGE_TYPE_DESC = "Billing methods. Valid values: 'PrePaid', 'PostPaid' " +
                "Default: PostPaid";
        public static final String SPOT_STRATEGY_DESC = "The spot price you are willing to accept for a preemptible instance. " +
                "It takes effect only when parameter InstanceChargeType is PostPaid. Optional values:" +
                "NoSpot: A normal Pay-As-You-Go instance. " +
                "SpotWithPriceLimit: Sets the price threshold for a preemptible instance. " +
                "SpotAsPriceGo: A price that is based on the highest Pay-As-You-Go instance. " +
                "Default: 'NoSpot'";
        public static final String SPOT_PRICE_LIMIT_DESC = "The hourly price threshold for a preemptible instance, " +
                "and it takes effect only when parameter SpotStrategy is SpotWithPriceLimit. " +
                "Three decimal places are allowed at most.";
        public static final String PERIOD_DESC = "Unit: month. This parameter is valid and mandatory only when InstanceChargeType is set to PrePaid. " +
                "Valid values: '1-9', '12', '24', '36', '48', '60'";
        public static final String PERIOD_UNIT_DESC = "Value: Optional values: 'week', 'month'. Default: 'month'";
        public static final String AUTO_RENEW_DESC = "Whether to set AutoRenew. Whether to set AutoRenew. " +
                "This parameter is valid when InstanceChargeType is PrePaid. Valid values: true, false" +
                "Default: false.";
        public static final String AUTO_RENEW_PERIOD_DESC = "When AutoRenew is set to True, this parameter is required. " +
                "Valid values: '1', '2', '3', '6', '12'";
        public static final String USER_DATA_DESC = "The user data for an instance must be encoded in Base64 format. " +
                "The maximum size of the user-defined data is 16 KB.";
        public static final String CLIENT_TOKEN_DESC = "It is used to guarantee the idempotence of the request. " +
                "This parameter value is generated by the client and is guaranteed to be unique between different requests. " +
                "It can contain a maximum of 64 ASCII characters only. ";
        public static final String KEY_PAIR_NAME_DESC = "The name of the key pair." +
                "This parameter is valid only for a Linux instance. For a Windows ECS instance, if a value is set for parameter KeyPairName, " +
                "the password still takes effect. If a value is set for parameter KeyPairName, the Password still takes effect." +
                "The user name and password authentication method is disabled if a value is set for parameter KeyPairName for a Linux instance. Default: ''.";
        public static final String DEPLOYMENT_SET_ID_DESC = "Deployment Set ID. If you do not enter the value, 1 is used.";
        public static final String RAM_ROLE_NAME_DESC = "The RAM role name of the instance. ";
        public static final String SECURITY_ENHANCEMENT_STRATEGY_DESC = "Whether or not to enable security enhancement. Valid values: active, deactive";
        public static final String TAGS_KEY_LIST_DESC = "The key of a tag of which n is from 1 to 5. It cannot be an empty string. " +
                "Once you use this parameter, it cannot be a null string. " +
                "It can be up to 64 characters in length. It cannot start with \"aliyun\", \"acs:\", \"http://\", or \"https://\".";
        public static final String TAGS_VALUE_LIST_DESC = "The value of a tag of which n is from 1 to 5. It can be a null string." +
                " It can be up to 128 characters in length Seven characters. " +
                "It cannot begin with \"aliyun\", \"http://\", or \"https://\".";

        //Results
        public static final String RETURN_RESULT_DESC = "The authentication token in case of success, or an error" +
                " message in case of failure.";
        public static final String RETURN_CODE_DESC = "\"0\" if operation was successfully executed, \"-1\" otherwise.";
        public static final String REQUEST_ID_DESC = "Request ID. We return a unique RequestId for every API request, " +
                "whether the request is successful or not.";
        public static final String EXCEPTION_DESC = "Exception if there was an error when executing, empty otherwise.";
    }

    public static class CreateInstance {
        public static final String CREATE_INSTANCE_DESC = "Create an ECS instance.";
        public static final String SUCCESS_DESC = "The instance has been successfully created.";
        public static final String FAILURE_DESC = "An error has occurred while trying to create the instance.";
    }

    public static class DeleteInstance {
        public static final String DELETE_INSTANCE_DESC = "This operation is used to release a Pay-As-You-Go or expired " +
                "Subscription instance having the status Stopped.";
        public static final String SUCCESS_DESC = "The instance has been successfully deleted.";
        public static final String FAILURE_DESC = "An error has occurred while trying to delete the instance.";
    }

    public static class StartInstance {
        public static final String START_INSTANCE_DESC = "This operation is used to start a specified instance.";
        public static final String SUCCESS_DESC = "The instance has been successfully started.";
        public static final String FAILURE_DESC = "An error has occurred while trying to start the instance.";

        //Inputs
        public static final String INIT_LOCAL_DISK_DESC = "Recover to the previous normal status of instance local disk " +
                "when exceptions occurs. Valid values: 'true', 'false'";
    }

    public static class StopInstance {
        public static final String STOP_INSTANCE_DESC = "This operation is used to stop an ECS instance.";
        public static final String SUCCESS_DESC = "The instance has been successfully stopped.";
        public static final String FAILURE_DESC = "An error has occurred while trying to stop the instance.";

        //Inputs
        public static final String CONFIRM_STOP_DESC = "Whether to stop an I1 ECS instance or not.  " +
                "A required parameter for I1 type family instance, it only takes effect when the instance is of I1 type family." +
                "Valid values: true, false" +
                "Default value: false";
        public static final String STOPPED_MODE_DESC = "Whether a VPC ECS instance is billed after it is stopped or not. " +
                "Optional value: KeepCharging" +
                "After you enable the feature of No fees for stopped instances for a VPC instance, you can set StoppedMode=KeepCharging " +
                "to disable the feature, the ECS instance will be billed after it is stopped, " +
                " and its resource and Internet IP address are reserved.";
    }

    public static class RestartInstance {
        public static final String RESTART_INSTANCE_DESC = "This operation is used to restart an ECS instance.";
        public static final String SUCCESS_DESC = "The instance has been successfully restarted.";
        public static final String FAILURE_DESC = "An error has occurred while trying to restart the instance.";
    }
}
