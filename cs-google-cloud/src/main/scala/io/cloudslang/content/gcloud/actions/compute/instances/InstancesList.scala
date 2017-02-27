package io.cloudslang.content.gcloud.actions.compute.instances

import java.nio.charset.StandardCharsets
import java.util

import com.google.api.services.compute.ComputeScopes
import com.google.api.services.compute.model.Instance
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.{BooleanUtilities, OutputUtilities}
import org.apache.commons.io.IOUtils

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
  def execute(@Param(value = "projectId", required = true) projectId: String,
              @Param(value = "zone", required = true) zone: String,
              @Param(value = "proxyHost") proxyHost: String,
              @Param(value = "proxyPort") proxyPort: String,
              @Param(value = "proxyUsername") proxyUsername: String,
              @Param(value = "proxyPassword", encrypted = true) proxyPassword: String,
              @Param(value = "prettyPrint") prettyPrintStr: String,
              @Param(value = "", encrypted = true) jsonToken: String): util.Map[String, String] = {
    try {
//      val httpTransport = HttpTransportUtils.getNetHttpTransport(Option(proxyHost), NumberUtilities.toInteger(proxyPort), Option(proxyUsername), proxyPassword)
      val httpTransport = HttpTransportUtils.getNetHttpTransport()
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory

      val credential = GoogleAuth.fromJsonWithScopes(IOUtils.toInputStream(jsonToken, StandardCharsets.UTF_8), httpTransport, jsonFactory, List(ComputeScopes.COMPUTE_READONLY), timeout = 1999999)

      credential.refreshToken()
      val accessToken = credential.getAccessToken

      val theToken = GoogleAuth.fromAccessToken(accessToken)

      theToken.refreshToken()
      val accessToken2 = theToken.getAccessToken

      val theToken2 = GoogleAuth.fromAccessToken(accessToken2)

      val prettyPrint = BooleanUtilities.toBoolean(prettyPrintStr)

      val instanceDelimiter = if (prettyPrint) "\n" else ","

      val resultList2 = InstanceService.list(httpTransport, jsonFactory, theToken2, projectId, zone)

      val resultList = InstanceService.list(httpTransport, jsonFactory, theToken2, projectId, zone)
        .map { instance: Instance =>
          if (prettyPrint) {
            instance.toPrettyString
          } else {
            instance.toString
          }
        }.mkString("[", instanceDelimiter, "]")
      OutputUtilities.getSuccessResultsMap(resultList)
    } catch {
      case e: Throwable => OutputUtilities.getFailureResultsMap(e)
    }
  }

}
