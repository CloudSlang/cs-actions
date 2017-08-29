package io.cloudslang.content.google.actions.compute.operations

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.operations.ZoneOperationService
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.google.utils.action.GoogleOutputNames.STATUS
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
  * Created by sandorr 
  * 3/1/2017.
  */
class ZoneOperationsGet {

  /**
    * This operation can be used to retrieve a ZoneOperation resource, as JSON object.
    *
    * @param projectId         Google Cloud project id.
    *                          Example: "example-project-a"
    * @param zone              The name of the zone where the Instance resource is located.
    *                          Examples: "us-central1-a", "us-central1-b", "us-central1-c"
    * @param zoneOperationName Name of the ZoneOperation resource to return.
    *                          Example: "operation-1234"
    * @param accessToken       The access token returned by the GetAccessToken operation, with at least the
    *                          following scope: "https://www.googleapis.com/auth/compute.readonly".
    * @param proxyHost         Optional - Proxy server used to access the provider services.
    * @param proxyPortInp      Optional - Proxy server port used to access the provider services.
    *                          Default: "8080"
    * @param proxyUsername     Optional - Proxy server user name.
    * @param proxyPasswordInp  Optional - Proxy server password associated with the <proxyUsername> input value.
    * @param prettyPrintInp    Optional - Whether to format the resulting JSON.
    *                          Default: "true"
    * @return a map containing a ZoneOperation resource as returnResult, and it's status
    */
  @Action(name = "Get ZoneOperation",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(STATUS)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = ZONE_OPERATION_NAME, required = true) zoneOperationName: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,
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

      val operation = ZoneOperationService.get(httpTransport, jsonFactory, credential, projectId, zone, zoneOperationName)
      val resultString = if (prettyPrint) operation.toPrettyString else operation.toString

      getSuccessResultsMap(resultString) + (STATUS -> operation.getStatus)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
