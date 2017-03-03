package io.cloudslang.content.gcloud.actions.compute.instances

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames._
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.Constants._
import io.cloudslang.content.gcloud.utils.action.DefaultValues._
import io.cloudslang.content.gcloud.utils.action.GoogleOutputNames.ZONE_OPERATION_NAME
import io.cloudslang.content.gcloud.utils.action.InputNames._
import io.cloudslang.content.gcloud.utils.action.InputUtils._
import io.cloudslang.content.gcloud.utils.action.InputValidator._
import io.cloudslang.content.gcloud.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities._
import io.cloudslang.content.utils.NumberUtilities._
import io.cloudslang.content.utils.OutputUtilities._
import org.apache.commons.lang3.StringUtils._

import scala.collection.JavaConversions._

/**
  * Created by pinteae on 3/2/2017.
  */
class InstancesRestart {

  /**
    * This operation can be used to restart an Instance resource. The operation returns a ZoneOperation resource as a
    * JSON object, that can be used to retrieve the status and progress of the ZoneOperation, using the
    * ZoneOperationsGet operation.
    *
    * @param projectId        Google Cloud project id.
    *                         Example: "example-project-a"
    * @param zone             The name of the zone where the Instance resource is located.
    *                         Examples: "us-central1-a", "us-central1-b", "us-central1-c"
    * @param instanceName     Name of the Instance resource to delete.
    *                         Example: "operation-1234"
    * @param accessToken      The access token returned by the GetAccessToken operation, with at least the
    *                         following scope: "https://www.googleapis.com/auth/compute".
    * @param proxyHost        Optional - Proxy server used to access the provider services.
    * @param proxyPortInp     Optional - Proxy server port used to access the provider services.
    *                         Default: "8080"
    * @param proxyUsername    Optional - Proxy server user name.
    * @param proxyPasswordInp Optional - Proxy server password associated with the <proxyUsername> input value.
    * @param prettyPrintInp   Optional - Whether to format the resulting JSON.
    *                         Default: "true"
    * @return a map containing a ZoneOperation resource as returnResult, and it's name as zoneOperationName
    */
  @Action(name = "Restart Instance",
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
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
              @Param(value = ACCESS_TOKEN, required = true) accessToken: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {
    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPasswordStr = defaultIfEmpty(proxyPasswordInp, DEFAULT_PROXY_PASSWORD)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPasswordStr)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val operation = InstanceService.restart(httpTransport, jsonFactory, credential, projectId, zone, instanceName)
      val resultString = if (prettyPrint) operation.toPrettyString else operation.toString

      getSuccessResultsMap(resultString) + (ZONE_OPERATION_NAME -> operation.getName)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
