package io.cloudslang.content.google.actions.compute.compute_engine.instances

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.BooleanValues.FALSE
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.disks.DiskController
import io.cloudslang.content.google.services.compute.compute_engine.instances.InstanceService
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_INTERFACE, DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT, DEFAULT_VOLUME_MOUNT_MODE}
import io.cloudslang.content.google.utils.action.GoogleOutputNames.ZONE_OPERATION_NAME
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._

/**
  * Created by sandorr on 5/2/2017.
  */
class InstancesAttachDisk {

  /**
    * Creates a disk resource in the specified project using the data included as inputs.
    *
    * @param projectId        Name of the Google Cloud project.
    * @param zone             Name of the zone for this request.
    * @param accessToken      The access token from GetAccessToken.
    * @param instanceName     Name of the instance to attach the disk to.
    * @param source           A valid partial or full URL to an existing Persistent Disk resource.
    * @param boot             Optional - Indicates that this is a boot disk. The virtual machine will use the first
    *                         partition of the disk for its root filesystem.
    * @param mode             Optional - The mode in which to attach the disk to the instance.
    *                         Valid values: "READ_WRITE", "READ_ONLY"
    *                         Default: "READ_WRITE"
    * @param autoDelete       Optional - Whether to delete the disk when the instance is deleted.
    *                         Default: "false"
    * @param deviceName       Optional - Specifies a unique device name of your choice that is reflected into the
    *                         /dev/disk/by-id/google-* tree of a Linux operating system running within the instance.
    *                         This name can be used to reference the device for mounting, resizing, and so on, from
    *                         within the instance.
    *                         Note: If not specified, the server chooses a default device name to apply to this disk,
    *                         in the form persistent-disks-x, where x is a number assigned by Google Compute Engine.
    *                         This field is only applicable for persistent disks.
    * @param interface        Optional - Specifies the disk interface to use for attaching this disk.
    *                         Note: Persistent disks must always use SCSI and the request will fail if you attempt to
    *                         attach a persistent disk in any other format than SCSI. Local SSDs can use either
    *                         NVME or SCSI.
    *                         Valid values: "SCSI", "NVME"
    *                         Default: "SCSI"
    * @param proxyHost        Optional - Proxy server used to connect to Google Cloud API. If empty no proxy will
    *                         be used.
    * @param proxyPortInp     Optional - Proxy server port.
    *                         Default: "8080"
    * @param proxyUsername    Optional - Proxy server user name.
    * @param proxyPasswordInp Optional - Proxy server password associated with the proxyUsername input value.
    * @param prettyPrintInp   Optional - Whether to format (pretty print) the resulting json.
    *                         Valid values: "true", "false"
    *                         Default: "true"
    * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
    *         operation, status of the ZoneOperation, or failure message and the exception if there is one
    */
  @Action(name = "Instances Attach Disk",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(ZONE_OPERATION_NAME)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,
              @Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
              @Param(value = SOURCE, required = true) source: String,
              @Param(value = BOOT) boot: String,
              @Param(value = MODE) mode: String,
              @Param(value = AUTO_DELETE) autoDelete: String,
              @Param(value = DEVICE_NAME) deviceName: String,
              @Param(value = INTERFACE) interface: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String
             ): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)
    val modeStr = defaultIfEmpty(mode, DEFAULT_VOLUME_MOUNT_MODE)
    val autoDeleteStr = defaultIfEmpty(autoDelete, FALSE)
    val bootStr = defaultIfEmpty(boot, FALSE)
    val interfaceStr = defaultIfEmpty(autoDelete, DEFAULT_INTERFACE)
    val deviceNameOpt = verifyEmpty(deviceName)


    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateBoolean(autoDeleteStr, AUTO_DELETE) ++
      validateBoolean(bootStr, BOOT)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    try {
      val proxyPort = toInteger(proxyPortStr)
      val prettyPrint = toBoolean(prettyPrintStr)
      val autoDelete = toBoolean(autoDeleteStr)
      val boot = toBoolean(bootStr)

      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val attachedDisk = DiskController.createAttachedDisk(
        boot = boot,
        autoDelete = autoDelete,
        mountMode = modeStr,
        deviceNameOpt = deviceNameOpt,
        sourceOpt = Some(source),
        interfaceOpt = Some(interfaceStr))

      val operation = InstanceService.attachDisk(httpTransport, jsonFactory, credential, projectId, zone, instanceName, attachedDisk)
      val resultString = if (prettyPrint) operation.toPrettyString else operation.toString

      getSuccessResultsMap(resultString) + (ZONE_OPERATION_NAME -> operation.getName)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}