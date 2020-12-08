package io.cloudslang.content.google.actions.storage.buckets

import java.util

import io.cloudslang.content.google.utils.action.InputNames.StorageBucketInputs.LIST_BUCKET_OPERATION_NAME
import io.cloudslang.content.google.utils.action.Descriptions.StorageBucketDesc.LIST_BUCKET_OPERATION_DESC
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.storage.buckets.BucketService
import io.cloudslang.content.google.utils.Constants.{ NEW_LINE}
import io.cloudslang.content.google.utils.action.DefaultValues.StorageBucket.DEFAULT_PROJECTION
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.google.utils.action.Descriptions.Common.{ACCESS_TOKEN_DESC, EXCEPTION_DESC, PRETTY_PRINT_DESC, PROJECT_ID_DESC, PROXY_HOST_DESC, PROXY_PASSWORD_DESC, PROXY_PORT_DESC, PROXY_USERNAME_DESC, RETURN_CODE_DESC, RETURN_RESULT_DESC}
import io.cloudslang.content.google.utils.action.InputNames.{ACCESS_TOKEN, PRETTY_PRINT, PROJECT_ID, PROXY_HOST, PROXY_PASSWORD, PROXY_PORT, PROXY_USERNAME}
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.DefaultValues.StorageBucket.DEFAULT_MAXRESULTS
import io.cloudslang.content.google.utils.action.InputNames.StorageBucketInputs.PROJECTION
import io.cloudslang.content.google.utils.action.InputNames.StorageBucketInputs.MAXRESULTS
import io.cloudslang.content.google.utils.action.InputNames.StorageBucketInputs.PREFIX
import io.cloudslang.content.google.utils.action.InputNames.StorageBucketInputs.PAGETOKEN
import io.cloudslang.content.google.utils.action.Descriptions.StorageBucketDesc.MAXRESULTS_DESC
import io.cloudslang.content.google.utils.action.Descriptions.StorageBucketDesc.PAGETOKEN_DESC
import io.cloudslang.content.google.utils.action.Descriptions.StorageBucketDesc.PREFIX_DESC
import io.cloudslang.content.google.utils.action.Descriptions.StorageBucketDesc.PROJECTION_DESC
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

class ListBucket {
  @Action(name = LIST_BUCKET_OPERATION_NAME,
    description = LIST_BUCKET_OPERATION_DESC,
    outputs = Array(
      new Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
      new Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
      new Output(value = EXCEPTION, description = EXCEPTION_DESC)
    )
    ,
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
        matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
        matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true, encrypted = true, description = PROJECT_ID_DESC) projectId: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true, description = ACCESS_TOKEN_DESC) accessToken: String,
              @Param(value = MAXRESULTS, description = MAXRESULTS_DESC) maxResults: String,
              @Param(value = PREFIX, description = PREFIX_DESC) prefix: String,
              @Param(value = PAGETOKEN, description = PAGETOKEN_DESC) pageToken: String,
              @Param(value = PROJECTION, description = PROJECTION_DESC) projection: String,
              @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) proxyHost: String,
              @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) proxyPort: String,
              @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) proxyPassword: String,
              @Param(value = PRETTY_PRINT, description = PRETTY_PRINT_DESC) prettyPrintInp: String): util.Map[String, String] = {

    val proxyHostStr = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val projectionStr = defaultIfEmpty(projection, DEFAULT_PROJECTION)
    val pageTokenStr = defaultIfEmpty(pageToken, DEFAULT_PROJECTION)
    val maxresultsInt = defaultIfEmpty(maxResults,DEFAULT_MAXRESULTS)
    val prefixStr = defaultIfEmpty(prefix,EMPTY)
    val proxyPortInt = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT)
    val proxyPasswordStr = defaultIfEmpty(proxyPassword, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)


    val validationStream = validateProxyPort(proxyPortInt) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPortVal = toInteger(proxyPortInt)
    val prettyPrint = toBoolean(prettyPrintStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostStr, proxyPortVal, proxyUsernameOpt,
        proxyPasswordStr)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)


      val listbucketDetails = BucketService.list(httpTransport, jsonFactory, credential,projectId,maxresultsInt,prefixStr,pageTokenStr,projectionStr)

      getSuccessResultsMap(toPretty(prettyPrint, listbucketDetails))
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }

}
