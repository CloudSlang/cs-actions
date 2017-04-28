package io.cloudslang.content.gcloud.actions.compute.instances

import java.util

import com.google.api.services.compute.model._
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.actions.compute.utils.GetAccessToken
import io.cloudslang.content.gcloud.services.compute.disks.DiskController
import io.cloudslang.content.gcloud.services.compute.instances.{InstanceController, InstanceService}
import io.cloudslang.content.gcloud.services.compute.networks.NetworkController
import io.cloudslang.content.gcloud.utils.Constants.NEW_LINE
import io.cloudslang.content.gcloud.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.gcloud.utils.action.InputNames._
import io.cloudslang.content.gcloud.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.gcloud.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.gcloud.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._

/**
  * Created by victor on 01.03.2017.
  */
class InstancesInsert {

  @Action(name = "Insert Instance",
    outputs = Array(
      new Output(OutputNames.RETURN_CODE),
      new Output(OutputNames.RETURN_RESULT),
      new Output(OutputNames.EXCEPTION)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,

              @Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
              @Param(value = INSTANCE_DESCRIPTION, required = true) instanceDescription: String,
              @Param(value = MACHINE_TYPE, required = true) machineType: String,
              @Param(value = LIST_DELIMITER, required = true) listDelimiter: String,
              @Param(value = CAN_IP_FORWARD, required = true) canIpForward: String,

              @Param(value = METADATA_KEYS) metadataKeys: String,
              @Param(value = METADATA_VALUES) metadataValues: String,

              @Param(value = TAGS_LIST) tagsList: String,

              @Param(value = VOLUME_MOUNT_TYPE, required = true) volumeMountType: String,
              @Param(value = VOLUME_MOUNT_MODE, required = true) volumeMountMode: String,
              @Param(value = VOLUME_AUTO_DELETE, required = true) volumeAutoDelete: String,
              @Param(value = VOLUME_DISK_DEVICE_NAME, required = true) volumeDiskDeviceName: String,
              @Param(value = VOLUME_DISK_NAME, required = true) volumeDiskName: String,
              @Param(value = VOLUME_DISK_SOURCE_IMAGE, required = true) volumeDiskSourceImage: String,
              @Param(value = VOLUME_DISK_TYEPE, required = true) volumeDiskType: String,
              @Param(value = VOLUME_DISK_SIZE, required = true) volumeDiskSize: String,

              @Param(value = NETWORK, required = true) network: String,
              @Param(value = SUBNETWORK, required = true) subnetwork: String,
              @Param(value = ACCESS_CONFIG_NAME, required = true) accessConfigName: String,
              @Param(value = ACCESS_CONFIG_TYPE, required = true) accessConfigType: String,

              @Param(value = SCHEDULING_ON_HOST_MAINTENANCE, required = true) schedulingOnHostMaintenance: String,
              @Param(value = SCHEDULING_AUTOMATIC_RESTART, required = true) schedulingAutomaticRestart: String,
              @Param(value = SCHEDULING_PREEMPTIBLE, required = true) schedulingPreemptible: String,

              @Param(value = SERVICE_ACCOUNT_EMAIL, required = true) serviceAccountEmail: String,
              @Param(value = SERVICE_ACCOUNT_SCOPES, required = true) serviceAccountScopes: String,

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

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)


      val attachedDisk = DiskController.createAttachedDisk(
        boot = true,
        mountType = "PERSISTENT",
        mountMode = "READ_WRITE",
        autoDelete = true,
        diskDeviceName = "boot",
        diskName = "name1234",
        diskSourceImage = "projects/debian-cloud/global/images/debian-8-jessie-v20170426",
        diskType = "projects/rational-medley-165804/zones/us-central1-c/diskTypes/pd-standard",
        diskSize = 10L)

      val description = ""
      val machineType = "projects/rational-medley-165804/zones/europe-west1-b/machineTypes/n1-standard-1"
      val metadata = new Metadata().setItems(InstanceController.createMetadataItems("", "", ""))
      val tags = InstanceController.createTags("", "")
      val canIpForward = false

      val networkInterface = NetworkController.createNetworkInterface(Some("projects/rational-medley-165804/global/networks/default"),
        Some("projects/rational-medley-165804/regions/europe-west1/subnetworks/default"), Some("External NAT"), "ONE_TO_ONE_NAT")

      val serviceAccount = InstanceController.getServiceAccount(None, Some("https://www.googleapis.com/auth/compute"), ",")

      val scheduler = InstanceController.createScheduling(Some("MIGRATE"), automaticRestart = true, preemptible = false)

      val instance = InstanceController.createInstance(instanceName, description, zone, scheduler, machineType, metadata, tags,
        attachedDisk, networkInterface, canIpForward, serviceAccount)

      val operation = InstanceService.insert(httpTransport, jsonFactory, credential, projectId, zone, instance)
      val resultString = if (prettyPrint) operation.toPrettyString else operation.toString

      getSuccessResultsMap(resultString)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
