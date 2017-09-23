package io.cloudslang.content.google.actions.compute.compute_engine.instances

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.BooleanValues.FALSE
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.instances.InstanceService
import io.cloudslang.content.google.utils.Constants.{NEW_LINE, TIMEOUT_EXCEPTION}
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_POLLING_INTERVAL, DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT, DEFAULT_SYNC_TIMEOUT}
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
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._
import scala.concurrent.TimeoutException

/**
  * Created by sandorr
  * 2/27/2017.
  */
class InstancesDelete {

  /**
    * This operation can be used to delete an Instance resource. The operation returns a ZoneOperation resource as a
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
    * @param proxyHost          Optional - Proxy server used to access the provider services.
    * @param proxyPortInp       Optional - Proxy server port used to access the provider services.
    *                           Default: "8080"
    * @param proxyUsername      Optional - Proxy server user name.
    * @param proxyPasswordInp   Optional - Proxy server password associated with the <proxyUsername> input value.
    * @param prettyPrintInp     Optional - Whether to format the resulting JSON.
    *                           Default: "true"
    * @return a map containing a ZoneOperation resource as returnResult, it's name as zoneOperationName,
    *         the status of the operation and the instance name.
    *         In case an exception occurs the failure message is provided.
    */
  @Action(name = "Delete Instance",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(ZONE_OPERATION_NAME),
      new Output(INSTANCE_NAME),
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
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,
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
    val asyncStr = defaultIfEmpty(asyncInp, FALSE)
    val timeoutStr = defaultIfEmpty(timeoutInp, DEFAULT_SYNC_TIMEOUT)
    val pollingIntervalStr = defaultIfEmpty(pollingIntervalInp, DEFAULT_POLLING_INTERVAL)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateBoolean(asyncStr, ASYNC) ++
      validateNonNegativeLong(timeoutStr, TIMEOUT) ++
      validateNonNegativeDouble(pollingIntervalStr, POLLING_INTERVAL)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)
    val async = toBoolean(asyncStr)
    val timeout = toLong(timeoutStr)
    val pollingIntervalMilli = convertSecondsToMilli(toDouble(pollingIntervalStr))

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      OperationStatus(InstanceService.delete(httpTransport, jsonFactory, credential, projectId, zone, instanceName, async,
        timeout, pollingIntervalMilli)) match {
        case SuccessOperation(operation) =>
          val name = defaultIfEmpty(operation.getName, EMPTY)
          val status = defaultIfEmpty(operation.getStatus, EMPTY)

          getSuccessResultsMap(toPretty(prettyPrint, operation)) +
            (ZONE_OPERATION_NAME -> name) +
            (INSTANCE_NAME -> instanceName) +
            (STATUS -> status)
        case ErrorOperation(error) => getFailureResultsMap(error)
      }
    } catch {
      case t: TimeoutException => getFailureResultsMap(TIMEOUT_EXCEPTION, t)
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
