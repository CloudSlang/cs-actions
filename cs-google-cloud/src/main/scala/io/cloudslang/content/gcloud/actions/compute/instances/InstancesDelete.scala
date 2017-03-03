package io.cloudslang.content.gcloud.actions.compute.instances

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.Constants.NEW_LINE
import io.cloudslang.content.gcloud.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PASSWORD, DEFAULT_PROXY_PORT}
import io.cloudslang.content.gcloud.utils.action.GoogleOutputNames.ZONE_OPERATION_NAME
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
  * 2/27/2017.
  */
class InstancesDelete {

  /**
    * This operation can be used to delete an Instance resource. The operation returns a ZoneOperation resource as a
    * JSON object, that can be used to retrieve the status and progress of the ZoneOperation, using the
    * ZoneOperationsGet operation.
    *
    * @param projectId        Google Cloud project name.
    *                         Example: "example-project-a"
    * @param zone             The name of the zone in which the instance lives.
    *                         Examples: "us-central1-a", "us-central1-b", "us-central1-c"
    * @param instanceName     Name of the Instance resource to delete.
    *                         Example: "operation-1234"
    * @param accessToken      The access token returned by the GetAccessToken operation, with at least one of the
    *                         following scopes: "https://www.googleapis.com/auth/compute",
    *                         "https://www.googleapis.com/auth/cloud-platform".
    * @param proxyHost        Optional - Proxy server used to access the provider services.
    *                         Default: ""
    * @param proxyPortInp     Optional - Proxy server port used to access the provider services.
    *                         Default: "8080"
    * @param proxyUsername    Optional - Proxy server user name.
    *                         Default: ""
    * @param proxyPasswordInp Optional - Proxy server password associated with the proxy_username input value.
    *                         Default: ""
    * @param prettyPrintInp   Optional - Whether to format the resulting JSON.
    *                         Default: "true"
    * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
    *         operation, name of the created ZoneOperation, or failure message and the exception if there is one
    */
  @Action(name = "Delete Instance",
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
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
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

      val operation = InstanceService.delete(httpTransport, jsonFactory, credential, projectId, zone, instanceName)
      val resultString = if (prettyPrint) operation.toPrettyString else operation.toString

      getSuccessResultsMap(resultString) + (ZONE_OPERATION_NAME -> operation.getName)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
