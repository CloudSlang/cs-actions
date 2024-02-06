/*
 * Copyright 2024 Open Text
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




package io.cloudslang.content.google.utils.action

object Descriptions {

  object Common {
    final val PROJECT_ID_DESC = "Google Cloud project id. Example: example-project-a"
    final val ACCESS_TOKEN_DESC = "The access token returned by the GetAccessToken operation, with at least the following scope: https://www.googleapis.com/auth/cloud-platform"
    final val PROXY_HOST_DESC = "Proxy server used to connect to Google Cloud API. If empty no proxy will be used."
    final val PROXY_PORT_DESC = "Proxy server port used to access the provider services. Default: 8080"
    final val PROXY_USERNAME_DESC = "Proxy server user name"
    final val PROXY_PASSWORD_DESC = "Proxy server password associated with the <proxyUsername> input value."
    final val PRETTY_PRINT_DESC = "Whether to format (pretty print) the resulting json. Valid values: true or false Default: true"
    final val RETURN_RESULT_DESC = "If successful, returns the complete API response. In case of an error this output will contain the error message."
    final val STATUS_CODE_DESC = "The HTTP status code for the request."
    final val RETURN_CODE_DESC = "0 if operation was successfully executed, -1 otherwise."
    final val EXCEPTION_DESC = " The stack trace of the thrown error (if an error occurred)."
    final val ASYNC_DESC = "Boolean specifying whether the operation to run sync or async.Valid values: true, false Default: true"
    final val TIMEOUT_DESC = "The time, in seconds, to wait for a response if the async input is set to \"false\". If the value is 0, the operation will wait until zone operation progress is 100.Valid values: Any positive number including 0.Default: 30"
    final val POLLING_INTERVAL_DESC = "The time, in seconds, to wait before a new request that verifies if the operation finished is executed, if the async input is set to \"false\".Valid values: Any positive number including 0.Default: 1"
    final val STATUS_DESC =  " The status of the SQL instance if the async input is false, otherwise the status of the Operation."
    final val FILTER_DESC = "A filter expression that filters resources listed in the response. The expression is in the form of field:value. For example, 'instanceType:CLOUD_SQL_INSTANCE'. Fields can be nested as needed as per their JSON representation, such as 'settings.userLabels.auto_start:true'.Multiple filter queries are space-separated. For example. 'state:RUNNABLE instanceType:CLOUD_SQL_INSTANCE'. By default, each expression is an AND expression. However, you can include AND and OR expressions explicitly"
  }

  object SQLDataBaseInstances {
    final val CREATE_SQL_INSTANCE_OPERATION_DESCRIPTION = "Creates a resource containing information about a database inside a Google Cloud SQL instance."
    final val DELETE_SQL_INSTANCE_OPERATION_DESCRIPTION = "Deletes a Cloud SQL instance."
    final val GET_SQL_INSTANCE_OPERATION_DESCRIPTION = "Retrieves a resource containing information about a database inside a Google Cloud SQL instance."
    final val LIST_SQL_INSTANCES_OPERATION_DESCRIPTION = "Lists instances under a given project."
    final val START_SQL_INSTANCE_OPERATION_DESCRIPTION = "Starts a Cloud SQL instance."
    final val UPDATE_SQL_INSTANCE_OPERATION_DESCRIPTION = "Updates settings of a Cloud SQL instance. Using this operation might cause your instance to restart."
    final val STOP_SQL_INSTANCE_OPERATION_DESCRIPTION = "Stops a Cloud SQL instance."
    final val RESTART_SQL_INSTANCE_OPERATION_DESCRIPTION = "Restarts a Cloud SQL instance."
  }

  object CreateSQLDataBaseInstance {
    final val INSTANCE_ID_DESC = "Id of the Cloud SQL instance. Example: instance-1"
    final val INSTANCE_PASSWORD_DESC = "Initial root password. Use only on creation."
    final val ZONE_DESC = "The Compute Engine zone that the instance is currently serving from. This value could be different from the zone that was specified when the instance was created if the instance has failed over to its secondary zone."
    final val REGION_DESC = "The geographical region. Example: us-central1,asia-east1"
    final val DATABASE_VERSION_DESC = "The database engine type and version. The databaseVersion field cannot be changed after instance creation. Example: MySQL instances: MYSQL_8_0, MYSQL_5_7 (default), or MYSQL_5_6. PostgreSQL instances: POSTGRES_13, POSTGRES_12 (default), POSTGRES_11, POSTGRES_10, or POSTGRES_9_6.SQL Server instances: SQLSERVER_2017_STANDARD (default), SQLSERVER_2017_ENTERPRISE, SQLSERVER_2017_EXPRESS, or SQLSERVER_2017_WEB."
    final val MACHINE_TYPE_DESC = "The machine type for this instance. Example: db-n1-standard-1 (MySQL instances) or db-custom-1-3840 (PostgreSQL instances)."
    final val STORAGE_TYPE_DESC = "The type of data disk. Valid Values: PD_SSD or PD_HDD. Default: PD_SSD"
    final val STORAGE_CAPACITY_DESC = "The size of data disk, in GB. The data disk size minimum is 10GB and maximum is 30720GB. Default: 100GB"
    final val STORAGE_AUTO_RESIZE_DESC = "Configuration to increase storage size automatically. Valid values: true or false. Default: true"
    final val AVAILABILITY_TYPE_DESC = "Availability type. Potential values:ZONAL: The instance serves data from only one zone. Outages in that zone affect data accessibility.REGIONAL: The instance can serve data from more than one zone in a region (it is highly available).Default: REGIONAL"
    final val PREFERRED_MAINTENANCE_WINDOW_DAY_DESC = "The best day window for this instance to undergo routine maintenance. Day of week (1-7), starting on Monday.Default: 7"
    final val PREFERRED_MAINTENANCE_WINDOW_HOUR_DESC = "The best time window for this instance to undergo routine maintenance. Hour of day - 0 to 23. Default: 0"
    final val ACTIVATION_POLICY_DESC = "The activation policy specifies when the instance is activated; it is applicable only when the instance state is RUNNABLE. Valid values: ALWAYS: The instance is on, and remains so even in the absence of connection requests. NEVER: The instance is off; it is not activated, even if a connection request arrives. Default: ALWAYS"
    final val LABELS_DESC = "User-provided labels, represented as a dictionary where each label is a single key value pair. Example: { \"name\": \"wrench\", \"mass\": \"1.3kg\", \"count\": \"3\" }."
    final val CONNECTION_NAME_DESC = "Connection name of the Cloud SQL instance used in connection strings."
    final val PUBLIC_IP_ADDRESS_DESC = "The assigned public IP addresses for the instance."
    final val SELF_LINK_DESC = "The URI of this resource."
  }

  object UpdateSQLDataBaseInstance {
    final val INSTANCE_ID_DESC = "Id of the Cloud SQL instance. Example: instance-1"
    final val ZONE_DESC = "The Compute Engine zone that the instance is currently serving from. This value could be different from the zone that was specified when the instance was created if the instance has failed over to its secondary zone."
    final val MACHINE_TYPE_DESC = "The machine type for this instance. Example: db-n1-standard-1 (MySQL instances) or db-custom-1-3840 (PostgreSQL instances)."
    final val STORAGE_TYPE_DESC = "The type of data disk. Valid Values: PD_SSD or PD_HDD. Default: PD_SSD"
    final val STORAGE_CAPACITY_DESC = "The size of data disk, in GB. The data disk size minimum is 10GB and maximum is 30720GB. Default: 100GB"
    final val STORAGE_AUTO_RESIZE_DESC = "Configuration to increase storage size automatically. Valid values: true or false. Default: true"
    final val PRIVATE_NETWORK_DESC = "The resource link for the VPC network from which the Cloud SQL instance is accessible for private IP. For example, /projects/myProject/global/networks/default. This setting can be updated, but it cannot be removed after it is set."
    final val IS_IPV4_ENABLED_DESC = "Whether the instance is assigned a public IP address or not."
    final val AVAILABILITY_TYPE_DESC = "Availability type. Potential values:ZONAL: The instance serves data from only one zone. Outages in that zone affect data accessibility.REGIONAL: The instance can serve data from more than one zone in a region (it is highly available).Default: REGIONAL"
    final val IS_BACKUP_CONFIGURATION_ENABLED_DESC = "Whether this configuration is enabled.Whether this configuration is enabled."
    final val IS_BINARY_LOG_ENABLED_DESC = "Whether binary log is enabled. If backup configuration is disabled, binarylog must be disabled as well. This is only for MYSQL."
    final val PREFERRED_MAINTENANCE_WINDOW_DAY_DESC = "The best day window for this instance to undergo routine maintenance. Day of week (1-7), starting on Monday.Default: 7"
    final val PREFERRED_MAINTENANCE_WINDOW_HOUR_DESC = "The best time window for this instance to undergo routine maintenance. Hour of day - 0 to 23. Default: 0"
    final val ACTIVATION_POLICY_DESC = "The activation policy specifies when the instance is activated; it is applicable only when the instance state is RUNNABLE. Valid values: ALWAYS: The instance is on, and remains so even in the absence of connection requests. NEVER: The instance is off; it is not activated, even if a connection request arrives. Default: ALWAYS"
    final val LABELS_DESC = "User-provided labels, represented as a dictionary where each label is a single key value pair. Example: { \"name\": \"wrench\", \"mass\": \"1.3kg\", \"count\": \"3\" }."
    final val PUBLIC_IP_ADDRESS_DESC = "The assigned public IP address for the instance."
    final val PRIVATE_IP_ADDRESS_DESC = "The assigned private IP address for the instance."
    final val SELF_LINK_DESC = "The URI of the resource."
  }

  object StorageBucketDesc{
    final val CREATE_BUCKET_OPERATION_DESC = "Creates a bucket inside a Google Cloud Storage."
    final val GET_BUCKET_OPERATION_DESC = "Retrieves a resource containing information about a bucket inside a Google Cloud Storage."
    final val BUCKET_NAME_DESC= "Name of a bucket."
    final val METAGENERATION_MATCH_DESC = "Makes the return of the bucket metadata conditional on whether the bucket's current metageneration matches the given value."
    final val METAGENERATION_NOT_MATCH_DESC = "Makes the return of the bucket metadata conditional on whether the bucket's current metageneration does not match the given value."
    final val PROJECTION_DESC = "Set of properties to return. Defaults to noAcl.\n\nAcceptable values are:\n\"full\": Include all properties.\n\"noAcl\": Omit owner, acl and defaultObjectAcl properties."
    final val LIST_BUCKET_OPERATION_DESC = "Retrieves a list of buckets for a given project, ordered in the list lexicographically by name."
    final val MAX_RESULTS_DESC = "Maximum number of buckets to return in a single response. The service will use the smaller of this parameter."
    final val PREFIX_DESC = "Filter results to buckets whose names begin with this prefix."
    final val PAGE_TOKEN_DESC = "The pageToken is an encoded field containing the name of the last item (bucket) in the returned list. In a subsequent request using the pageToken, items that come after the pageToken are shown (up to maxResults)."
    final val DELETE_BUCKET_OPERATION_DESC = "Permanently deletes an empty bucket."
    final val DELETE_BUCKET_SUCCESS_DESC = " bucket is deleted successfully "
    final val PREDEFINED_ACL_DESC = "Applies a predefined set of access controls to this bucket."
    final val PREDEFINED_DEFAULT_OBJECT_ACL_DESC = "Applies a predefined set of default object access controls to this bucket."
    final val RETENTION_PERIOD_TYPE_DESC = "The type of period of time. Valid values are 'seconds','days','months','years'. Default: 'seconds' "
    final val RETENTION_PERIOD_DESC = "The period of time, in seconds, that objects in the bucket must be retained and cannot be deleted, replaced, or made noncurrent. The value must be less than 3,155,760,000 seconds."
    final val LOCATION_DESC = "Location where the bucket gets created."
    final val LOCATION_TYPE_DESC = "Type of Location. Multi-region - highest availability across largest area, Dual-region - high availability and low latency across 2 regions, Region - it gives lowest latency across single region"
    final val IS_VERSIONING_ENABLED_DESC = "While set to true, versioning is fully enabled for this bucket."
    final val STORAGE_CLASS_DESC = "The bucket's default storage class, used whenever no storageClass is specified for a newly-created object. Available storage classes are 'STANDARD','NEARLINE','COLDLINE','ARCHIVE'."
    final val LABELS_DESC = "User-provided labels, represented as a dictionary where each label is a single key value pair. Example: { \"name\": \"wrench\", \"mass\": \"1.3kg\", \"count\": \"3\" }."
    final val ACCESS_CONTROL_TYPE_DESC = "Whether the bucket uses uniform bucket-level access or fine-grained bucket-level access. If set, access checks only use bucket-level IAM policies or above. Valid values are 'Uniform' and 'Fine-grained'. "
    final val IS_DEFAULT_EVENT_BASED_HOLD_ENABLED_DESC = "Whether or not to automatically apply an eventBasedHold to new objects added to the bucket."
  }

  object UpdateStorageBucketDesc {
    final val UPDATE_BUCKET_OPERATION_DESC = "Updates a bucket. Changes to the bucket are readable immediately after writing, but configuration changes may take time to propagate.";

    final val BUCKET_NAME_DESC= "Name of a bucket."
    final val METAGENERATION_MATCH_DESC = "Makes the return of the bucket metadata conditional on whether the bucket's current metageneration matches the given value."
    final val METAGENERATION_NOT_MATCH_DESC = "Makes the return of the bucket metadata conditional on whether the bucket's current metageneration does not match the given value."
    final val PREDEFINED_ACL_DESC = "Apply a predefined set of access controls to this bucket. Acceptable values are: authenticatedRead,private,projectPrivate,publicRead,publicReadWrite. If iamConfiguration.uniformBucketLevelAccess.enabled is set to true, requests that include this parameter fail with a 400 Bad Request response."
    final val PREDEFINED_DEFAULT_OBJECT_ACL_DESC = "Apply a predefined set of default object access controls to this bucket. Acceptable values are: authenticatedRead,bucketOwnerFullControl,bucketOwnerRead,private,projectPrivate,publicRead. If iamConfiguration.uniformBucketLevelAccess.enabled is set to true, requests that include this parameter fail with a 400 Bad Request response."

    final val PROJECTION_DESC = "Set of properties to return. Defaults to noAcl. Acceptable values are:'full': Include all properties.'noAcl': Omit owner, acl and defaultObjectAcl properties."
    final val STORAGE_CLASS_DESC = "The bucket's default storage class, used whenever no storageClass is specified for a newly-created object. Available storage classes are 'STANDARD','NEARLINE','COLDLINE','ARCHIVE'."
    final val LABELS_DESC = "The user-provided bucket labels, in key/value pairs."
    final val IS_DEFAULT_EVENT_BASED_HOLD_ENABLED_DESC = "Whether or not to automatically apply an eventBasedHold to new objects added to the bucket."
    final val IS_VERSIONING_ENABLED_DESC = "While set to true, versioning is fully enabled for this bucket."
    final val ACCESS_CONTROL_TYPE_DESC = "Whether the bucket uses uniform bucket-level access or fine-grained bucket-level access. If set, access checks only use bucket-level IAM policies or above. Valid values are 'Uniform' and 'Fine-grained'. "
    final val RETENTION_PERIOD_TYPE_DESC = "The type of period of time. Valid values are 'seconds','days','months','years'. Default: 'seconds' "
    final val RETENTION_PERIOD_DESC = "The period of time, in seconds, that objects in the bucket must be retained and cannot be deleted, replaced, or made noncurrent. The value must be less than 3,155,760,000 seconds."
    final val REMOVE_RETENTION_POLICY_DESC = "Whether to delete retention policy of the bucket or not."
    final val LOCATION_DESC = "The location of the bucket. Object data for objects in the bucket resides in physical storage within this region."
    final val LOCATION_TYPE_DESC = "The type of location that the bucket resides in, as determined by the location property."
    final val SELF_LINK_DESC = "The URI of the bucket."
  }
}
