package io.cloudslang.content.google.actions.storage.buckets

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.storage.buckets.BucketService
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.google.utils.action.Descriptions.Common._
import io.cloudslang.content.google.utils.action.Descriptions.CreateSQLDataBaseInstance.SELF_LINK_DESC
import io.cloudslang.content.google.utils.action.Descriptions.StorageBucketDesc.BUCKET_NAME_DESC
import io.cloudslang.content.google.utils.action.InputNames.StorageBucketInputs.BUCKET_NAME
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.action.Outputs.SQLDatabaseInstance.SELF_LINK
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._

class GetBucket {
  @Action(name = "Get Bucket",
    description = "Retrieves a resource containing information about a bucket inside a Google Cloud Storage.",
    outputs = Array(
      new Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
      new Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
      new Output(value = EXCEPTION, description = EXCEPTION_DESC),
      new Output(value = SELF_LINK, description = SELF_LINK_DESC)
    )
    ,
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
        matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
        matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = ACCESS_TOKEN, required = true, encrypted = true, description = ACCESS_TOKEN_DESC) accessToken: String,
              @Param(value = BUCKET_NAME, required = true, description = BUCKET_NAME_DESC) bucketName: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPort: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPassword: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {


    val proxyHostStr = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
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

      val bucketDetails = BucketService.get(httpTransport, jsonFactory, credential, bucketName)


      getSuccessResultsMap(toPretty(prettyPrint, bucketDetails)) +
        (SELF_LINK -> bucketDetails.getSelfLink)

    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
