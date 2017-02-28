package io.cloudslang.content.gcloud.actions.compute.instances

import java.util

import com.google.api.services.compute.model.Instance
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PASSWORD, DEFAULT_PROXY_PORT}
import io.cloudslang.content.gcloud.utils.InputNames._
import io.cloudslang.content.gcloud.utils.InputUtils.verifyEmpty
import io.cloudslang.content.gcloud.utils.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.gcloud.utils.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.defaultIfEmpty

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
      return getFailureResultsMap(validationStream.mkString("\n"))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)


    try {
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory

      val instanceDelimiter = if (prettyPrint) "\n" else ","

      val resultList = InstanceService.list(httpTransport, jsonFactory, credential, projectId, zone)
        .map { instance: Instance => if (prettyPrint) instance.toPrettyString else instance.toString }
        .mkString("[", instanceDelimiter, "]")
      getSuccessResultsMap(resultList)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
