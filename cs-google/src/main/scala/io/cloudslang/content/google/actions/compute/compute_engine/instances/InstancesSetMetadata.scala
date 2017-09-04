package io.cloudslang.content.google.actions.compute.compute_engine.instances

import java.util

import com.google.api.services.compute.model.Metadata.Items
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.instances.{InstanceController, InstanceService}
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_ITEMS_DELIMITER, DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.google.utils.action.GoogleOutputNames.ZONE_OPERATION_NAME
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validatePairedLists, validateProxyPort}
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._

/**
  * Created by Tirla Alin
  * 2/27/2017.
  */
class InstancesSetMetadata {
  /**
    * Sets metadata for the specified instance to the data provided to the operation. Can be used as a delete metadata as well.
    *
    * @param projectId          Name of the Google Cloud project.
    * @param zone               Name of the zone for this request.
    * @param instanceName       Name of the instance scoping this request as seen in the google cloud console
    * @param accessToken        The access token from GetAccessToken.
    * @param itemsKeysListInp   Optional - key for the metadata entry. Keys must conform to the following regexp: [a-zA-Z0-9-_]+,
    *                           and be less than 128 bytes in length. This is reflected as part of a URL in the metadata
    *                           server. Additionally, to avoid ambiguity, keys must not conflict with any other metadata
    *                           keys for the project. The length of the itemsKeysList must be equal with the length of
    *                           the itemsValuesList.
    * @param itemsValuesListInp Optional - value for the metadata entry. These are free-form strings, and only have meaning as
    *                           interpreted by the image running in the instance. The only restriction placed on values
    *                           is that their size must be less than or equal to 32768 bytes. The length of the
    *                           itemsKeysList must be equal with the length of the itemsValuesList.
    * @param itemsDelimiterInp  The delimiter to split the <itemsKeysListInp> and <itemsValuesListInp>
    *                           Default: ','
    * @param proxyHostInp       Optional - proxy server used to connect to Google Cloud API. If empty no proxy will
    *                           be used.
    * @param proxyPortInp       Optional - proxy server port.
    *                           Default: "8080"
    * @param proxyUsernameInp   Optional - proxy server user name.
    * @param proxyPasswordInp   Optional - proxy server password associated with the proxyUsername input value.
    * @param prettyPrintInp     Optional - whether to format (pretty print) the resulting json.
    *                           Valid values: "true", "false"
    *                           Default: "true"
    * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
    *         operation, status of the ZoneOperation, or failure message and the exception if there is one
    */
  @Action(name = "Set Instance Metadata",
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
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,
              @Param(value = ITEMS_KEYS_LIST) itemsKeysListInp: String,
              @Param(value = ITEMS_VALUES_LIST) itemsValuesListInp: String,
              @Param(value = ITEMS_DELIMITER) itemsDelimiterInp: String,
              @Param(value = PROXY_HOST) proxyHostInp: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsernameInp: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHostInp)
    val proxyUsernameOpt = verifyEmpty(proxyUsernameInp)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, EMPTY)
    val itemsKeysList = defaultIfEmpty(itemsKeysListInp, EMPTY)
    val itemsValuesList = defaultIfEmpty(itemsValuesListInp, EMPTY)
    val itemsDelimiter = defaultIfEmpty(itemsDelimiterInp, DEFAULT_ITEMS_DELIMITER)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validatePairedLists(itemsKeysList, itemsValuesList, itemsDelimiter, ITEMS_KEYS_LIST, ITEMS_VALUES_LIST) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    try {

      val prettyPrint = toBoolean(prettyPrintStr)

      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, toInteger(proxyPortStr), proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val items: List[Items] = InstanceController.createMetadataItems(itemsKeysList, itemsValuesList, itemsDelimiter)

      val result = InstanceService.setMetadata(httpTransport, jsonFactory, credential, projectId, zone, instanceName, items, sync = false, 30000) //TODO
      val resultString = if (prettyPrint) result.toPrettyString else result.toString

      getSuccessResultsMap(resultString) + (ZONE_OPERATION_NAME -> result.getName)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
