package io.cloudslang.content.gcloud.actions.compute.utils

import java.nio.charset.StandardCharsets
import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.utils.DefaultValues.{DEFAULT_PROXY_PASSWORD, DEFAULT_PROXY_PORT}
import io.cloudslang.content.gcloud.utils.InputNames._
import io.cloudslang.content.gcloud.utils.InputUtils.verifyEmpty
import io.cloudslang.content.gcloud.utils.InputValidator.validateProxyPort
import io.cloudslang.content.gcloud.utils.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap
import io.cloudslang.content.utils.{NumberUtilities, OutputUtilities}
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils.defaultIfEmpty

/**
  * Created by victor on 28.02.2017.
  */
class GetAccessToken {


  /*
  scopes: see https://developers.google.com/identity/protocols/googlescopes#computev1
   */

  @Action(name = "Get the access token for Google Cloud",
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
  def execute(@Param(value = JSON_TOKEN, required = true, encrypted = true) jsonToken: String,
              @Param(value = SCOPES, required = true) scopes: String,
              @Param(value = TIMEOUT) timeoutStr: String,
              @Param(value = SCOPES_DELIMITER) scopesDel: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, DEFAULT_PROXY_PASSWORD)

    val validationStream = validateProxyPort(proxyPortStr)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString("\n"))
    }

    val proxyPort = toInteger(proxyPortStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
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
