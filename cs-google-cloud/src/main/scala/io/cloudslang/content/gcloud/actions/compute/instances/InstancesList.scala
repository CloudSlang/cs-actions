package io.cloudslang.content.gcloud.actions.compute.instances

import java.util

import com.google.api.services.compute.model.Instance
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.InputNames._
import io.cloudslang.content.gcloud.utils.InputUtils.verifyEmpty
import io.cloudslang.content.gcloud.utils.{GoogleAuth, HttpTransportUtils, InputUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.{BooleanUtilities, NumberUtilities, OutputUtilities}

/**
  * Created by victor on 27.02.2017.
  */
class InstancesList {

  @Action(name = "List Instances",
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
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPort: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPassword: String,
              @Param(value = PRETTY_PRINT) prettyPrintStr: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)

    try {
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val prettyPrint = BooleanUtilities.toBoolean(prettyPrintStr)

      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, NumberUtilities.toInteger(proxyPort), proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory

      val instanceDelimiter = if (prettyPrint) "\n" else ","

      val resultList = InstanceService.list(httpTransport, jsonFactory, credential, projectId, zone)
        .map { instance: Instance => if (prettyPrint) instance.toPrettyString else instance.toString }
        .mkString("[", instanceDelimiter, "]")
      OutputUtilities.getSuccessResultsMap(resultList)
    } catch {
      case e: Throwable => OutputUtilities.getFailureResultsMap(e)
    }
  }
}
