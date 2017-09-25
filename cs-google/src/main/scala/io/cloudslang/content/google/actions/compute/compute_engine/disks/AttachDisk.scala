package io.cloudslang.content.google.actions.compute.compute_engine.disks

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.BooleanValues.{FALSE, TRUE}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.disks.{DiskController, DiskService}
import io.cloudslang.content.google.services.compute.compute_engine.instances.InstanceService
import io.cloudslang.content.google.utils.Constants.{COMMA, NEW_LINE, TIMEOUT_EXCEPTION}
import io.cloudslang.content.google.utils.action.DefaultValues._
import io.cloudslang.content.google.utils.action.GoogleOutputNames.{ZONE_OPERATION_NAME => _, _}
import io.cloudslang.content.google.utils.action.InputNames.{ZONE_OPERATION_NAME, _}
import io.cloudslang.content.google.utils.action.InputUtils.{convertSecondsToMilli, verifyEmpty}
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateNonNegativeDouble, validateNonNegativeLong, validateProxyPort}
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.google.utils.{ErrorOperation, OperationStatus, SuccessOperation}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.{toDouble, toInteger, toLong}
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty, equals => strEquals}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.concurrent.TimeoutException

/**
  * Created by sandorr on 5/2/2017.
  */
class AttachDisk {

  /**
    * Creates a disk resource in the specified project using the data included as inputs.
    *
    * @param projectId          Name of the Google Cloud project.
    * @param zone               Name of the zone for this request.
    * @param accessToken        The access token from GetAccessToken.
    * @param instanceName       Name of the instance to attach the disk to.
    * @param source             A valid partial or full URL to an existing Persistent Disk resource.
    * @param mode               Optional - The mode in which to attach the disk to the instance.
    *                           Valid values: "READ_WRITE", "READ_ONLY"
    *                           Default: "READ_WRITE"
    * @param autoDelete         Optional - Whether to delete the disk when the instance is deleted.
    *                           Default: "false"
    * @param deviceName         Optional - Specifies a unique device name of your choice that is reflected into the
    *                           /dev/disk/by-id/google-* tree of a Linux operating system running within the instance.
    *                           This name can be used to reference the device for mounting, resizing, and so on, from
    *                           within the instance.
    *                           Note: If not specified, the server chooses a default device name to apply to this disk,
    *                           in the form persistent-disks-x, where x is a number assigned by Google Compute Engine.
    *                           This field is only applicable for persistent disks.
    * @param asyncInp           Optional - Boolean specifying whether the operation to run sync or async.
    *                           Valid values: "true", "false"
    *                           Default: "true"
    * @param timeoutInp         Optional - The time, in seconds, to wait for a response if the async input is set to "false".
    *                           If the value is 0, the operation will wait until zone operation progress is 100.
    *                           Valid values: Any positive number including 0.
    *                           Default: "30"
    * @param pollingIntervalInp Optional - The time, in seconds, to wait before a new request that verifies if the operation finished
    *                           is executed, if the async input is set to "false".
    *                           Valid values: Any positive number including 0.
    *                           Default: "1"
    * @param proxyHost          Optional - Proxy server used to connect to Google Cloud API. If empty no proxy will
    *                           be used.
    * @param proxyPortInp       Optional - Proxy server port.
    *                           Default: "8080"
    * @param proxyUsername      Optional - Proxy server user name.
    * @param proxyPasswordInp   Optional - Proxy server password associated with the proxyUsername input value.
    * @param prettyPrintInp     Optional - Whether to format (pretty print) the resulting json.
    *                           Valid values: "true", "false"
    *                           Default: "true"
    * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
    *         operation, status of the ZoneOperation if the <asyncInp> is true. If <asyncInp> is false the map will also
    *         contain the name of the instance, the details of the instance, including the attached disks and the
    *         device name of the newly attached disk, and the status of the operation will be replaced by the status of
    *         the instance.
    *         In case an exception occurs the failure message is provided.
    */
  @Action(name = "Attach Disk",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(ZONE_OPERATION_NAME),
      new Output(INSTANCE_NAME),
      new Output(INSTANCE_DETAILS),
      new Output(DISKS),
      new Output(STATUS),
      new Output(DEVICE_NAME)
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
              @Param(value = MODE) mode: String,
              @Param(value = AUTO_DELETE) autoDelete: String,
              @Param(value = DEVICE_NAME) deviceName: String,
              @Param(value = ASYNC) asyncInp: String,
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
    val modeStr = defaultIfEmpty(mode, DEFAULT_VOLUME_MOUNT_MODE)
    val autoDeleteStr = defaultIfEmpty(autoDelete, FALSE)
    val deviceNameOpt = verifyEmpty(deviceName)
    val asyncStr = defaultIfEmpty(asyncInp, TRUE)
    val timeoutStr = defaultIfEmpty(timeoutInp, DEFAULT_SYNC_TIMEOUT)
    val pollingIntervalStr = defaultIfEmpty(pollingIntervalInp, DEFAULT_POLLING_INTERVAL)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateBoolean(autoDeleteStr, AUTO_DELETE) ++
      validateBoolean(asyncStr, ASYNC) ++
      validateNonNegativeLong(timeoutStr, TIMEOUT) ++
      validateNonNegativeDouble(pollingIntervalStr, POLLING_INTERVAL)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    try {
      val proxyPort = toInteger(proxyPortStr)
      val prettyPrint = toBoolean(prettyPrintStr)
      val autoDelete = toBoolean(autoDeleteStr)
      val async = toBoolean(asyncStr)
      val timeout = toLong(timeoutStr)
      val pollingIntervalMilli = convertSecondsToMilli(toDouble(pollingIntervalStr))

      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val attachedDisk = DiskController.createAttachedDisk(
        boot = false,
        autoDelete = autoDelete,
        mountMode = modeStr,
        deviceNameOpt = deviceNameOpt,
        sourceOpt = Some(source),
        interfaceOpt = None)

      OperationStatus(InstanceService.attachDisk(httpTransport, jsonFactory, credential, projectId, zone, instanceName,
        attachedDisk, async, timeout, pollingIntervalMilli)) match {
        case SuccessOperation(operation) =>
          val resultMap = getSuccessResultsMap(toPretty(prettyPrint, operation)) + (ZONE_OPERATION_NAME -> operation.getName)

          if (async) {
            val status = defaultIfEmpty(operation.getStatus, EMPTY)

            resultMap +
              (STATUS -> status)
          } else {
            val instance = InstanceService.get(httpTransport, jsonFactory, credential, projectId, zone, instanceName)
            val disk = DiskService.get(httpTransport, jsonFactory, credential, projectId, zone, source.split("/").lastOption.getOrElse(EMPTY))

            val name = defaultIfEmpty(instance.getName, EMPTY)
            val status = defaultIfEmpty(instance.getStatus, EMPTY)
            val disksNames = Option(instance.getDisks).getOrElse(List().asJava).map(_.getDeviceName)
            val deviceNameOut = Option(instance.getDisks).getOrElse(List().asJava)
              .find(attachedDisk => strEquals(attachedDisk.getSource, disk.getSelfLink))
              .map(_.getDeviceName)
              .getOrElse(EMPTY)

            resultMap +
              (INSTANCE_NAME -> name) +
              (INSTANCE_DETAILS -> toPretty(prettyPrint, instance)) +
              (DISKS -> disksNames.mkString(COMMA)) +
              (STATUS -> status) +
              (DEVICE_NAME -> deviceNameOut)
          }
        case ErrorOperation(error) => getFailureResultsMap(error)
      }

    } catch {
      case t: TimeoutException => getFailureResultsMap(TIMEOUT_EXCEPTION, t)
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
