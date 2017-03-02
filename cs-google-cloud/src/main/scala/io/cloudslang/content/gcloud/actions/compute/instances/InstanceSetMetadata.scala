package io.cloudslang.content.gcloud.actions.compute.instances

import java.util

import com.google.api.services.compute.model.Metadata.Items
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
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
import io.cloudslang.content.gcloud.utils.action.GoogleOutputNames.ZONE_OPERATION_NAME

import scala.collection.JavaConversions._

/**
  * Created by Tirla Alin
  * 2/27/2017.
  */
class InstanceSetMetadata {
  /**
    * Sets metadata for the specified instance to the data provided to the operation. Can be used as a delete metadata as well.
    *
    * @param projectId         Name of the Google Cloud project.
    * @param zone              Name of the zone for this request.
    * @param instanceName      Name of the instance scoping this request as seen in the google cloud console
    * @param accessToken       The access token from GetAccessToken.
    * @param itemsKeysList     Optional - key for the metadata entry. Keys must conform to the following regexp: [a-zA-Z0-9-_]+,
    *                          and be less than 128 bytes in length. This is reflected as part of a URL in the metadata
    *                          server. Additionally, to avoid ambiguity, keys must not conflict with any other metadata
    *                          keys for the project. The length of the itemsKeysList must be equal with the length of
    *                          the itemsValuesList.
    *                          Default: ""
    * @param itemsValuesList   Optional - value for the metadata entry. These are free-form strings, and only have meaning as
    *                          interpreted by the image running in the instance. The only restriction placed on values
    *                          is that their size must be less than or equal to 32768 bytes. The length of the
    *                          itemsKeysList must be equal with the length of the itemsValuesList.
    *                          Default: ""
    * @param delimiterInp      The delimiter to split the items_keys_list and items_values_list
    *                          Default: ','
    * @param proxyHostInp      Optional - proxy server used to connect to Google Cloud API. If empty no proxy will
    *                          be used.
    *                          Default: ""
    * @param proxyPortInp      Optional - proxy server port.
    *                          Default: "80"
    * @param proxyUsernameInp  Optional - proxy server user name.
    *                          Default: ""
    * @param proxyPasswordInp  Optional - proxy server password associated with the proxyUsername input value.
    *                          Default: ""
    * @param prettyPrintInp    Optional - whether to format (pretty print) the resulting json.
    *                          Valid values: "true", "false"
    *                          Default: "true"
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

      getSuccessResultsMap(resultString) + (ZONE_OPERATION_NAME -> result.getName)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
