package io.cloudslang.content.google.actions.compute.compute_engine.instances

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.BooleanValues.FALSE
import io.cloudslang.content.constants.OutputNames._
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.instances.InstanceService
import io.cloudslang.content.google.utils.Constants._
import io.cloudslang.content.google.utils.action.DefaultValues._
import io.cloudslang.content.google.utils.action.GoogleOutputNames.{INSTANCE_DETAILS, STATUS, ZONE_OPERATION_NAME => _}
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils._
import io.cloudslang.content.google.utils.action.InputValidator._
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities._
import io.cloudslang.content.utils.NumberUtilities._
import io.cloudslang.content.utils.OutputUtilities._
import org.apache.commons.lang3.StringUtils._

import scala.collection.JavaConversions._
import scala.concurrent.TimeoutException

/**
  * Created by pinteae on 3/2/2017.
  */
class InstancesRestart {

  /**
    * This operation can be used to restart an Instance resource. The operation returns a ZoneOperation resource as a
    * JSON object, that can be used to retrieve the status and progress of the ZoneOperation, using the
    * ZoneOperationsGet operation.
    *
    * @param projectId          Google Cloud project id.
    *                           Example: "example-project-a"
    * @param zone               The name of the zone where the Instance resource is located.
    *                           Examples: "us-central1-a", "us-central1-b", "us-central1-c"
    * @param instanceName       Name of the Instance resource to delete.
    *                           Example: "operation-1234"
    * @param accessToken        The access token returned by the GetAccessToken operation, with at least the
    *                           following scope: "https://www.googleapis.com/auth/compute".
    * @param syncInp            Optional - Boolean specifying whether the operation to run sync or async.
    *                           Valid values: "true", "false"
    *                           Default: "false"
    * @param timeoutInp         Optional - The time, in seconds, to wait for a response if the sync input is set to "true".
    *                           If the value is 0, the operation will wait until zone operation progress is 100.
    *                           Valid values: Any positive number including 0.
    *                           Default: "30"
    * @param pollingIntervalInp Optional - The time, in seconds, to wait before a new request that verifies if the operation finished
    *                           is executed, if the sync input is set to "true".
    *                           Valid values: Any positive number including 0.
    *                           Default: "1"
    * @param proxyHost          Optional - Proxy server used to access the provider services.
    * @param proxyPortInp       Optional - Proxy server port used to access the provider services.
    *                           Default: "8080"
    * @param proxyUsername      Optional - Proxy server user name.
    * @param proxyPasswordInp   Optional - Proxy server password associated with the <proxyUsername> input value.
    * @param prettyPrintInp     Optional - Whether to format the resulting JSON.
    *                           Default: "true"
    * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
    *         operation, status of the ZoneOperation if the <syncInp> is false. If <syncInp> is true the map will also
    *         contain the name of the instance, the details of the instance and the status of the operation.
    *         In case an exception occurs the failure message is provided.
    */
  @Action(name = "Restart Instance",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(ZONE_OPERATION_NAME),
      new Output(INSTANCE_NAME),
      new Output(INSTANCE_DETAILS),
      new Output(STATUS)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
              @Param(value = ACCESS_TOKEN, required = true) accessToken: String,
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
    val proxyPasswordStr = defaultIfEmpty(proxyPasswordInp, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)
    val syncStr = defaultIfEmpty(syncInp, FALSE)
    val timeoutStr = defaultIfEmpty(timeoutInp, DEFAULT_SYNC_TIMEOUT)
    val pollingIntervalStr = defaultIfEmpty(pollingIntervalInp, DEFAULT_POLLING_INTERVAL)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateBoolean(syncStr, SYNC) ++
      validateNonNegativeLong(timeoutStr, TIMEOUT) ++
      validateNonNegativeDouble(pollingIntervalStr, POLLING_INTERVAL)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)
    val sync = toBoolean(syncStr)
    val timeout = toLong(timeoutStr)
    val pollingInterval = toDouble(pollingIntervalStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPasswordStr)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val pollingIntervalMilli = convertSecondsToMilli(pollingInterval)

      val operation = InstanceService.restart(httpTransport, jsonFactory, credential, projectId, zone, instanceName, sync, timeout, pollingIntervalMilli)
      val resultMap = getSuccessResultsMap(toPretty(prettyPrint, operation)) + (ZONE_OPERATION_NAME -> operation.getName)

      if (sync) {
        val instance = InstanceService.get(httpTransport, jsonFactory, credential, projectId, zone, instanceName)
        val name = Option(instance.getName).getOrElse("")
        val status = Option(operation.getStatus).getOrElse("")

        resultMap +
          (INSTANCE_NAME -> name) +
          (INSTANCE_DETAILS -> toPretty(prettyPrint, instance)) +
          (STATUS -> status)
      } else {
        resultMap
      }
    } catch {
      case t: TimeoutException => getFailureResultsMap(TIMEOUT_EXCEPTION, t)
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
