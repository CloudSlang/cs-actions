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

package io.cloudslang.content.google.utils.action

/**
  * Created by victor on 28.02.2017.
  */
object InputNames {
  final val PROJECT_ID = "projectId"
  final val JSON_TOKEN = "jsonToken"
  final val ZONE = "zone"
  final val ACCESS_TOKEN = "accessToken"
  final val PROXY_HOST = "proxyHost"
  final val PROXY_PORT = "proxyPort"
  final val PROXY_USERNAME = "proxyUsername"
  final val PROXY_PASSWORD = "proxyPassword"
  final val PRETTY_PRINT = "prettyPrint"
  final val STATUS_CODE = "statusCode"
  final val TIMEOUT = "timeout"
  final val SCOPES = "scopes"
  final val SCOPES_DELIMITER = "scopesDelimiter"
  final val ITEMS_DELIMITER = "itemsDelimiter"
  final val INSTANCE_NAME = "instanceName"
  final val INSTANCE_DESCRIPTION = "instanceDescription"
  final val ITEMS_KEYS_LIST = "itemsKeysList"
  final val ITEMS_VALUES_LIST = "itemsValuesList"
  final val DISK_NAME = "diskName"
  final val NETWORK_NAME = "networkName"
  final val NETWORK_DESCRIPTION = "networkDescription"
  final val AUTO_CREATE_SUBNETWORKS = "autoCreateSubnetworks"
  final val IPV4_RANGE = "ipV4Range"
  final val ZONE_OPERATION_NAME = "zoneOperationName"
  final val GLOBAL_OPERATION_NAME = "globalOperationName"
  final val TAGS_LIST = "tagsList"
  final val TAGS_DELIMITER = "tagsDelimiter"
  final val DISK_SIZE = "diskSize"
  final val DISK_DESCRIPTION = "diskDescription"
  final val LICENSES = "licensesList"
  final val LICENSES_DELIMITER = "licensesDelimiter"
  final val SOURCE_IMAGE = "sourceImage"
  final val IMAGE_ENCRYPTION_KEY = "imageEncryptionKey"
  final val DISK_ENCRYPTION_KEY = "diskEncryptionKey"
  final val SNAPSHOT_IMAGE = "snapshotImage"
  final val DISK_TYPE = "diskType"
  final val FILTER = "filter"
  final val ORDER_BY = "orderBy"

  final val LIST_DELIMITER = "listDelimiter"
  final val MACHINE_TYPE = "machineType"
  final val CAN_IP_FORWARD = "canIpForward"


  final val METADATA_KEYS = "metadataKeys"
  final val METADATA_VALUES = "metadataValues"

  final val VOLUME_SOURCE = "volumeSource"
  final val VOLUME_MOUNT_TYPE = "volumeMountType"
  final val VOLUME_MOUNT_MODE = "volumeMountMode"
  final val VOLUME_AUTO_DELETE = "volumeAutoDelete"
  final val VOLUME_DISK_DEVICE_NAME = "volumeDiskDeviceName"
  final val VOLUME_DISK_NAME = "volumeDiskName"
  final val VOLUME_DISK_SOURCE_IMAGE = "volumeDiskSourceImage"
  final val VOLUME_DISK_TYPE = "volumeDiskType"
  final val VOLUME_DISK_SIZE = "volumeDiskSize"

  final val NETWORK = "network"
  final val SUBNETWORK = "subnetwork"
  final val ACCESS_CONFIG_NAME = "accessConfigName"
  final val ACCESS_CONFIG_TYPE = "accessConfigType"

  final val SCHEDULING_ON_HOST_MAINTENANCE = "schedulingOnHostMaintenance"
  final val SCHEDULING_AUTOMATIC_RESTART = "schedulingAutomaticRestart"
  final val SCHEDULING_PREEMPTIBLE = "schedulingPreemptible"

  final val SERVICE_ACCOUNT_EMAIL = "serviceAccountEmail"
  final val SERVICE_ACCOUNT_SCOPES = "serviceAccountScopes"

  final val ASYNC = "async"
  final val POLLING_INTERVAL = "pollingInterval"

  final val USERNAME = "username"
  final val EMAIL = "email"
  final val SYNC_TIME = "syncTime"

  final val SOURCE = "source"
  final val MODE = "mode"
  final val BOOT = "boot"
  final val AUTO_DELETE = "autoDelete"
  final val INTERFACE = "interface"
  final val DEVICE_NAME = "deviceName"

  final val CONSOLE_PORT = "consolePort"
  final val START_INDEX = "startIndex"

  object CreateSQLDatabaseInstanceInputs {

    final val INSTANCE_ID = "instanceId"
    final val INSTANCE_PASSWORD = "databaseInstancePassword"
    final val ZONE = "zone"
    final val REGION = "region"
    final val DATABASE_VERSION = "databaseVersion"
    final val MACHINE_TYPE = "machineType"
    final val STORAGE_TYPE = "storageType"
    final val STORAGE_CAPACITY = "storageCapacity"
    final val STORAGE_AUTO_RESIZE = "storageAutoResize"
    final val AVAILABILITY_TYPE = "availabilityType"
    final val PREFERRED_MAINTENANCE_WINDOW_DAY = "preferredMaintenanceWindowDay"
    final val PREFERRED_MAINTENANCE_WINDOW_HOUR = "preferredMaintenanceWindowHour"
    final val ACTIVATION_POLICY = "activationPolicy"
    final val LABELS = "labels"

    final val ACTIVATION_POLICY_NEVER ="NEVER"
    final val ACTIVATION_POLICY_ALWAYS ="ALWAYS"
    final val START_INSTANCE ="START"
    final val STOP_INSTANCE ="STOP"
    final val RESTART_INSTANCE ="RESTART"
    final val MYSQL_CONST = "MYSQL"
    final val MAX_RESULTS = "maxResults"
    final val SQL_INSTANCE_RUNNING = "RUNNING"
    final val SQL_INSTANCE_STOPPED = "STOPPED"

  }

  object UpdateSQLDatabaseInstanceInputs {
    final val INSTANCE_ID = "instanceId"
    final val ZONE = "zone"
    final val MACHINE_TYPE = "machineType"
    final val SETTINGS_VERSION = "settingsVersion"
    final val STORAGE_AUTO_RESIZE = "storageAutoResize"
    final val STORAGE_CAPACITY = "storageCapacity"
    final val ACTIVATION_POLICY = "activationPolicy"
    final val PRIVATE_NETWORK = "privateNetwork"
    final val IS_IPV4_ENABLED = "isIPV4Enabled"
    final val AVAILABILITY_TYPE = "availabilityType"
    final val RETAINED_BACKUPS = "retainedBackups"
    final val RETENTION_UNIT = "retentionUnit"
    final val IS_BACKUP_CONFIGURATION_ENABLED = "isBackupConfigurationEnabled"
    final val IS_BINARY_LOG_ENABLED = "isBinaryLogEnabled"
    final val TRANSACTION_LOG_RETENTION_DAYS = "transactionLogRetentionDays"
    final val PREFERRED_MAINTENANCE_WINDOW_DAY = "preferredMaintenanceWindowDay"
    final val PREFERRED_MAINTENANCE_WINDOW_HOUR = "preferredMaintenanceWindowHour"
    final val LABELS = "labels"
  }

  object StorageBucketInputs {
    final val GET_BUCKET_OPERATION_NAME = "Get Bucket"
    final val BUCKET_NAME = "bucketName"
    final val METAGENERATION_MATCH = "metagenerationMatch"
    final val METAGENERATION_NOT_MATCH = "metagenerationNotMatch"
    final val PROJECTION = "projection"
    final val LIST_BUCKET_OPERATION_NAME = "List Bucket"
    final val MAX_RESULTS= "maxResults"
    final val PREFIX= "prefix"
    final val PAGE_TOKEN= "pageToken"
    final val DELETE_BUCKET_OPERATION_NAME = "Delete Bucket"
  }

  object UpdateStorageBucketInputs {

    final val BUCKET_NAME = "bucketName"
    final val METAGENERATION_MATCH = "metagenerationMatch"
    final val METAGENERATION_NOT_MATCH = "metagenerationNotMatch"
    final val PREDEFINED_ACL = "predefinedAcl"
    final val PREDEFINED_DEFAULT_OBJECT_ACL = "predefinedDefaultObjectAcl"
    final val PROJECTION = "projection"
    final val STORAGE_CLASS = "storageClass"
    final val LABELS = "labels"
    final val IS_DEFAULT_EVENT_BASED_HOLD_ENABLED = "isDefaultEventBasedHoldEnabled"
    final val IS_VERSIONING_ENABLED = "isVersioningEnabled"
    final val ACCESS_CONTROL_TYPE = "accessControlType"
    final val RETENTION_PERIOD_TYPE = "retentionPeriodType"
    final val RETENTION_PERIOD = "retentionPeriod"
    final val REMOVE_RETENTION_POLICY = "removeRetentionPolicy"
    final val IS_RETENTION_POLICY_LOCKED = "isRetentionPolicyLocked"
  }
}
