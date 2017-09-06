package io.cloudslang.content.google.actions.compute.compute_engine.instances

import java.util

import com.google.api.services.compute.model._
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType
import io.cloudslang.content.constants.BooleanValues.{FALSE, TRUE}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.disks.DiskController
import io.cloudslang.content.google.services.compute.compute_engine.instances.{InstanceController, InstanceService}
import io.cloudslang.content.google.services.compute.compute_engine.networks.NetworkController
import io.cloudslang.content.google.utils.Constants.{COMMA, NEW_LINE, TIMEOUT_EXCEPTION}
import io.cloudslang.content.google.utils.action.DefaultValues._
import io.cloudslang.content.google.utils.action.GoogleOutputNames.{ZONE_OPERATION_NAME => _, _}
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.{convertSecondsToMilli, verifyEmpty}
import io.cloudslang.content.google.utils.action.InputValidator._
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.{toDouble, toInteger, toLong}
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.concurrent.TimeoutException
import scala.language.postfixOps

/**
  * Created by victor on 01.03.2017.
  */
class InstancesInsert {

  /**
    * Creates an instance resource in the specified project using the data included as inputs.
    *
    * @param accessToken                 The access token from GetAccessToken.
    * @param projectId                   Name of the Google Cloud project.
    * @param zone                        Name of the zone for this request.
    * @param instanceName                The name that the new instance will have
    * @param instanceDescription         Optional - The description of the new instance
    * @param machineType                 Full or partial URL of the machine type resource to use for this instance, in the format:
    *                                    "zones/zone/machineTypes/machine-type".
    *                                    Example: "zones/us-central1-f/machineTypes/n1-standard-1"
    * @param listDelimiter               Optional - The delimiter to split all the lists from the inputs
    *                                    Default: ","
    * @param canIpForward                Optional - Boolean that specifies if the instance is allowed to send and receive packets
    *                                    with non-matching destination or source IPs. This is required if you plan to use this instance
    *                                    to forward routes
    *                                    Default: "true"
    *                                    Valid values: "true", "false"
    * @param metadataKeys                Optional - The keys for the metadata entry, separated by the <listDelimiter> delimiter.
    *                                    Keys must conform to the following regexp: [a-zA-Z0-9-_]+, and be less than 128 bytes in
    *                                    length. This is reflected as part of a URL in the metadata server. Additionally,
    *                                    to avoid ambiguity, keys must not conflict with any other metadata keys for the project.
    *                                    The length of the itemsKeysList must be equal with the length of the itemsValuesList.
    * @param metadataValues              Optional - The values for the metadata entry, separated by the <listDelimiter> delimiter.
    *                                    These are free-form strings, and only have meaning as interpreted by the image running in
    *                                    the instance. The only restriction placed on values is that their size must be less than
    *                                    or equal to 32768 bytes. The length of the
    *                                    itemsKeysList must be equal with the length of the itemsValuesList.
    * @param tagsList                    Optional - List of tags, separated by the <listDelimiter> delimiter. Tags are used to
    *                                    identify valid sources or targets for network firewalls and are specified by the client
    *                                    during instance creation. The tags can be later modified by the setTags method. Each tag
    *                                    within the list must comply with RFC1035.
    * @param volumeSource                Optional - A valid partial or full URL to an existing Persistent Disk resource.
    *                                    Note: If this input is specified, the disk will be used as a boot disk for the
    *                                    newly created instance, and <volumeDiskSourceImage> cannot be specified.
    *                                    Also <volumeMountType>, <volumeDiskName>, <volumeDiskType> and <volumeDiskSize>
    *                                    will be ignored.
    * @param volumeMountType             Optional - Specifies the type of the disk, either SCRATCH or PERSISTENT.
    *                                    Default: "PERSISTENT"
    * @param volumeMountMode             Optional - The mode in which to attach this disk, either READ_WRITE or READ_ONLY.
    *                                    Default: "READ_WRITE"
    * @param volumeAutoDelete            Optional - Boolean that specifies whether the disk will be auto-deleted when the instance
    *                                    is deleted (but not when the disk is detached from the instance).
    *                                    Default: "true"
    *                                    Valid values: "true", "false"
    * @param volumeDiskDeviceName        Optional - Specifies a unique device name of your choice that is reflected into the
    *                                    /dev/disk/by-id/google-* tree of a Linux operating system running within the instance.
    *                                    This name can be used to reference the device for mounting, resizing, and so on, from
    *                                    within the instance. If not specified, the server chooses a default device name to apply
    *                                    to this disk, in the form persistent-disks-x, where x is a number assigned by Google
    *                                    Compute Engine. This field is only applicable for persistent disks.
    * @param volumeDiskName              Optional - Specifies the disk name. If not specified, the default is to use the name of
    *                                    the instance.
    * @param volumeDiskSourceImage       The source image to create this disk. To create a disk with one of the public operating
    *                                    system images, specify the image by its family name. For example, specify family/debian-8
    *                                    to use the latest Debian 8 image:
    *                                    "projects/debian-cloud/global/images/family/debian-8"
    *                                    Alternatively, use a specific version of a public operating system image:
    *                                    "projects/debian-cloud/global/images/debian-8-jessie-vYYYYMMDD"
    *                                    To create a disk with a private image that you created, specify the image name in the
    *                                    following format:
    *                                    "global/images/my-private-image"
    *                                    You can also specify a private image by its image family, which returns the latest version
    *                                    of the image in that family. Replace the image name with family/family-name:
    *                                    "global/images/family/my-private-family"
    *                                    Note: This input cannot be specified if <volumeSource> is also specified.
    * @param volumeDiskType              Optional - Specifies the disk type to use to create the instance. If you define this input,
    *                                    you can provide either the full or partial URL. For example, the following are valid values:
    *                                    Note that for InstanceTemplate, this is the name of the disk type, not URL.
    *                                    Default: pd-standard, specified using the full URL
    *                                    Valid values: pd-ssd and local-ssd specified using the full/partial URL
    *                                    Example: "https://www.googleapis.com/compute/v1/projects/project/zones/zone/diskTypes/pd-standard",
    *                                    "projects/project/zones/zone/diskTypes/diskType/pd-standard",
    *                                    "zones/zone/diskTypes/diskType/pd-standard"
    * @param volumeDiskSize              Optional - Specifies the size in GB of the disk on which the system will be installed
    *                                    Constraint: Number greater or equal with 10
    *                                    Default: "10"
    * @param network                     Optional - URL of the network resource for this instance. When creating an instance, if neither the
    *                                    network nor the subnetwork is specified, the default network global/networks/default is used;
    *                                    if the network is not specified but the subnetwork is specified, the network is inferred.
    *                                    This field is optional when creating a firewall rule. If not specified when creating a firewall
    *                                    rule, the default network global/networks/default is used.
    *                                    If you specify this property, you can specify the network as a full or partial URL. For
    *                                    example, the following are all valid URLs:   -
    *                                    https://www.googleapis.com/compute/v1/projects/project/global/networks/network  -
    *                                    projects/project/global/networks/network  - global/networks/default
    * @param subnetwork                  Optional - The URL of the Subnetwork resource for this instance. If the network resource is in legacy
    *                                    mode, do not provide this property. If the network is in auto subnet mode, providing the
    *                                    subnetwork is optional. If the network is in custom subnet mode, then this field should be
    *                                    specified. If you specify this property, you can specify the subnetwork as a full or partial
    *                                    URL. For example, the following are all valid URLs: -
    *                                    https://www.googleapis.com/compute/v1/projects/project/regions/region/subnetworks/subnetwork  -
    *                                    regions/region/subnetworks/subnetwork
    * @param accessConfigName            Optional - Name of this access configuration. If specified, then the accessConfigType
    *                                    will be taken into account, otherwise not.
    * @param accessConfigType            Optional - The type of configuration.
    *                                    Valid values: "ONE_TO_ONE_NAT"
    *                                    Default: "ONE_TO_ONE_NAT"
    * @param schedulingOnHostMaintenance Optional - Defines the maintenance behavior for this instance. For standard instances, the default
    *                                    behavior is MIGRATE. For preemptible instances, the default and only possible behavior is
    *                                    TERMINATE.
    * @param schedulingAutomaticRestart  Optional - Boolean specifying whether the instance should be automatically restarted if it is terminated by Compute
    *                                    Engine (not terminated by a user). You can only set the automatic restart option for standard
    *                                    instances. Preemptible instances cannot be automatically restarted.
    *                                    Valid values: "true", "false"
    *                                    Default: "true"
    * @param schedulingPreemptible       Optional - Boolean specifying whether the instance is preemptible.
    *                                    Valid values: "true", "false"
    *                                    Default: "false"
    * @param serviceAccountEmail         Optional - Email address of the service account
    *                                    Default: The service account that was used to generate the token
    * @param serviceAccountScopes        Optional - The list of scopes to be made available for this service account.
    * @param syncInp                     Optional - Boolean specifying whether the operation to run sync or async.
    *                                    Valid values: "true", "false"
    *                                    Default: "false"
    * @param timeoutInp                  Optional - The time, in seconds, to wait for a response if the sync input is set to "true".
    *                                    If the value is 0, the operation will wait until zone operation progress is 100.
    *                                    Valid values: Any positive number including 0.
    *                                    Default: "30"
    * @param pollingIntervalInp          Optional - The time, in seconds, to wait before a new request that verifies if the operation finished
    *                                    is executed, if the sync input is set to "true".
    *                                    Valid values: Any positive number including 0.
    *                                    Default: "1"
    * @param proxyHost                   Optional - Proxy server used to connect to Google Cloud API. If empty no proxy will
    *                                    be used.
    * @param proxyPortInp                Optional - Proxy server port.
    *                                    Default: "8080"
    * @param proxyUsername               Optional - Proxy server user name.
    * @param proxyPasswordInp            Optional - Proxy server password associated with the proxyUsername input value.
    * @param prettyPrintInp              Optional - Whether to format (pretty print) the resulting json.
    *                                    Valid values: "true", "false"
    *                                    Default: "true"
    * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
    *         operation, status of the ZoneOperation if the <syncInp> is false. If <syncInp> is true the map will also
    *         contain the instance id, the name of the instance, a list of IPs separated by <listDelimiter> and the
    *         status of the operation will be replaced by the status of the instance.
    *         In case an exception occurs the failure message is provided.
    */
  @Action(name = "Insert Instance",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(INSTANCE_ID),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(ZONE_OPERATION_NAME),
      new Output(NAME),
      new Output(IPS),
      new Output(STATUS)

    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,

              @Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
              @Param(value = INSTANCE_DESCRIPTION) instanceDescription: String,
              @Param(value = MACHINE_TYPE, required = true) machineType: String,
              @Param(value = LIST_DELIMITER) listDelimiter: String,
              @Param(value = CAN_IP_FORWARD) canIpForward: String,

              @Param(value = METADATA_KEYS) metadataKeys: String,
              @Param(value = METADATA_VALUES) metadataValues: String,

              @Param(value = TAGS_LIST) tagsList: String,

              @Param(value = VOLUME_SOURCE) volumeSource: String,
              @Param(value = VOLUME_MOUNT_TYPE) volumeMountType: String,
              @Param(value = VOLUME_MOUNT_MODE) volumeMountMode: String,
              @Param(value = VOLUME_AUTO_DELETE) volumeAutoDelete: String,
              @Param(value = VOLUME_DISK_DEVICE_NAME) volumeDiskDeviceName: String,
              @Param(value = VOLUME_DISK_NAME) volumeDiskName: String,
              @Param(value = VOLUME_DISK_SOURCE_IMAGE) volumeDiskSourceImage: String,
              @Param(value = VOLUME_DISK_TYPE) volumeDiskType: String,
              @Param(value = VOLUME_DISK_SIZE) volumeDiskSize: String,

              @Param(value = NETWORK) network: String,
              @Param(value = SUBNETWORK) subnetwork: String,
              @Param(value = ACCESS_CONFIG_NAME) accessConfigName: String,
              @Param(value = ACCESS_CONFIG_TYPE) accessConfigType: String,

              @Param(value = SCHEDULING_ON_HOST_MAINTENANCE) schedulingOnHostMaintenance: String,
              @Param(value = SCHEDULING_AUTOMATIC_RESTART) schedulingAutomaticRestart: String,
              @Param(value = SCHEDULING_PREEMPTIBLE) schedulingPreemptible: String,

              @Param(value = SERVICE_ACCOUNT_EMAIL) serviceAccountEmail: String,
              @Param(value = SERVICE_ACCOUNT_SCOPES) serviceAccountScopes: String,

              @Param(value = SYNC) syncInp: String,
              @Param(value = TIMEOUT) timeoutInp: String,
              @Param(value = POLLING_INTERVAL) pollingIntervalInp: String,

              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)
    val volumeMountTypeStr = defaultIfEmpty(volumeMountType, DEFAULT_VOLUME_MOUNT_TYPE)
    val volumeMountModeStr = defaultIfEmpty(volumeMountMode, DEFAULT_VOLUME_MOUNT_MODE)
    val volumeAutoDeleteStr = defaultIfEmpty(volumeAutoDelete, TRUE)
    val volumeDiskNameStr = defaultIfEmpty(volumeDiskName, instanceName)
    val volumeDiskSizeStr = defaultIfEmpty(volumeDiskSize, DEFAULT_DISK_SIZE)
    val volumeDiskDeviceNameOpt = verifyEmpty(volumeDiskDeviceName)
    val volumeDiskTypeOpt = verifyEmpty(volumeDiskType)
    val canIpForwardStr = defaultIfEmpty(canIpForward, TRUE)
    val listDelimiterStr = defaultIfEmpty(listDelimiter, COMMA)
    val instanceDescriptionStr = defaultIfEmpty(instanceDescription, EMPTY)
    val metadataKeysStr = defaultIfEmpty(metadataKeys, EMPTY)
    val metadataValuesStr = defaultIfEmpty(metadataValues, EMPTY)
    val tagsListStr = defaultIfEmpty(tagsList, EMPTY)
    val networkOpt = verifyEmpty(network)
    val subnetworkOpt = verifyEmpty(subnetwork)
    val accessConfigNameOpt = verifyEmpty(accessConfigName)
    val accessConfigTypeStr = defaultIfEmpty(accessConfigType, DEFAULT_ACCESS_CONFIG_TYPE)
    val volumeSourceOpt = verifyEmpty(volumeSource)
    val volumeDiskSourceImageOpt = verifyEmpty(volumeDiskSourceImage)

    val schedulingOnHostMaintenanceOpt = verifyEmpty(schedulingOnHostMaintenance)
    val schedulingAutomaticRestartStr = defaultIfEmpty(schedulingAutomaticRestart, TRUE)
    val schedulingPreemptibleStr = defaultIfEmpty(schedulingPreemptible, FALSE)

    val serviceAccountEmailOpt = verifyEmpty(serviceAccountEmail)
    val serviceAccountScopesOpt = verifyEmpty(serviceAccountScopes)

    val syncStr = defaultIfEmpty(syncInp, FALSE)
    val timeoutStr = defaultIfEmpty(timeoutInp, DEFAULT_SYNC_TIMEOUT)
    val pollingIntervalStr = defaultIfEmpty(pollingIntervalInp, DEFAULT_POLLING_INTERVAL)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateBoolean(volumeAutoDeleteStr, VOLUME_AUTO_DELETE) ++
      validateDiskSize(volumeDiskSizeStr, VOLUME_DISK_SIZE) ++
      validateBoolean(canIpForwardStr, CAN_IP_FORWARD) ++
      validatePairedLists(metadataKeysStr, metadataValuesStr, listDelimiterStr, METADATA_KEYS, METADATA_VALUES) ++
      validateBoolean(schedulingAutomaticRestartStr, SCHEDULING_AUTOMATIC_RESTART) ++
      validateBoolean(schedulingPreemptibleStr, SCHEDULING_PREEMPTIBLE) ++
      validateBoolean(syncStr, SYNC) ++
      validateRequiredExclusion(volumeSourceOpt, volumeDiskSourceImageOpt, VOLUME_SOURCE, VOLUME_DISK_SOURCE_IMAGE) ++
      validateNonNegativeLong(timeoutStr, TIMEOUT) ++
      validateNonNegativeDouble(pollingIntervalStr, POLLING_INTERVAL)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    try {
      val proxyPort = toInteger(proxyPortStr)
      val prettyPrint = toBoolean(prettyPrintStr)
      val volumeAutoDelete = toBoolean(volumeAutoDeleteStr)
      val volumeDiskSize = toInteger(volumeDiskSizeStr)

      val schedulingAutomaticRestart = toBoolean(schedulingAutomaticRestartStr)
      val schedulingPreemptible = toBoolean(schedulingPreemptibleStr)
      val canIpForward = toBoolean(canIpForwardStr)
      val sync = toBoolean(syncStr)
      val timeout = toLong(timeoutStr)
      val pollingIntervalMilli = convertSecondsToMilli(toDouble(pollingIntervalStr))

      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val attachedDisk = volumeSourceOpt match {
        case Some(_) => DiskController.createAttachedDisk(
          boot = true,
          autoDelete = volumeAutoDelete,
          mountMode = volumeMountModeStr,
          sourceOpt = volumeSourceOpt,
          deviceNameOpt = volumeDiskDeviceNameOpt
        )
        case None => DiskController.createAttachedDisk(
          boot = true,
          autoDelete = volumeAutoDelete,
          mountMode = volumeMountModeStr,
          deviceNameOpt = volumeDiskDeviceNameOpt,
          mountTypeOpt = Some(volumeMountTypeStr),
          initializeParamsOpt = Some(
            DiskController.createAttachedDiskInitializeParams(
              diskName = volumeDiskNameStr,
              diskSourceImage = volumeDiskSourceImage,
              diskTypeOpt = volumeDiskTypeOpt,
              diskSize = volumeDiskSize
            )
          )
        )
      }


      val metadata = new Metadata().setItems(InstanceController.createMetadataItems(metadataKeysStr, metadataValuesStr, listDelimiterStr))
      val tags = InstanceController.createTags(tagsListStr, listDelimiterStr)

      val networkInterface = NetworkController.createNetworkInterface(
        networkOpt = networkOpt,
        subNetworkOpt = subnetworkOpt,
        accessConfigNameOpt = accessConfigNameOpt,
        accessConfigType = accessConfigTypeStr)

      val serviceAccount = InstanceController.getServiceAccount(serviceAccountEmailOpt, serviceAccountScopesOpt, listDelimiterStr)
      val scheduler = InstanceController.createScheduling(schedulingOnHostMaintenanceOpt, schedulingAutomaticRestart, schedulingPreemptible)

      val instance = InstanceController.createInstance(
        instanceName = instanceName,
        description = instanceDescriptionStr,
        zone = zone,
        schedulingOpt = scheduler,
        machineType = machineType,
        metadata = metadata,
        tags = tags,
        bootDisk = attachedDisk,
        networkInterface = networkInterface,
        canIpForward = canIpForward,
        serviceAccountOpt = serviceAccount)

      val operation = InstanceService.insert(httpTransport, jsonFactory, credential, projectId, zone, instance, sync, timeout, pollingIntervalMilli)
      val resultMap = getSuccessResultsMap(toPretty(prettyPrint, operation)) + (ZONE_OPERATION_NAME -> operation.getName)

      if (sync) {
        val instance = InstanceService.get(httpTransport, jsonFactory, credential, projectId, zone, instanceName)
        val networkInterfaces = Option(instance.getNetworkInterfaces).getOrElse(List[NetworkInterface]().asJava)
        val instanceId = Option(instance.getId).getOrElse(BigInt(0)).toString
        val status = defaultIfEmpty(instance.getStatus, EMPTY)
        val name = defaultIfEmpty(instance.getName, EMPTY)

        resultMap +
          (INSTANCE_ID -> instanceId) +
          (INSTANCE_DETAILS -> toPretty(prettyPrint, instance)) +
          (NAME -> name) +
          (IPS -> networkInterfaces.map(_.getNetworkIP).mkString(listDelimiterStr)) +
          (STATUS -> status)
      } else {
        val status = defaultIfEmpty(operation.getStatus, EMPTY)

        resultMap +
          (STATUS -> status)
      }
    } catch {
      case t: TimeoutException => getFailureResultsMap(TIMEOUT_EXCEPTION, t)
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
