package io.cloudslang.content.google.actions.authentication

import java.nio.charset.StandardCharsets
import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_PROXY_PORT, DEFAULT_SCOPES_DELIMITER, DEFAULT_TIMEOUT}
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateNonNegativeLong, validateProxyPort}
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.NumberUtilities.{toLong, toInteger}
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

/**
  * Created by victor on 28.02.2017.
  */
class GetAccessToken {

  /**
    * This operation can be used to retrieve an access token to be used in subsequent google compute operations.
    *
    * @param jsonToken        Content of the Google Cloud service account JSON.
    * @param scopes           Scopes that you might need to request to access Google Compute APIs, depending on the level of access
    *                         you need. One or more scopes may be specified delimited by the <scopesDelimiter>.
    *                         Example: 'https://www.googleapis.com/auth/compute.readonly'
    *                         Note: It is recommended to use the minimum necessary scope in order to perform the requests.
    *                         For a full list of scopes see https://developers.google.com/identity/protocols/googlescopes#computev1
    * @param scopesDelInp     Optional - Delimiter that will be used for the <scopes> input.
    *                         Default: ","
    * @param timeoutInp       Optional - Timeout of the resulting access token, in seconds.
    *                         Default: "600"
    * @param proxyHost        Optional - Proxy server used to access the provider services.
    * @param proxyPortInp     Optional - Proxy server port used to access the provider services.
    *                         Default: "8080"
    * @param proxyUsername    Optional - Proxy server user name.
    * @param proxyPasswordInp Optional - Proxy server password associated with the <proxyUsername> input value.
    * @return a map containing an access token as returnResult
    */

  @Action(name = "Get the access token for Google Cloud",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = JSON_TOKEN, required = true, encrypted = true) jsonToken: String,
              @Param(value = SCOPES, required = true) scopes: String,
              @Param(value = SCOPES_DELIMITER) scopesDelInp: String,
              @Param(value = TIMEOUT) timeoutInp: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, EMPTY)
    val scopesDel = defaultIfEmpty(scopesDelInp, DEFAULT_SCOPES_DELIMITER)
    val timeoutStr = defaultIfEmpty(timeoutInp, DEFAULT_TIMEOUT)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateNonNegativeLong(timeoutStr, TIMEOUT)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val timeout = toLong(timeoutStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory

      val credential = GoogleAuth.fromJsonWithScopes(IOUtils.toInputStream(jsonToken, StandardCharsets.UTF_8),
        httpTransport, jsonFactory, scopes.split(scopesDel), timeout)

      val accessToken = GoogleAuth.getAccessTokenFromCredentials(credential)

      getSuccessResultsMap(accessToken)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
