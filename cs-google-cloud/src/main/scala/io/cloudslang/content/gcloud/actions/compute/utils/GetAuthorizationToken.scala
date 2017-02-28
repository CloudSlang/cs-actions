package io.cloudslang.content.gcloud.actions.compute.utils

import java.nio.charset.StandardCharsets
import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.utils.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.{NumberUtilities, OutputUtilities}
import org.apache.commons.io.IOUtils

/**
  * Created by victor on 28.02.2017.
  */
class GetAuthorizationToken {


  /*
  scopes: see https://developers.google.com/identity/protocols/googlescopes#computev1
   */

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
  def execute(@Param(value = "jsonToken", required = true, encrypted = true) jsonToken: String,
              @Param(value = "timeout") timeoutStr: String,
              @Param(value = "scopes") scopes: String,
              @Param(value = "scopesDelimiter") scopesDel: String,
              @Param(value = "proxyHost") proxyHost: String,
              @Param(value = "proxyPort") proxyPort: String,
              @Param(value = "proxyUsername") proxyUsername: String,
              @Param(value = "proxyPassword", encrypted = true) proxyPassword: String): util.Map[String, String] = {
    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(Option(proxyHost), NumberUtilities.toInteger(proxyPort), Option(proxyUsername), proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory

      val timeout = NumberUtilities.toInteger(timeoutStr)

      val credential = GoogleAuth.fromJsonWithScopes(IOUtils.toInputStream(jsonToken, StandardCharsets.UTF_8),
        httpTransport, jsonFactory, scopes.split(scopesDel), timeout = timeout)
      val accessToken = GoogleAuth.getAccessTokenFromCredentials(credential)

      OutputUtilities.getSuccessResultsMap(accessToken)
    } catch {
      case e: Throwable => OutputUtilities.getFailureResultsMap(e)
    }
  }
}
