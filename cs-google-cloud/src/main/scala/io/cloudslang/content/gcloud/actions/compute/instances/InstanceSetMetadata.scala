package io.cloudslang.content.gcloud.actions.compute.instances

import java.util

import com.google.api.services.compute.model.Metadata.Items
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.actions.compute.utils.GetAccessToken
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.Constants.NEW_LINE
import io.cloudslang.content.gcloud.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PASSWORD, DEFAULT_PROXY_PORT, DEFAULT_SCOPES_DELIMITER}
import io.cloudslang.content.gcloud.utils.action.InputNames._
import io.cloudslang.content.gcloud.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.gcloud.utils.action.InputValidator.validateProxyPort
import io.cloudslang.content.gcloud.utils.action.InputValidator.validatePairedLists
import io.cloudslang.content.gcloud.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.StringUtils.defaultIfEmpty
import io.cloudslang.content.gcloud.utils.action.OutputNames.ZONE_OPERATION_NAME

/**
  * Created by Tirla Alin
  * 2/27/2017.
  */
class InstanceSetMetadata {

  @Action(name = "Set Instance Metadata",
    outputs = Array(
      new Output(OutputNames.RETURN_CODE),
      new Output(OutputNames.RETURN_RESULT),
      new Output(OutputNames.EXCEPTION),
      new Output(ZONE_OPERATION_NAME)
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
              @Param(value = ITEMS_KEYS_LIST) itemsKeysList: String,
              @Param(value = ITEMS_VALUES_LIST) itemsValuesList: String,
              @Param(value = SCOPES_DELIMITER) delimiterInp: String,
              @Param(value = PROXY_HOST) proxyHostInp: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsernameInp: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {

    try {
      val delimiter = defaultIfEmpty(delimiterInp, DEFAULT_SCOPES_DELIMITER)

      val proxyHost = verifyEmpty(proxyHostInp)
      val proxyUsername = verifyEmpty(proxyUsernameInp)
      val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
      val proxyPassword = defaultIfEmpty(proxyPasswordInp, DEFAULT_PROXY_PASSWORD)
      val prettyPrint = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)

      val validationStream = validateProxyPort(proxyPortStr) ++
        validatePairedLists(itemsKeysList, itemsValuesList, delimiter)

      if (validationStream.nonEmpty) {
        return getFailureResultsMap(validationStream.mkString(NEW_LINE))
      }

      val items = StringUtils.split(itemsKeysList, delimiter).zip(StringUtils.split(itemsValuesList, delimiter)).map {
        case (key, value) => new Items().setKey(key).setValue(value)
      }.toList

      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHost, toInteger(proxyPortStr), proxyUsername, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory

      val credential = GoogleAuth.fromAccessToken(accessToken)

      val result = InstanceService.setMetadata(httpTransport, jsonFactory, credential, projectId, zone, instanceName, items)
      val resultString = if (toBoolean(prettyPrint)) result.toPrettyString else result.toString

      val mapResult = getSuccessResultsMap(resultString)

      mapResult.put(ZONE_OPERATION_NAME, result.getName)
      mapResult
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
