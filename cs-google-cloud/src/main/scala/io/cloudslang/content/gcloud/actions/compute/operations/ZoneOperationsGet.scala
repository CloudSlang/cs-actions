package io.cloudslang.content.gcloud.actions.compute.operations

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.services.compute.operations.ZoneOperationService
import io.cloudslang.content.gcloud.utils.Constants.NEW_LINE
import io.cloudslang.content.gcloud.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PASSWORD, DEFAULT_PROXY_PORT}
import io.cloudslang.content.gcloud.utils.action.GoogleOutputNames.STATUS
import io.cloudslang.content.gcloud.utils.action.InputNames._
import io.cloudslang.content.gcloud.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.gcloud.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.gcloud.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.defaultIfEmpty

import scala.collection.JavaConversions._

/**
  * Created by sandorr 
  * 3/1/2017.
  */
class ZoneOperationsGet {

  /**
    * This operation can be used to retrieve a ZoneOperation resource, as JSON object.
    *
    * @param projectId         Google Cloud project name.
    *                          Example: "example-project-a"
    * @param zone              The name of the zone in which the instance lives.
    *                          Examples: "us-central1-a", "us-central1-b", "us-central1-c"
    * @param zoneOperationName Name of the ZoneOperation resource to return.
    *                          Example: "operation-1234"
    * @param accessToken       The access token returned by the GetAccessToken operation, with at least one of the
    *                          following scopes: "https://www.googleapis.com/auth/compute.readonly",
    *                          "https://www.googleapis.com/auth/compute",
    *                          "https://www.googleapis.com/auth/cloud-platform".
    * @param proxyHost         Optional - Proxy server used to access the provider services.
    *                          Default: ""
    * @param proxyPortInp      Optional - Proxy server port used to access the provider services.
    *                          Default: "8080"
    * @param proxyUsername     Optional - Proxy server user name.
    *                          Default: ""
    * @param proxyPasswordInp  Optional - Proxy server password associated with the proxy_username input value.
    *                          Default: ""
    * @param prettyPrintInp    Optional - Whether to format the resulting JSON.
    *                          Default: "true"
    * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
    *         operation, status of the ZoneOperation, or failure message and the exception if there is one
    */
  @Action(name = "Get ZoneOperation",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(STATUS)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
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
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, DEFAULT_PROXY_PASSWORD)
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
